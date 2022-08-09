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
import * as Material from "@material-ui/core";
import Count from "../Count";
import Topic from "../Topic";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTwitter } from "@fortawesome/free-brands-svg-icons";
import styles from "./styles";

// eslint-disable-next-line @typescript-eslint/no-empty-interface
interface Props extends WithStyles<typeof styles> {}

const yesterdayOf = (date: Date): Date => {
  const d = new Date(date);
  d.setHours(date.getHours() - 24);
  return d;
};

const toString = (date: Date) =>
  date.toLocaleDateString(undefined, {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });

export default withStyles(styles, { withTheme: true })((props: Props) => {
  const date = new Date();
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
        Trend Chart ({toString(date)})
      </Material.Typography>
      <Material.Box className={props.classes.box}>
        <Material.Paper square className={props.classes.paper}>
          <Material.Grid container spacing={0}>
            <Material.Grid item xs={9}>
              <Count date={date} />
            </Material.Grid>
            <Material.Grid item xs={3}>
              <Material.Grid container spacing={0}>
                <Material.Grid item xs={12}>
                  <Topic date={date} />
                </Material.Grid>
                <Material.Grid item xs={12}>
                  <Topic date={yesterdayOf(date)} />
                </Material.Grid>
              </Material.Grid>
            </Material.Grid>
          </Material.Grid>
        </Material.Paper>
      </Material.Box>
    </>
  );
});
