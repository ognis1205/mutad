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
import ExpandMore from "@material-ui/icons/ExpandMore";
import KeyboardArrowRight from "@material-ui/icons/KeyboardArrowRight";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import * as Metadata from "../../../metadata";

interface ItemProps extends WithStyles<typeof styles> {
  key: number;
  menu: Metadata.Menu.Item;
  navDrawerOpen: boolean;
}

const Anchor = React.forwardRef<HTMLDivElement>((_, ref) => {
  return <div ref={ref} style={{ position: "absolute", right: 0 }} />;
});

Anchor.displayName = "Anchor";

const Item = withStyles(styles, { withTheme: true })((props: ItemProps) => {
  const [open, setOpen] = React.useState<boolean>(false);

  const anchor = React.createRef<HTMLDivElement>();

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
          <Material.MenuItem classes={{ root: props.classes.muMenuItem }}>
            <Material.ListItemIcon className={props.classes.listItemIcon}>
              {props.menu.icon}
            </Material.ListItemIcon>
          </Material.MenuItem>
        </Link>
      );
    }

    return (
      <Material.MenuItem
        key={props.key}
        classes={{ root: props.classes.muMenuItem }}
        onClick={handleClick}
      >
        <Material.ListItemIcon className={props.classes.listItemIcon}>
          {props.menu.icon}
        </Material.ListItemIcon>
        <Anchor ref={anchor} />
        <Material.Menu
          anchorEl={anchor.current}
          classes={{ paper: props.classes.muMenu }}
          open={open}
          onClose={handleClose}
        >
          {props.menu.subMenu.map((item, index) => (
            <Link key={index} href={item.link}>
              <Material.MenuItem
                key={index}
                classes={{ root: props.classes.subMuMenuItem }}
              >
                <Material.ListItemIcon style={{ color: "white" }}>
                  {item.icon}
                </Material.ListItemIcon>
                <span>{item.text}</span>
              </Material.MenuItem>
            </Link>
          ))}
        </Material.Menu>
      </Material.MenuItem>
    );
  };

  const large = () => {
    if (!props.menu.subMenu || !props.menu.subMenu.length) {
      return (
        <Link key={props.key} href={props.menu.link}>
          <Material.MenuItem classes={{ root: props.classes.subMuMenuItem }}>
            <Material.ListItemIcon style={{ color: "white" }}>
              {props.menu.icon}
            </Material.ListItemIcon>
            <span>{props.menu.text}</span>
          </Material.MenuItem>
        </Link>
      );
    }

    return (
      <React.Fragment>
        <Material.ListItem
          key={props.key}
          classes={{ root: props.classes.subMuMenuItem }}
          onClick={handleClick}
        >
          <Material.ListItemIcon style={{ color: "white" }}>
            {props.menu.icon}
          </Material.ListItemIcon>
          <span>{props.menu.text}</span>
          {open ? (
            <ExpandMore className={props.classes.chevronIcon} />
          ) : (
            <KeyboardArrowRight className={props.classes.chevronIcon} />
          )}
        </Material.ListItem>
        <Material.Collapse in={open} timeout="auto" unmountOnExit>
          <Material.List
            component="div"
            disablePadding
            classes={{ root: props.classes.subMenuItem }}
          >
            {props.menu.subMenu.map((item, index) => (
              <Link key={index} href={item.link}>
                <Material.MenuItem
                  key={index}
                  classes={{ root: props.classes.subMuMenuItem }}
                >
                  <Material.ListItemIcon style={{ color: "white" }}>
                    {item.icon}
                  </Material.ListItemIcon>
                  <span>{item.text}</span>
                </Material.MenuItem>
              </Link>
            ))}
          </Material.List>
        </Material.Collapse>
      </React.Fragment>
    );
  };

  if (props.navDrawerOpen) return large();
  else return mini();
});

interface Props extends WithStyles<typeof styles> {
  menu: Metadata.Menu.Item[];
  navDrawerOpen: boolean;
}

export default withStyles(styles, { withTheme: true })((props: Props) => {
  return (
    <Material.List>
      {props.menu.map((item: Metadata.Menu.Item, index: number) => (
        <Item key={index} menu={item} navDrawerOpen={props.navDrawerOpen} />
      ))}
    </Material.List>
  );
});
