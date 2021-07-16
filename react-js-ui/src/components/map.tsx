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
import React from "react";
import { LatLngExpression        } from "leaflet";
import { MapContainer, TileLayer } from "react-leaflet";


export default function Map(): JSX.Element {
  const position: LatLngExpression = [0.0, 0.0];
  const zoom: number = 15;
  return (
    <MapContainer center={position} zoom={zoom} scrollWheelZoom={false}>
      <TileLayer
        attribution="&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors"
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      { /*Placeholder, we'll put our markers here*/ }
    </MapContainer>
  )
};
