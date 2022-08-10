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
import Link from "next/link";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import * as Metadata from "../../../metadata";

interface ItemProps extends WithStyles<typeof styles> {
  subMenu: Metadata.SubItem;
}

const Item = withStyles(styles, { withTheme: true })((props: ItemProps) => {
  return (
    <Material.Card className={props.classes.card}>
      <Link href={props.subMenu.link} passHref>
        <Material.CardActionArea>
          <Material.CardMedia
            className={props.classes.cardMedia}
            image={props.subMenu.image}
            title={props.subMenu.text}
          />
          <Material.CardContent>
            <Material.Typography gutterBottom variant="h5" component="h2">
              {props.subMenu.text}
            </Material.Typography>
            <Material.Typography
              variant="body2"
              color="textSecondary"
              component="p"
            >
              {props.subMenu.description}
            </Material.Typography>
          </Material.CardContent>
        </Material.CardActionArea>
      </Link>
      <Material.CardActions>
        <Material.Button size="small" color="primary">
          Reference
        </Material.Button>
      </Material.CardActions>
    </Material.Card>
  );
});

interface Props extends WithStyles<typeof styles> {
  menu: Metadata.SubItem[];
}

export default withStyles(styles, { withTheme: true })((props: Props) => (
  <Material.Grid container spacing={2} className={props.classes.grid}>
    {props.menu.map((subMenu: Metadata.SubItem, index: number) => (
      <Material.Grid item xs={4} key={index}>
        <Item subMenu={subMenu} />
      </Material.Grid>
    ))}
  </Material.Grid>
));
