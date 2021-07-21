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
import dynamic from "next/dynamic";
import { useTheme, makeStyles, Theme } from "@material-ui/core/styles";
import { Container } from "@material-ui/core";
import { NextPage } from "next";

const useStyle = makeStyles({
  root: (theme: Theme) => ({
    minWidth: "100%",
    minHeight: "87vh",
    paddingLeft: "0px",
    paddingRight: "0px",
  }),
});

const Index: NextPage = () => {
  const classes = useStyle(useTheme());

  const Map = React.useMemo(
    () =>
      dynamic(() => import("../../../components/tweet/Map"), {
        loading: () => <p>Loading a map...</p>,
        ssr: false,
      }),
    []
  );

  return (
    <Container className={classes.root}>
      <Map
        center={[0.0, 0.0]}
        zoom={3}
        minZoom={3}
        maxBounds={[
          [-90, -180],
          [90, 180],
        ]}
      />
    </Container>
  );
};

export default Index;
