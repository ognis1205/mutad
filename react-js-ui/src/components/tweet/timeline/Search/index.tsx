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
import * as React from "react";
import * as ReactRedux from "react-redux";
import * as Material from "@material-ui/core";
import Draggable from "react-draggable";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import * as TimelineModule from "../../../../redux/modules/timeline";
import * as Store from "../../../../redux/store";
import styles from "./styles";

const PaperComponent = (props: Material.PaperProps) => {
  return (
    <Draggable
      handle="#draggable-dialog-title"
      cancel={'[class*="MuiDialogContent-root"]'}
    >
      <Material.Paper {...props} />
    </Draggable>
  );
};

interface Props extends WithStyles<typeof styles> {
  onRefresh: () => void;
}

export default withStyles(styles, { withTheme: true })((props: Props) => {
  const timelineStore = ReactRedux.useSelector(
    (store: Store.Type) => store.timeline
  );

  const dispatch = ReactRedux.useDispatch();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();
    const { name, value } = e.target;
    switch (name) {
      case "text":
        dispatch(
          TimelineModule.newQuery({
            before: timelineStore.timestamp,
            text: value,
            hashtags: /\S/.test(timelineStore.hashtags)
              ? timelineStore.hashtags.split(/\s+/)
              : [],
            page: timelineStore.page,
            size: 50,
          })
        );
        break;
      case "hashtags":
        dispatch(
          TimelineModule.newQuery({
            before: timelineStore.timestamp,
            text: timelineStore.text,
            hashtags: /\S/.test(value) ? value.split(/\s+/) : [],
            page: timelineStore.page,
            size: 50,
          })
        );
        break;
      default:
        break;
    }
  };

  const handleClose = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    dispatch(TimelineModule.close());
  };

  const handleSearch = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    props.onRefresh();
    dispatch(
      TimelineModule.request(
        new Date().getTime(),
        timelineStore.text,
        timelineStore.hashtags,
        0,
        50
      )
    );
    dispatch(TimelineModule.close());
  };

  return (
    <Material.Box>
      <Material.Dialog
        open={timelineStore.dialog}
        onClose={handleClose}
        PaperComponent={PaperComponent}
        aria-labelledby="draggable-dialog-title"
      >
        <Material.DialogTitle
          style={{ cursor: "move" }}
          id="draggable-dialog-title"
        >
          Search with:
        </Material.DialogTitle>
        <Material.DialogContent>
          <form className={props.classes.form} noValidate>
            <Material.Box mb={2} width={1}>
              <Material.TextField
                id="text"
                name="text"
                label="Text"
                value={timelineStore.text}
                fullWidth={true}
                className={props.classes.textField}
                variant="outlined"
                onChange={handleChange}
              />
            </Material.Box>
            <Material.Box mb={1} width={1}>
              <Material.TextField
                id="hashtags"
                name="hashtags"
                label="#Hashtag"
                value={timelineStore.hashtags}
                fullWidth={true}
                className={props.classes.textField}
                variant="outlined"
                onChange={handleChange}
              />
            </Material.Box>
          </form>
        </Material.DialogContent>
        <Material.DialogActions>
          <Material.Button autoFocus onClick={handleClose} color="primary">
            Cancel
          </Material.Button>
          <Material.Button onClick={handleSearch} color="primary">
            Search
          </Material.Button>
        </Material.DialogActions>
      </Material.Dialog>
    </Material.Box>
  );
});
