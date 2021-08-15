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
import Count from "../Count";
import Topic from "../Topic";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";

interface Props extends WithStyles<typeof styles> {}

const yesterdayOf = (date: Date): Date => {
  const d = new Date(date);
  d.setHours(date.getHours() - 24);
  return d;
};

export default withStyles(styles, { withTheme: true })((props: Props) => {
  const date = new Date();

  return (
    <Material.Box className={props.classes.box}>
      <Material.Paper square className={props.classes.paper}>
        <Material.Grid container spacing={1}>
          <Material.Grid item xs={12}>
            <Count date={date} />
          </Material.Grid>
          <Material.Grid item xs={6}>
            <Topic date={date} />
          </Material.Grid>
          <Material.Grid item xs={6}>
            <Topic date={yesterdayOf(date)} />
          </Material.Grid>
        </Material.Grid>
      </Material.Paper>
    </Material.Box>
  );
});
