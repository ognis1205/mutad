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
import classNames from "classnames";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import Menu from "../Menu";
import * as Metadata from "../../../metadata";
import * as Themes from "../../../themes";

interface Props extends WithStyles<typeof styles> {
  theme: Themes.Theme;
  menu: Metadata.Item[];
  navDrawerOpen: boolean;
  handleChangeNavDrawer: () => void;
}

export default withStyles(styles, { withTheme: true })((props: Props) => {
  const drawerContent = () => (
    <React.Fragment>
      <Material.Box className={props.classes.logo}></Material.Box>
      <Menu menu={props.menu} navDrawerOpen={props.navDrawerOpen} />
    </React.Fragment>
  );

  return (
    <React.Fragment>
      <Material.Hidden mdUp>
        <Material.Drawer
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
        </Material.Drawer>
      </Material.Hidden>
      <Material.Hidden smDown>
        <Material.Drawer
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
        </Material.Drawer>
      </Material.Hidden>
    </React.Fragment>
  );
});
