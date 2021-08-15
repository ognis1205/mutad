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
import * as ReactChart from "react-chartjs-2";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";

interface Props extends WithStyles<typeof styles> {}

export default withStyles(styles)((props: Props) => {
  const data = {
    labels: [...Array(10).keys()].map((n) => "#Hashtag" + n),
    datasets: [{
      label: "count",
      data: [...Array(10).keys()].map((_) => 0),
    }],
  };

  return (
    <Material.Card className={props.classes.box}>
      <Material.CardHeader title="Trended Hashtags"/>
      <Material.Divider/>
      <Material.CardContent>
      <Material.Typography variant="h6" className={props.classes.typography}>
        Not Enough Data
      </Material.Typography>
      <ReactChart.Bar 
        data={data} 
        options={{
          indexAxis: "y",
          responsive: true,
          plugins: {
            legend: {
              display: false,
            },
            title: {
              display: false,
              text: "Hashtags [#]",
            },
          },
        }}/>
      </Material.CardContent>
    </Material.Card>
  );
});
