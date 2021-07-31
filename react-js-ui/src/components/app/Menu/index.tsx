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
import React, { useState, createRef, forwardRef } from "react";
import Link from "next/link";
import {
  Collapse,
  List,
  ListItem,
  ListItemIcon,
  Menu as MUMenu,
  MenuItem as MUMenuItem,
} from "@material-ui/core";
import {
  ExpandMore,
  KeyboardArrowRight,
} from "@material-ui/icons";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import { MenuDef } from "../../../specs";

interface ItemProps extends WithStyles<typeof styles> {
  key: number;
  menu: MenuDef;
  navDrawerOpen: boolean;
}

const Item = withStyles(styles, { withTheme: true })((props: ItemProps) => {
  const [open, setOpen] = useState<boolean>(false);

  const divElem = createRef<null | HTMLDivElement>();

  const Div = forwardRef<HTMLDivElement>((_, ref) => {
    return (
      <div ref={ref} style={{ position: "absolute", right: 0 }} />
    );
  });

  const handleClick = (e: React.MouseEvent<HTMLElement>) => {
    e.stopPropagation();
    setOpen(!open);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const mini = () => {
    if (!props.menu.subMenu || !props.menu.subMenu.length) {
      return (
        <Link key={props.key} href={props.menu.link}>
          <MUMenuItem classes={{ root: props.classes.muMenuItem }}>
            <ListItemIcon className={props.classes.listItemIcon}>
              {props.menu.icon}
            </ListItemIcon>
          </MUMenuItem>
        </Link>
      );
    }

    return (
      <MUMenuItem
        key={props.key}
        classes={{ root: props.classes.muMenuItem }}
        onClick={handleClick}
      >
        <ListItemIcon className={props.classes.listItemIcon}>
          {props.menu.icon}
        </ListItemIcon>
        <Div ref={divElem} />
        <MUMenu
          anchorEl={divElem.current}
          classes={{ paper: props.classes.muMenu }}
          open={open}
          onClose={handleClose}
        >
          {props.menu.subMenu.map((item, index) => (
            <Link key={index} href={item.link}>
              <MUMenuItem key={index} classes={{ root: props.classes.subMuMenuItem }}>
                <ListItemIcon style={{ color: "white" }}>
                  {item.icon}
                </ListItemIcon>
                <span>{item.text}</span>
              </MUMenuItem>
            </Link>
          ))}
        </MUMenu>
      </MUMenuItem>
    );
  };

  const large = () => {
    if (!props.menu.subMenu || !props.menu.subMenu.length) {
      return (
        <Link key={props.key} href={props.menu.link}>
          <MUMenuItem classes={{ root: props.classes.subMuMenuItem }}>
            <ListItemIcon style={{ color: "white" }}>
              {props.menu.icon}
            </ListItemIcon>
            <span>{props.menu.text}</span>
          </MUMenuItem>
        </Link>
      );
    }

    return (
      <div>
        <ListItem
          key={props.key}
          classes={{ root: props.classes.subMuMenuItem }}
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
            classes={{ root: props.classes.subMenuItem }}
          >
            {props.menu.subMenu.map((item, index) => (
              <Link key={index} href={item.link}>
                <MUMenuItem
                  key={index}
                  classes={{ root: props.classes.subMuMenuItem }}
                >
                  <ListItemIcon style={{ color: "white" }}>
                    {item.icon}
                  </ListItemIcon>
                  <span>{item.text}</span>
                </MUMenuItem>
              </Link>
            ))}
          </List>
        </Collapse>
      </div>
    );
  };

  if (props.navDrawerOpen) return large();
  else return mini();
});

interface Props extends WithStyles<typeof styles> {
  menu: MenuDef[];
  navDrawerOpen: boolean;
}

export default withStyles(styles, { withTheme: true })((props: Props) => {
  return (
    <>
      {props.menu.map((item: MenuDef, index: number) => (
        <Item key={index} menu={item} navDrawerOpen={props.navDrawerOpen} />
      ))}
    </>
  );
});
