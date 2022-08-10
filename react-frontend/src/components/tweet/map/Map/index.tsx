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
import * as Leaflet from "leaflet";
import "leaflet.heat";
import "leaflet/dist/leaflet.css";
import * as ReactLeaflet from "react-leaflet";
import * as Material from "@material-ui/core";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTwitter } from "@fortawesome/free-brands-svg-icons";
import * as Store from "../../../../redux/store";
import styles from "./styles";

interface Props
  extends WithStyles<typeof styles>,
    ReactLeaflet.MapContainerProps {}

export default withStyles(styles)((props: Props) => {
  const mapStore = ReactRedux.useSelector((store: Store.Type) => store.map);

  const [map, setMap] = React.useState<Leaflet.Map>();

  const [heat, setHeat] = React.useState<Leaflet.Layer>();

  React.useEffect(() => {
    const leafletMap = Leaflet.map("map", {
      ...props,
      zoomControl: false,
    }).setView(props.center, props.zoom);
    Leaflet.control.zoom({ position: "bottomleft" }).addTo(leafletMap);
    Leaflet.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution:
        '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(leafletMap);
    setMap(leafletMap);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  React.useEffect(() => {
    if (map) {
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      const layer = (Leaflet as any).heatLayer(mapStore.points, {
        radius: mapStore.radius,
        blur: mapStore.blur,
        maxZoom: mapStore.zoom,
      });
      if (heat) map.removeLayer(heat);
      map.addLayer(layer);
      setHeat(layer);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [mapStore.points, mapStore.radius, mapStore.blur, mapStore.zoom]);

  return (
    <>
      <Material.Typography
        className={props.classes.text}
        variant="h5"
        gutterBottom
      >
        <Material.Box
          className={props.classes.icon}
          component="span"
          sx={{ display: "inline" }}
        >
          <FontAwesomeIcon icon={faTwitter} />
        </Material.Box>
        Heat Map
      </Material.Typography>
      <Material.Box id="map" className={props.classes.box}></Material.Box>
    </>
  );
});
