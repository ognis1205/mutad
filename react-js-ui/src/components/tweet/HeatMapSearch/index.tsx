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
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import clsx from "clsx";
import {
  Box,
  Button,
  Card,
  CardContent,
  CardActions,
  Collapse,
  IconButton,
  Slider,
  TextField,
  Typography,
} from "@material-ui/core";
import LocationSearchingIcon from "@material-ui/icons/LocationSearching";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import {
  Actions,
  Selectors,
  Types
} from "../../../state/ducks/geos";

interface Props extends WithStyles<typeof styles> {}

export default withStyles(styles)((props: Props) => {
  const dispatch = useDispatch();

  const [expanded, setExpanded] = useState<boolean>(false);

  const [inputs, setInputs] = useState<Types.GeoQuery>({
    "from": (new Date()).getTime(),
    "to": (new Date()).getTime(),
    "text": "",
    "hashtags": [],
  });

  const r = useSelector(Selectors.getGeoRadius);

  const b = useSelector(Selectors.getGeoBlur);

  const z = useSelector(Selectors.getGeoZoom);

  const handleExpand = () => {
    setExpanded(!expanded);
  };

  const handlePost = async (e: React.MouseEvent<HTMLElement>) => {
    e.preventDefault();
    dispatch(Actions.reqGeoPoints({
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

  const handleSlide = (e: React.ChangeEvent<HTMLSpanElement>, v: number | number[]) => {
    const { id } = e.target.parentElement;
    switch (id) {
      case "radius":
        if (typeof v === "number") dispatch(Actions.newGeoRadius(v));
        break;
      case "blur":
        if (typeof v === "number") dispatch(Actions.newGeoBlur(v));
        break;
      case "maxZoom":
        if (typeof v === "number") dispatch(Actions.newGeoZoom(v));
        break;
      default:
        break;
    }
  }

  const valuetext = (value: number) => {
    return `${value}`;
  };

  return (
    <Card
      className={props.classes.card}>
      <CardActions className={props.classes.cardActions} disableSpacing>
        <IconButton
          className={clsx(props.classes.iconButton, {
            [props.classes.iconButtonOpen]: expanded,
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
          <form className={props.classes.form} noValidate>
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
            <Box mb={1} width={1}>
              <Typography variant="caption" align="center" gutterBottom>
                Radius
              </Typography>
              <Slider
                id="radius"
                value={r}
                getAriaValueText={valuetext}
                aria-labelledby="discrete-slider"
                valueLabelDisplay="auto"
                step={1}
                marks
                min={1}
                max={20}
                onChange={handleSlide}
              />
            </Box>
            <Box mb={1} width={1}>
              <Typography variant="caption" align="center" gutterBottom>
                Blur
              </Typography>
              <Slider
                id="blur"
                value={b}
                getAriaValueText={valuetext}
                aria-labelledby="discrete-slider"
                valueLabelDisplay="auto"
                step={1}
                marks
                min={1}
                max={20}
                onChange={handleSlide}
              />
            </Box>
            <Box mb={1} width={1}>
              <Typography variant="caption" align="center" gutterBottom>
                Max Zoom
              </Typography>
              <Slider
                id="maxZoom"
                value={z}
                getAriaValueText={valuetext}
                aria-labelledby="discrete-slider"
                valueLabelDisplay="auto"
                step={1}
                marks
                min={2}
                max={15}
                onChange={handleSlide}
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
});
