/*
 * Copyright 2021 Shingo OKAWA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import React from "react";
import Draggable from "react-draggable";
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Paper,
  TextField,
} from "@material-ui/core";
import { PaperProps } from "@material-ui/core/Paper";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import * as Context from "../../../../contexts/tweet/timeline";

const PaperComponent = (props: PaperProps) => {
  return (
    <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
      <Paper {...props} />
    </Draggable>
  );
};

interface Props extends WithStyles<typeof styles> {
  onRefresh: () => void;
}

export default withStyles(styles, { withTheme: true })((props: Props) => {
  const {state, dispatch} = React.useContext(Context.store);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();
    const { name, value } = e.target;
    switch (name) {
      case "text":
        dispatch(Context.Actions.newQuery({
          before: state.timestamp,
          text: value,
          hashtags: /\S/.test(state.hashtags) ? state.hashtags.split(/\s+/) : [],
          page: state.page,
          size: 50,
        }));
        break;
      case "hashtags":
        dispatch(Context.Actions.newQuery({
          before: state.timestamp,
          text: state.text,
          hashtags: /\S/.test(value) ? value.split(/\s+/) : [],
          page: state.page,
          size: 50,
        }));
        break;
      default:
        break;
    }
  }

  const handleClose = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    dispatch(Context.Actions.close());
  }

  const handleSearch = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    props.onRefresh();
    dispatch(Context.Actions.refresh(
      state.text,
      state.hashtags,
      dispatch));
    dispatch(Context.Actions.close());
  }

  return (
    <Box>
      <Dialog
        open={state.dialog}
        onClose={handleClose}
        PaperComponent={PaperComponent}
        aria-labelledby="draggable-dialog-title"
      >
        <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
          Search with:
        </DialogTitle>
        <DialogContent>
          <form className={props.classes.form} noValidate>
            <Box mb={2} width={1}>
              <TextField
                id="text"
                name="text"
                label="Text"
                value={state.text}
                fullWidth={true}
                className={props.classes.textField}
                variant="outlined"
                onChange={handleChange}
              />
            </Box>
            <Box mb={1} width={1}>
              <TextField
                id="hashtags"
                name="hashtags"
                label="#Hashtag"
                value={state.hashtags}
                fullWidth={true}
                className={props.classes.textField}
                variant="outlined"
                onChange={handleChange}
              />
            </Box>
          </form>
        </DialogContent>
        <DialogActions>
          <Button autoFocus onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={handleSearch} color="primary">
            Search
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
});
