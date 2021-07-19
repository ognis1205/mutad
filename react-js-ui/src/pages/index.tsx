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
import { useTheme, makeStyles, Theme } from "@material-ui/core/styles";
import { Card, Grid, Typography } from "@material-ui/core";
import { NextPage } from "next";

const useStyle = makeStyles({
  root: (theme: Theme) => ({
    minWidth: "100%",
    minHeight: "100vh",
    display: "flex",
    flexDirection: "column",
  }),
});

const Index: NextPage = () => {
  const classes = useStyle(useTheme());
  return (
    <Grid
      container
      className={classes.root}
      spacing={0}
      alignItems="center"
      justifyContent="center"
    >
      <Grid item>
        <Typography variant="h2" align="center" gutterBottom={true}>
          Mutad|datuM
        </Typography>
      </Grid>
    </Grid>
  );
};

export default Index;
