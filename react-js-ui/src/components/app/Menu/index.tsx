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
import { Menu as MUMenu } from "@material-ui/core";
import { MenuItem as MUMenuItem } from "@material-ui/core";
import ListItem from "@material-ui/core/ListItem";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import Collapse from "@material-ui/core/Collapse";
import List from "@material-ui/core/List";
import ExpandMore from "@material-ui/icons/ExpandMore";
import KeyboardArrowRight from "@material-ui/icons/KeyboardArrowRight";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import { MenuDef } from "../../../specs";

interface Props extends WithStyles<typeof styles> {
  menu: MenuDef[];
  navDrawerOpen: boolean;
}

interface ContentProps extends WithStyles<typeof styles> {
  key: number;
  menu: MenuDef;
  navDrawerOpen: boolean;
}

const MenuItem: FC<ContentProps> = (props: ContentProps) => {
//  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

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
//    setAnchorEl(e.currentTarget);
    setOpen(!open);
  };

  const handleClose = () => {
//    setAnchorEl(null);
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
};

const StyledMenuItem = withStyles(styles, { withTheme: true })(MenuItem);

const Menu = (props: Props) => (
  <>
    {props.menu.map((item: MenuDef, index: number) => (
      <StyledMenuItem
        key={index}
        menu={item}
        navDrawerOpen={props.navDrawerOpen}
      />
    ))}
  </>
);

export default withStyles(styles, { withTheme: true })(Menu);
