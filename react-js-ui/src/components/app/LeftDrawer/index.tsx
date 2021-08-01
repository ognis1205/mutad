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
import classNames from "classnames";
import {
  Box,
  Drawer,
  Hidden,
} from "@material-ui/core";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import Menu from "../Menu";
import { MenuDef } from "../../../specs";
import { CustomTheme } from "../../../theme";

interface Props extends WithStyles<typeof styles> {
  theme: CustomTheme;
  menu: MenuDef[];
  navDrawerOpen: boolean;
  handleChangeNavDrawer: (e: any) => void;
}

export default withStyles(styles, { withTheme: true })((props: Props) => {
  const drawerContent = () => (
    <React.Fragment>
      <Box className={props.classes.logo}></Box>
      <Menu menu={props.menu} navDrawerOpen={props.navDrawerOpen} />
    </React.Fragment>
  );

  return (
    <React.Fragment>
      <Hidden mdUp>
        <Drawer
          variant="temporary"
          anchor={props.theme.direction === "rtl" ? "right" : "left"}
          open={props.navDrawerOpen}
          onClose={props.handleChangeNavDrawer}
          classes={{
            paper: props.classes.drawer,
          }}
          ModalProps={{
            keepMounted: true,
          }}
        >
          {drawerContent()}
        </Drawer>
      </Hidden>
      <Hidden smDown>
        <Drawer
          open={props.navDrawerOpen}
          variant="permanent"
          classes={{
            paper: classNames(
              props.classes.drawer,
              !props.navDrawerOpen && props.classes.drawerClose
            ),
          }}
        >
          {drawerContent()}
        </Drawer>
      </Hidden>
    </React.Fragment>
  );
});
