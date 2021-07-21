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
import { NextPage } from "next";
import Grid from '@material-ui/core/Grid';
import Specs from "../specs";
import Links from "../components/app/Links";

const useStyle = makeStyles({
  root: (theme: Theme) => ({
    minWidth: "100%",
    minHeight: "87vh",
    paddingTop: "5vh",
    paddingLeft: "5vw",
    paddingRight: "0px",
  }),
  control: (theme: Theme) => ({
    padding: theme.spacing(2),
  }),
});

const Index: NextPage = () => {
  const classes = useStyle(useTheme());

  const menus = [].concat(...Specs.menus.map((menu, index) => {
    return menu.hasOwnProperty("subMenus") && Array.isArray(menu.subMenus) ? menu.subMenus : [];
  }));

  return (
    <Grid container className={classes.root} justifyContent="flex-start" spacing={2}>
      <Links menus={menus} />
    </Grid>
  );
};

export default Index;
