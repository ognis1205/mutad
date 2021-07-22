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
import React, { FC, useEffect, useState } from "react";
import { useSelector } from 'react-redux';
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import { MapContainerProps } from "react-leaflet";
import { styles } from "./styles";
import { getGeos } from "../../../state/ducks/geos/selectors";

interface Props extends WithStyles<typeof styles>, MapContainerProps {}

const Map: FC<Props> = (props: Props) => {
  const geos = useSelector(getGeos);

  const [map, setMap] = useState<LeafletMap>();

  useEffect(() => {
    const leafletMap = L.map("map", { ...props }).setView(props.center, props.zoom);
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    }).addTo(leafletMap);
    setMap(leafletMap);
  }, []);

  useEffect(() => {
    if (map) {
      const addressPoints = [
        [-37.8839, null, "571"],
      ];
      const points = geos
                   ? [...geos].map((p) => {
                     let lonLat = p.toArray();
                     return [...p];
                   })
                   : [];
      console.log(points);
      L.heatLayer(points).addTo(map);
    }
  }, [geos]);
  
  return <div id="map" className={props.classes.leafletContainer}></div>;
};

export default withStyles(styles)(Map);
