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
import * as Context from "../../../../contexts/tweet/trend";

const oneHourBeforeOf = (date: Date): Date => {
  const d = new Date(date);
  d.setHours(date.getHours() - 1);
  return d;
};

function timestamp(str: string) {
  const date = new Date(str);
  return date.toLocaleTimeString(undefined, {
    hour: "2-digit",
    minute: "2-digit",
  });
}

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
  const [model, setModel] = React.useState<Context.Count.Model>(null);

  React.useEffect(() => {
    const query = {
      from: oneHourBeforeOf(props.date).getTime(),
      to: props.date.getTime(),
      interval: "1m",
    } as Context.Count.Query;

    const opts = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(query),
    };

    fetch(`${process.env.API_ENDPOINT}/tweet/counts`, opts)
      .then((res) => {
        if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
        return res.json();
      })
      .then((json) => {
        setModel({
          datasets: [
            {
              label: "count",
              data: json.map((e: Context.Count.Response) => {
                return {
                  x: timestamp(e.timestamp),
                  y: e.count,
                };
              }),
              borderColor: "rgb(  0,  71, 171)",
            },
          ],
        });
      })
      .catch((reason) => {
        console.log(reason);
      });
  }, []);

  const dummy = {
    datasets: [
      {
        label: "count",
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        data: [...Array(60).keys()].map((_) => {
          return {
            x: "",
            y: 0,
          };
        }),
      },
    ],
  } as Context.Topic.Model;

  const emptyCount = () => (
    <Material.Card className={props.classes.card}>
      <Material.CardHeader title={getDateString(props.date)} />
      <Material.Divider />
      <Material.CardContent>
        <Material.Typography variant="h6" className={props.classes.typography}>
          Not Enough Data
        </Material.Typography>
        <ReactChart.Line
          data={dummy}
          options={{
            responsive: true,
            plugins: {
              legend: {
                display: false,
              },
              title: {
                display: false,
              },
            },
          }}
        />
      </Material.CardContent>
    </Material.Card>
  );

  const count = () => (
    <Material.Card className={props.classes.card}>
      <Material.CardHeader title={getDateString(props.date)} />
      <Material.Divider />
      <Material.CardContent>
        <ReactChart.Line
          data={model}
          options={{
            responsive: true,
            plugins: {
              legend: {
                display: false,
              },
              title: {
                display: false,
              },
            },
          }}
        />
      </Material.CardContent>
    </Material.Card>
  );

  if (model?.datasets[0]?.data?.length < 30) return emptyCount();
  else return count();
});
