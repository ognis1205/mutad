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
import * as Leaflet from "leaflet";
import "leaflet.heat";
import "leaflet/dist/leaflet.css";
import * as ReactLeaflet from "react-leaflet";
import * as Material from "@material-ui/core";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import * as Context from "../../../../contexts/tweet/map";

interface Props
  extends WithStyles<typeof styles>,
    ReactLeaflet.MapContainerProps {}

export default withStyles(styles)((props: Props) => {
  const { state } = React.useContext(Context.store);

  const [map, setMap] = React.useState<Leaflet.Map>();

  const [heat, setHeat] = React.useState<L.Layer>();

  React.useEffect(() => {
    const leafletMap = Leaflet.map("map", { ...props }).setView(
      props.center,
      props.zoom
    );
    Leaflet.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(leafletMap);
    setMap(leafletMap);
  }, []);

  React.useEffect(() => {
    if (map) {
      const layer = Leaflet.heatLayer(state.points, {
        radius: state.radius,
        blur: state.blur,
        maxZoom: state.zoom,
      });
      if (heat) map.removeLayer(heat);
      map.addLayer(layer);
      setHeat(layer);
    }
  }, [state.points, state.radius, state.blur, state.zoom]);

  return <Material.Box id="map" className={props.classes.box}></Material.Box>;
});
