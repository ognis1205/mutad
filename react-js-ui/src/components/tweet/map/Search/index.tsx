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
import * as Material from "@material-ui/core";
import LocationSearchingIcon from "@material-ui/icons/LocationSearching";
import clsx from "clsx";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import * as Context from "../../../../contexts/tweet/map";

interface Props extends WithStyles<typeof styles> {}

export default withStyles(styles)((props: Props) => {
  const {state, dispatch} = React.useContext(Context.store);

  const [expanded, setExpanded] = React.useState<boolean>(false);

  React.useEffect(() => {
    dispatch(Context.Actions.newQuery({
      text: "",
      hashtags: [],
      from: (new Date()).getTime(),
      to: (new Date()).getTime(),
    }));
  }, []);

  const handleExpand = () => {
    setExpanded(!expanded);
  };

  const handlePost = async (e: React.MouseEvent<HTMLElement>) => {
    e.preventDefault();
    dispatch(Context.Actions.request(
      state.from,
      state.to,
      state.text,
      state.hashtags,
      dispatch));
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();
    const { name, value } = e.target;
    switch (name) {
      case "from":
        dispatch(Context.Actions.newQuery({
          text: state.text,
          hashtags: /\S/.test(state.hashtags) ? state.hashtags.split(/\s+/) : [],
          from: (new Date(value)).getTime(),
          to: state.to,
        }));
        break;
      case "to":
        dispatch(Context.Actions.newQuery({
          text: state.text,
          hashtags: /\S/.test(state.hashtags) ? state.hashtags.split(/\s+/) : [],
          from: state.from,
          to: (new Date(value)).getTime(),
        }));
        break;
      case "text":
        dispatch(Context.Actions.newQuery({
          text: value,
          hashtags: /\S/.test(state.hashtags) ? state.hashtags.split(/\s+/) : [],
          from: state.from,
          to: state.to,
        }));
        break;
      case "hashtags":
        dispatch(Context.Actions.newQuery({
          text: value,
          hashtags: /\S/.test(value) ? value.split(/\s+/) : [],
          from: state.from,
          to: state.to,
        }));
        break;
      default:
        break;
    }
  }

  const handleSlide = (e: React.ChangeEvent<HTMLSpanElement>, value: number | number[]) => {
    e.preventDefault();
    const {id} = e.target.parentElement;
    switch (id) {
      case "radius":
        if (typeof value === "number")
          dispatch(Context.Actions.newConfig({
            radius: value,
            blur: state.blur,
            zoom: state.zoom,
          }));
        break;
      case "blur":
        if (typeof value === "number")
          dispatch(Context.Actions.newConfig({
            radius: state.radius,
            blur: value,
            zoom: state.zoom,
          }));
        break;
      case "maxZoom":
        if (typeof value === "number")
          dispatch(Context.Actions.newConfig({
            radius: state.radius,
            blur: state.blur,
            zoom: value,
          }));
        break;
      default:
        break;
    }
  }

  const valuetext = (value: number) => {
    return `${value}`;
  };

  return (
    <Material.Card
      className={props.classes.card}>
      <Material.CardActions className={props.classes.cardActions} disableSpacing>
        <Material.IconButton
          className={clsx(props.classes.iconButton, {
            [props.classes.iconButtonOpen]: expanded,
          })}
          onClick={handleExpand}
          aria-expanded={expanded}
          aria-label="show more"
        >
          <LocationSearchingIcon />
        </Material.IconButton>
      </Material.CardActions>
      <Material.Collapse in={expanded} timeout="auto" unmountOnExit>
        <Material.CardContent>
          <form className={props.classes.form} noValidate>
            <Material.Box mb={2} width={1}>
              <Material.TextField
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
            </Material.Box>
            <Material.Box mb={2} width={1}>
              <Material.TextField
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
            </Material.Box>
            <Material.Box mb={2} width={1}>
              <Material.TextField
                id="text"
                name="text"
                label="Text"
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
                fullWidth={true}
                className={props.classes.textField}
                variant="outlined"
                onChange={handleChange}
              />
            </Material.Box>
            <Material.Box mb={1} width={1}>
              <Material.Typography variant="caption" align="center" gutterBottom>
                Radius
              </Material.Typography>
              <Material.Slider
                id="radius"
                value={state.radius}
                getAriaValueText={valuetext}
                aria-labelledby="discrete-slider"
                valueLabelDisplay="auto"
                step={1}
                marks
                min={1}
                max={20}
                onChange={handleSlide}
              />
            </Material.Box>
            <Material.Box mb={1} width={1}>
              <Material.Typography variant="caption" align="center" gutterBottom>
                Blur
              </Material.Typography>
              <Material.Slider
                id="blur"
                value={state.blur}
                getAriaValueText={valuetext}
                aria-labelledby="discrete-slider"
                valueLabelDisplay="auto"
                step={1}
                marks
                min={1}
                max={20}
                onChange={handleSlide}
              />
            </Material.Box>
            <Material.Box mb={1} width={1}>
              <Material.Typography variant="caption" align="center" gutterBottom>
                Max Zoom
              </Material.Typography>
              <Material.Slider
                id="maxZoom"
                value={state.zoom}
                getAriaValueText={valuetext}
                aria-labelledby="discrete-slider"
                valueLabelDisplay="auto"
                step={1}
                marks
                min={2}
                max={15}
                onChange={handleSlide}
              />
            </Material.Box>
            <Material.Box width={1}>
              <Material.Button
                id="search"
                label="Search"
                color="primary"
                className={props.classes.textField}
                onClick={handlePost}
              >
                Search
              </Material.Button>
            </Material.Box>
          </form>
        </Material.CardContent>
      </Material.Collapse>
    </Material.Card>
  );
});
