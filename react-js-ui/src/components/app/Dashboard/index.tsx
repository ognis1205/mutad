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
import React, { FC } from 'react';
import Link from 'next/link'
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import Grid, { GridSpacing } from '@material-ui/core/Grid';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import { styles } from "./styles";

interface Props extends WithStyles<typeof styles> {
  menus: any;
}

interface ContentProps extends WithStyles<typeof styles> {
  menu: any;
}

const DashboardContent: FC<ContentProps> = (props: ContentProps) => {
  return (
    <Card className={props.classes.card}>
      <Link href={props.menu.link} passHref>
        <CardActionArea>
          <CardMedia
            className={props.classes.cardMedia}
            image={props.menu.image}
            title={props.menu.text}
          />
          <CardContent>
            <Typography gutterBottom variant="h5" component="h2">
              {props.menu.text}
            </Typography>
            <Typography variant="body2" color="textSecondary" component="p">
              {props.menu.description}
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

const Dashboard = (props: Props) =>
  props.menus.map((menu, index) => (
    <Grid item key={index}>
      <StyledDashboardContent
        menu={menu}
      />
    </Grid>
  ));

export default Dashboard;
