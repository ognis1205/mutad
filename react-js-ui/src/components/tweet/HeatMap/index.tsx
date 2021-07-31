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
import "leaflet/dist/leaflet.css";
import L, { Map as LeafletMap } from "leaflet";
import "leaflet.heat";
import React, { useEffect, useState } from "react";
import { MapContainerProps } from "react-leaflet";
import { useSelector } from "react-redux";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import { Selectors } from "../../../state/ducks/geos";

interface Props extends WithStyles<typeof styles>, MapContainerProps {}

export default withStyles(styles)((props: Props) => {
  const geos = useSelector(Selectors.getGeos);

  const r = useSelector(Selectors.getGeoRadius);

  const b = useSelector(Selectors.getGeoBlur);

  const z = useSelector(Selectors.getGeoZoom);

  const [map, setMap] = useState<LeafletMap>();

  const [heat, setHeat] = useState<L.Layer>();

  useEffect(() => {
    const leafletMap = L.map("map", { ...props }).setView(props.center, props.zoom);
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(leafletMap);
    setMap(leafletMap);
  }, []);

  useEffect(() => {
    if (map) {
      const layer = L.heatLayer(geos, {
        radius: r,
        blur: b,
        maxZoom: z,
      });
      if (heat) map.removeLayer(heat);
      map.addLayer(layer);
      setHeat(layer);
    }
  }, [geos, r, b, z]);
  
  return <div id="map" className={props.classes.leaflet}></div>;
});
