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
import React, { FC, useState, createRef, forwardRef } from "react";
import Link from "next/link";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import Collapse from "@material-ui/core/Collapse";
import List from "@material-ui/core/List";
import ExpandMore from "@material-ui/icons/ExpandMore";
import KeyboardArrowRight from "@material-ui/icons/KeyboardArrowRight";
import { styles } from "./styles";

interface Props extends WithStyles<typeof styles> {
  menus: any;
  navDrawerOpen: boolean;
}

interface ContentProps extends WithStyles<typeof styles> {
  key: string;
  menu: any;
  navDrawerOpen: boolean;
}

const MenuContent: FC<ContentProps> = (props: ContentProps) => {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const [open, setOpen] = useState<boolean>(false);

  const divElem = createRef<null | HTMLDivElement>();

  const Div = forwardRef<HTMLDivElement>((props, ref) => {
    return (
      <>
        <div ref={ref} style={{ position: "absolute", right: 0 }} />
      </>
    );
  });

  const handleClick = (e: React.MouseEvent<HTMLElement>) => {
    e.stopPropagation();
    setAnchorEl(e.currentTarget);
    setOpen(!open);
  };

  const handleClose = () => {
    setAnchorEl(null);
    setOpen(false);
  };

  const mini = () => {
    if (!props.menu.subMenus || !props.menu.subMenus.length) {
      return (
        <Link key={props.key} href={props.menu.link}>
          <MenuItem classes={{ root: props.classes.miniMenuItem }}>
            <ListItemIcon className={props.classes.miniIcon}>
              {props.menu.icon}
            </ListItemIcon>
          </MenuItem>
        </Link>
      );
    }

    return (
      <MenuItem
        key={props.key}
        classes={{ root: props.classes.miniMenuItem }}
        onClick={handleClick}
      >
        <ListItemIcon className={props.classes.miniIcon}>
          {props.menu.icon}
        </ListItemIcon>
        <Div ref={divElem} />
        <Menu
          anchorEl={divElem.current}
          classes={{ paper: props.classes.popupSubMenus }}
          open={open}
          onClose={handleClose}
        >
          {props.menu.subMenus.map((menu, index) => (
            <Link key={index} href={menu.link}>
              <MenuItem key={index} classes={{ root: props.classes.menuItem }}>
                <ListItemIcon style={{ color: "white" }}>
                  {menu.icon}
                </ListItemIcon>
                <span>{menu.text}</span>
              </MenuItem>
            </Link>
          ))}
        </Menu>
      </MenuItem>
    );
  };

  const large = () => {
    if (!props.menu.subMenus || !props.menu.subMenus.length) {
      return (
        <Link key={props.key} href={props.menu.link}>
          <MenuItem classes={{ root: props.classes.menuItem }}>
            <ListItemIcon style={{ color: "white" }}>
              {props.menu.icon}
            </ListItemIcon>
            <span>{props.menu.text}</span>
          </MenuItem>
        </Link>
      );
    }

    return (
      <div>
        <ListItem
          key={props.key}
          classes={{ root: props.classes.menuItem }}
          onClick={handleClick}
        >
          <ListItemIcon style={{ color: "white" }}>
            {props.menu.icon}
          </ListItemIcon>
          <span>{props.menu.text}</span>
          {open ? (
            <ExpandMore className={props.classes.chevronIcon} />
          ) : (
            <KeyboardArrowRight className={props.classes.chevronIcon} />
          )}
        </ListItem>
        <Collapse in={open} timeout="auto" unmountOnExit>
          <List
            component="div"
            disablePadding
            classes={{ root: props.classes.subMenus }}
          >
            {props.menu.subMenus.map((menu, index) => (
              <Link key={index} href={menu.link}>
                <MenuItem
                  key={index}
                  classes={{ root: props.classes.menuItem }}
                >
                  <ListItemIcon style={{ color: "white" }}>
                    {menu.icon}
                  </ListItemIcon>
                  <span>{menu.text}</span>
                </MenuItem>
              </Link>
            ))}
          </List>
        </Collapse>
      </div>
    );
  };

  if (props.navDrawerOpen) return large();
  else return mini();
};

const StyledMenuContent = withStyles(styles, { withTheme: true })(MenuContent);

const Menus = (props: Props) =>
  props.menus.map((menu, index) => (
    <StyledMenuContent
      key={index}
      menu={menu}
      navDrawerOpen={props.navDrawerOpen}
    />
  ));

export default Menus;
