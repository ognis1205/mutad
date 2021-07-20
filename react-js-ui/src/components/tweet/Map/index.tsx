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
import React, { useState, useEffect } from "react";
import { useTheme, makeStyles, Theme } from "@material-ui/core/styles";
import { Container } from "@material-ui/core";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import { MapContainer, MapContainerProps, TileLayer } from "react-leaflet";
import { NextPage } from "next";
import { styles } from "./styles";

const useStyle = makeStyles({
  root: (theme: Theme) => ({
    minWidth: "100%",
    minHeight: "87vh",
    paddingLeft: "0px",
    paddingRight: "0px",
  }),
});

interface Props extends WithStyles<typeof styles>, MapContainerProps {}

const Index: NextPage = (props: Props) => {
  const classes = useStyle(useTheme());

  const [isBrowser, setIsBrowser] = useState(false);

  useEffect(() => {
    setIsBrowser(true);
  }, []);

  if (!isBrowser) return null;
  else
    return (
      <Container className={classes.root}>
        <MapContainer className={props.classes.leafletContainer} {...props}>
          <TileLayer
            attribution="&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors"
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
        </MapContainer>
      </Container>
    );
};

export default withStyles(styles)(Index);
