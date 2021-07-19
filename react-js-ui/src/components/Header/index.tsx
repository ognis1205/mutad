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
import React, { FC } from "react";
import classNames from "classnames";
import AppBar from "@material-ui/core/AppBar";
import IconButton from "@material-ui/core/IconButton";
import MenuIcon from "@material-ui/icons/Menu";
import { Toolbar, Typography } from "@material-ui/core";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import { styles } from "./styles";

interface Props extends WithStyles<typeof styles> {
  title: string;
  handleChangeNavDrawer: (e: any) => void;
  navDrawerOpen: boolean;
}

const Header: FC<Props> = (props: Props) => {
  return (
    <div>
      <AppBar
        className={classNames(props.classes.appBar, {
          [props.classes.appBarShift]: props.navDrawerOpen,
        })}
      >
        <Toolbar>
          <IconButton
            className={props.classes.menuButton}
            color="inherit"
            aria-label="Open drawer"
            onClick={props.handleChangeNavDrawer}
          >
            <MenuIcon />
          </IconButton>
          <div className={props.classes.grow}>
            <Typography variant="h5" color="inherit">
              {props.title}
            </Typography>
          </div>
        </Toolbar>
      </AppBar>
    </div>
  );
};

export default withStyles(styles)(Header);
