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
import { FC } from "react";
import Link from "next/link";
import {
  Button,
  Card,
  CardActionArea,
  CardActions,
  CardContent,
  CardMedia,
  Grid,
  Typography
} from "@material-ui/core";
import withStyles from "@material-ui/core/styles/withStyles";
import { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import { SubMenuDef } from "../../../specs";

interface Props extends WithStyles<typeof styles> {
  menu: SubMenuDef[];
}

interface ContentProps extends WithStyles<typeof styles> {
  def: SubMenuDef;
}

const DashboardContent: FC<ContentProps> = (props: ContentProps) => {
  return (
    <Card className={props.classes.card}>
      <Link href={props.def.link} passHref>
        <CardActionArea>
          <CardMedia
            className={props.classes.cardMedia}
            image={props.def.image}
            title={props.def.text}
          />
          <CardContent>
            <Typography gutterBottom variant="h5" component="h2">
              {props.def.text}
            </Typography>
            <Typography variant="body2" color="textSecondary" component="p">
              {props.def.description}
            </Typography>
          </CardContent>
        </CardActionArea>
      </Link>
      <CardActions>
        <Button size="small" color="primary">
          Reference
        </Button>
      </CardActions>
    </Card>
  );
};

const StyledDashboardContent = withStyles(styles, { withTheme: true })(DashboardContent);

const Dashboard = (props: Props) => (
  <Grid container className={props.classes.grid} spacing={2}>
    {props.menu.map((def: SubMenuDef, index: number) => (
      <Grid item xs={4} key={index}>
        <StyledDashboardContent
          def={def}
        />
      </Grid>
    ))}
  </Grid>
  );

export default withStyles(styles, { withTheme: true })(Dashboard);
