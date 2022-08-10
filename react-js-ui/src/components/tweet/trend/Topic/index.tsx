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
import * as ReactChart from "react-chartjs-2";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";

type Model = {
  labels: string[];
  datasets: Histogram[];
};

type Histogram = {
  label: string;
  backgroundColor: string[];
  borderColor: string[];
  data: number[];
};

type Query = {
  from: number;
  to: number;
  topN: number;
};

type Response = {
  name: string;
  count: number;
};

const beginOf = (date: Date): Date => {
  const d = new Date(date);
  d.setHours(0, 0, 0, 0);
  return d;
};

const endOf = (date: Date): Date => {
  const d = new Date(date);
  d.setHours(24, 0, 0, 0);
  return d;
};

interface Props extends WithStyles<typeof styles> {
  date: Date;
}

export default withStyles(styles)((props: Props) => {
  const [model, setModel] = React.useState<Model>(null);

  React.useEffect(() => {
    fetch(`${process.env.API_ENDPOINT}/tweet/topics`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        from: beginOf(props.date).getTime(),
        to: endOf(props.date).getTime(),
        topN: 10,
      } as Query),
    })
      .then((res) => {
        if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
        return res.json();
      })
      .then((json) => {
        setModel({
          labels: json.map((res: Response) => "#" + res.name),
          datasets: [
            {
              label: "count",
              data: json.map((res: Response) => res.count),
              backgroundColor: [
                "rgba(0,101, 255, 1)",
                "rgba(25, 117, 255, 1)",
                "rgba(50, 132, 255, 1)",
                "rgba(76, 147, 255, 1)",
                "rgba(102, 163, 255, 1)",
                "rgba(127, 178, 255, 1)",
                "rgba(153, 193, 255, 1)",
                "rgba(178, 209, 255, 1)",
                "rgba(204, 224, 255, 1)",
                "rgba(229, 239, 255, 1)",
              ],
              borderColor: [
                "rgba(0,101, 255, 1)",
                "rgba(25, 117, 255, 1)",
                "rgba(50, 132, 255, 1)",
                "rgba(76, 147, 255, 1)",
                "rgba(102, 163, 255, 1)",
                "rgba(127, 178, 255, 1)",
                "rgba(153, 193, 255, 1)",
                "rgba(178, 209, 255, 1)",
                "rgba(204, 224, 255, 1)",
                "rgba(229, 239, 255, 1)",
              ],
            },
          ],
        });
      })
      .catch((reason) => {
        console.log(reason);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const dummy = {
    labels: [...Array(10).keys()].map((n) => "#Hashtag" + n),
    datasets: [
      {
        label: "count",
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        data: [...Array(10).keys()].map((_) => 0),
      },
    ],
  } as Model;

  const emptyTopic = () => (
    <Material.Box className={props.classes.box}>
      <Material.Typography variant="h6" className={props.classes.typography}>
        Not Enough Data
      </Material.Typography>
      <ReactChart.Bar
        data={dummy}
        options={{
          indexAxis: "y",
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
            title: {
              display: false,
              text: "Hashtags [#]",
            },
          },
          scales: {
            x: {
              display: false,
              grid: {
                display: false,
              },
            },
            y: {
              display: false,
              grid: {
                display: false,
              },
            },
          },
        }}
      />
    </Material.Box>
  );

  const topic = () => (
    <Material.Box className={props.classes.box}>
      <ReactChart.Bar
        data={model}
        options={{
          indexAxis: "y",
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
            title: {
              display: false,
              text: "Hashtags [#]",
            },
          },
          scales: {
            x: {
              display: false,
              grid: {
                display: false,
              },
            },
            y: {
              display: model ? true : false,
              grid: {
                display: false,
              },
            },
          },
        }}
      />
    </Material.Box>
  );

  if (model?.datasets[0]?.data?.length < 1) return emptyTopic();
  else return topic();
});
