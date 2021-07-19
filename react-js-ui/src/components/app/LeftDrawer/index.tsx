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
import Drawer from "@material-ui/core/Drawer";
import Hidden from "@material-ui/core/Hidden";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import { styles } from "./styles";
import Menus from "../Menus";

interface Props extends WithStyles<typeof styles> {
  theme: any;
  menus: any;
  navDrawerOpen: boolean;
  handleChangeNavDrawer: (e: any) => void;
}

const LeftDrawer: FC<Props> = (props: Props) => {
  const drawerContent = () => (
    <div>
      <div className={props.classes.logo}></div>
      <Menus menus={props.menus} navDrawerOpen={props.navDrawerOpen} />
    </div>
  );

  return (
    <div>
      <Hidden mdUp>
        <Drawer
          variant="temporary"
          anchor={props.theme.direction === "rtl" ? "right" : "left"}
          open={props.navDrawerOpen}
          onClose={props.handleChangeNavDrawer}
          classes={{
            paper: props.classes.drawerPaper,
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
              props.classes.drawerPaper,
              !props.navDrawerOpen && props.classes.drawerPaperClose
            ),
          }}
        >
          {drawerContent()}
        </Drawer>
      </Hidden>
    </div>
  );
};

export default withStyles(styles, { withTheme: true })(LeftDrawer);
