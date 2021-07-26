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
import { useDispatch } from 'react-redux';
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import clsx from 'clsx';
import Button from "@material-ui/core/Button";
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import Box from '@material-ui/core/Box';
import IconButton from '@material-ui/core/IconButton';
import TextField from '@material-ui/core/TextField';
import LocationSearchingIcon from '@material-ui/icons/LocationSearching';
import { styles } from "./styles";
import { reqGeoPoints } from "../../../state/ducks/geos/actions";
import { GeoQuery } from "../../../state/ducks/geos/geo.d";

interface Props extends WithStyles<typeof styles> {}

const MapMenu: FC<Props> = (props: Props) => {
  const dispatch = useDispatch();

  const [expanded, setExpanded] = useState(false);

  const [inputs, setInputs] = useState<GeoQuery>({
    "from": (new Date()).getTime(),
    "to": (new Date()).getTime(),
    "text": "",
    "hashtags": [],
  });

  const handleExpand = () => {
    setExpanded(!expanded);
  };

  const handlePost = async (e: React.MouseEvent<HTMLElement>) => {
    e.preventDefault()
    dispatch(reqGeoPoints({
      from: inputs?.from,
      to: inputs?.to,
      text: inputs?.text,
      hashtags: inputs?.hashtags,
    }));
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    switch (name) {
      case "from":
        setInputs(prev => ({ ...prev, [name]: (new Date(value)).getTime() }));
        break;
      case "to":
        setInputs(prev => ({ ...prev, [name]: (new Date(value)).getTime() }));
        break;
      case "text":
        setInputs(prev => ({ ...prev, [name]: value }));
        break;
      case "hashtags":
        setInputs(prev => ({ ...prev, [name]: value.split(/\s+/) }));
        break;
      default:
        break;
    }
  }

  return (
    <Card
      className={props.classes.menu}>
      <CardActions className={props.classes.icon} disableSpacing>
        <IconButton
          className={clsx(props.classes.expand, {
            [props.classes.expandOpen]: expanded,
          })}
          onClick={handleExpand}
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
                name="from"
                label="From"
                type="date"
                defaultValue={new Date().toISOString().slice(0, 10)}
                className={props.classes.textField}
                fullWidth={true}
                InputLabelProps={{
                  shrink: true,
                }}
                onChange={handleChange}
              />
            </Box>
            <Box mb={2} width={1}>
              <TextField
                id="to"
                name="to"
                label="To"
                type="date"
                defaultValue={new Date().toISOString().slice(0, 10)}
                fullWidth={true}
                className={props.classes.textField}
                InputLabelProps={{
                  shrink: true,
                }}
                onChange={handleChange}
              />
            </Box>
            <Box mb={2} width={1}>
              <TextField
                id="text"
                name="text"
                label="Text"
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
                fullWidth={true}
                className={props.classes.textField}
                variant="outlined"
                onChange={handleChange}
              />
            </Box>
            <Box width={1}>
              <Button
                id="search"
                label="Search"
                color="primary"
                className={props.classes.textField}
                onClick={handlePost}
              >
                Search
              </Button>
            </Box>
          </form>
        </CardContent>
      </Collapse>
    </Card>
  );
};

export default withStyles(styles)(MapMenu);
