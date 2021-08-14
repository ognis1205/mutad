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

interface Props extends WithStyles<typeof styles> {
  options: any,
}

export default withStyles(styles)((props: Props) => {
  const {state, dispatch} = React.useContext(Context.store);

  React.useEffect(() => {
    dispatch(Context.Actions.init(state.topN, dispatch));
  }, []);

  return (
    <Material.Box className={props.classes.box}>
      <ReactChart.Bar data={state.topic} options={props.options}/>
    </Material.Box>
  );
});
