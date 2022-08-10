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
  datasets: Line[];
};

type Line = {
  label: string;
  borderColor: string;
  data: Marker[];
};

type Marker = {
  x: string;
  y: number;
};

type Query = {
  from: number;
  to: number;
  interval: string;
};

type Response = {
  timestamp: string;
  count: number;
};

const hoursBeforeOf = (date: Date, hours: number): Date =>
  new Date(date.getTime() - hours * 60 * 60 * 1000);

const timestamp = (str: string): string => {
  const date = new Date(str);
  return date.toLocaleTimeString(undefined, {
    hour: "2-digit",
    minute: "2-digit",
  });
};

interface Props extends WithStyles<typeof styles> {
  date: Date;
}

export default withStyles(styles)((props: Props) => {
  const [model, setModel] = React.useState<Model>(null);

  React.useEffect(() => {
    fetch(`${process.env.API_ENDPOINT}/tweet/counts`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        from: hoursBeforeOf(props.date, 1).getTime(),
        to: props.date.getTime(),
        interval: "1m",
      } as Query),
    })
      .then((res) => {
        if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
        return res.json();
      })
      .then((json) => {
        const count = json.map((res: Response) => {
          return {
            x: timestamp(res.timestamp),
            y: res.count,
          };
        });
        setModel({
          datasets: [
            {
              label: "count",
              data: count.slice(1, -1),
              borderColor: "rgba(254,132,132,1)",
            },
            {
              label: "average",
              data: (function () {
                const average = [];
                for (let i = 1; i < count.length - 1; i++)
                  average.push({
                    x: count[i]["x"],
                    y: Math.floor(
                      (count[i + 1]["y"] + count[i]["y"] + count[i - 1]["y"]) /
                        3
                    ),
                  });
                return average;
              })(),
              // @ts-ignore
              fill: true,
              backgroundColor: "rgba(75,192,192,0.2)",
              borderColor: "rgba(75,192,192,1)",
              tension: 0.4,
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
    datasets: [
      {
        label: "count",
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        data: [...Array(60).keys()].map((k) => {
          return {
            x: "",
            y: 0,
          };
        }),
      },
    ],
  } as Model;

  const emptyCount = () => (
    <Material.Box className={props.classes.box}>
      <Material.Typography variant="h6" className={props.classes.typography}>
        Not Enough Data
      </Material.Typography>
      <ReactChart.Line
        data={dummy}
        options={{
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
            title: {
              display: false,
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
              min: 0,
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

  const count = () => (
    <Material.Box className={props.classes.box}>
      <ReactChart.Line
        data={model}
        options={{
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
            title: {
              display: false,
            },
          },
          scales: {
            x: {
              display: model ? true : false,
              grid: {
                display: false,
              },
            },
            y: {
              min: 0,
              max: model
                ? Math.max(...model.datasets[0].data.map((m) => m["y"])) * 1.5
                : 0,
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

  if (model?.datasets[0]?.data?.length < 1) return emptyCount();
  else return count();
});
