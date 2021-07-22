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
import React, { FC, useState } from "react";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import clsx from 'clsx';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import Box from '@material-ui/core/Box';
import IconButton from '@material-ui/core/IconButton';
import TextField from '@material-ui/core/TextField';
import LocationSearchingIcon from '@material-ui/icons/LocationSearching';
import { styles } from "./styles";

interface Props extends WithStyles<typeof styles> {}

const MapMenu: FC<Props> = (props: Props) => {
  const [expanded, setExpanded] = useState(false);

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  return (
    <Card
      className={props.classes.menu}>
      <CardActions className={props.classes.icon} disableSpacing>
        <IconButton
          className={clsx(props.classes.expand, {
            [props.classes.expandOpen]: expanded,
          })}
          onClick={handleExpandClick}
          aria-expanded={expanded}
          aria-label="show more"
        >
          <LocationSearchingIcon />
        </IconButton>
      </CardActions>
      <Collapse in={expanded} timeout="auto" unmountOnExit>
        <CardContent>
          <form className={props.classes.search} noValidate>
            <Box mb={2} width={1}>
              <TextField
                id="from"
                label="From"
                type="date"
                defaultValue={new Date().toISOString().slice(0, 10)}
                className={props.classes.textField}
                fullWidth={true}
                InputLabelProps={{
                  shrink: true,
                }}
              />
            </Box>
            <Box mb={2} width={1}>
              <TextField
                id="to"
                label="To"
                type="date"
                defaultValue={new Date().toISOString().slice(0, 10)}
                fullWidth={true}
                className={props.classes.textField}
                InputLabelProps={{
                  shrink: true,
                }}
              />
            </Box>
            <Box mb={2} width={1}>
              <TextField
                id="text"
                label="Text"
                fullWidth={true}
                className={props.classes.textField}
                variant="outlined"
              />
            </Box>
            <Box width={1}>
              <TextField
                id="hashtag"
                label="#Hashtag"
                fullWidth={true}
                className={props.classes.textField}
                variant="outlined"
              />
            </Box>
          </form>
        </CardContent>
      </Collapse>
    </Card>
  );
};

export default withStyles(styles)(MapMenu);
