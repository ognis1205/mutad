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
import * as Context from "../../../../contexts/tweet/trend";

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

const getDateString = (date: Date) =>
  date.toLocaleDateString(undefined, {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });

interface Props extends WithStyles<typeof styles> {
  date: Date;
}

export default withStyles(styles)((props: Props) => {
  const [model, setModel] = React.useState<Context.Topic.Model>(null);

  React.useEffect(() => {
    const query = {
      from: beginOf(props.date).getTime(),
      to: endOf(props.date).getTime(),
      topN: 10,
    } as Context.Topic.Query;

    const opts = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(query),
    };

    fetch(`${process.env.API_ENDPOINT}/tweet/topics`, opts)
      .then((res) => {
        if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
        return res.json();
      })
      .then((json) => {
        setModel({
          labels: json.map((e: Context.Topic.Response) => "#" + e.name),
          datasets: [
            {
              label: "count",
              data: json.map((e: Context.Topic.Response) => e.count),
              backgroundColor: [
                "rgba(  0,  71, 171, 0.7)",
                "rgba( 17,  81, 171, 0.7)",
                "rgba( 34,  91, 171, 0.7)",
                "rgba( 51, 101, 171, 0.7)",
                "rgba( 68, 111, 171, 0.7)",
                "rgba( 85, 121, 171, 0.7)",
                "rgba(103, 131, 171, 0.7)",
                "rgba(120, 141, 171, 0.7)",
                "rgba(137, 151, 171, 0.7)",
                "rgba(154, 161, 171, 0.7)",
              ],
              borderColor: [
                "rgb(  0,  71, 171)",
                "rgb( 17,  81, 171)",
                "rgb( 34,  91, 171)",
                "rgb( 51, 101, 171)",
                "rgb( 68, 111, 171)",
                "rgb( 85, 121, 171)",
                "rgb(103, 131, 171)",
                "rgb(120, 141, 171)",
                "rgb(137, 151, 171)",
                "rgb(154, 161, 171)",
              ],
            },
          ],
        });
      })
      .catch((reason) => {
        console.log(reason);
      });
  }, []);

  const dummy = {
    labels: [...Array(10).keys()].map((n) => "#Hashtag" + n),
    datasets: [
      {
        label: "count",
        data: [...Array(10).keys()].map((_) => 0),
      },
    ],
  } as Context.Topic.Model;

  const emptyTopic = () => (
    <Material.Card className={props.classes.card}>
      <Material.CardHeader title={getDateString(props.date)} />
      <Material.Divider />
      <Material.CardContent>
        <Material.Typography variant="h6" className={props.classes.typography}>
          Not Enough Data
        </Material.Typography>
        <ReactChart.Bar
          data={dummy}
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
          }}
        />
      </Material.CardContent>
    </Material.Card>
  );

  const topic = () => (
    <Material.Card className={props.classes.card}>
      <Material.CardHeader title={getDateString(props.date)} />
      <Material.Divider />
      <Material.CardContent>
        <ReactChart.Bar
          data={model}
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
          }}
        />
      </Material.CardContent>
    </Material.Card>
  );

  if (model?.datasets[0]?.data?.length !== 10) return emptyTopic();
  else return topic();
});
