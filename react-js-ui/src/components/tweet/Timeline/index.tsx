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
import * as Redux from "react-redux";
import {
  AppBar,
  Avatar,
  Box,
  Divider,
  Fab,
  IconButton,
  Link,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  ListSubheader,
  Paper,
  Toolbar,
  Typography,
} from "@material-ui/core";
import RefreshIcon from "@material-ui/icons/Refresh";
import SearchIcon from "@material-ui/icons/Search";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import {
  Selectors,
  Types,
} from "../../../state/ducks/tweets";

const getDateOf = (tweet: Types.Tweet) => {
  const date = new Date(tweet.timestamp);
  date.setMilliseconds(0);
  date.setSeconds(0);
  date.setMinutes(0);
  date.setHours(0);
  return date.toISOString();
};

const getDateString = (date: string) =>
  new Date(date).toLocaleDateString(undefined, {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });

const groupByDate = (tweets: Types.Tweet[]) => {
  return tweets.reduce((dates, tweet) => {
    const date = getDateOf(tweet);
    if (!dates[date]) dates[date] = [];
    dates[date] = dates[date].concat(tweet);
    return dates;
  }, {});
};

interface Props extends WithStyles<typeof styles> {
  text: string;
  hashtags: string;
  handleDialogOpen: (e: React.MouseEvent<HTMLButtonElement>) => void;
  handleNewPage: () => void;
  handleNewResult: () => void;
}

export default withStyles(styles, { withTheme: true })((props: Props) => {
  const tweets = Redux.useSelector(Selectors.getTweets);

  const scrollRef = React.createRef<HTMLUListElement>();

  const [tweetsByDate, setTweetsByDate] = React.useState({});

  React.useEffect(() => {
    setTweetsByDate(groupByDate(tweets));
  }, [tweets]);

  const handleScroll = (e: React.UIEvent<HTMLElement>) => {
    e.preventDefault();
    const bottom = e.currentTarget.scrollHeight - e.currentTarget.scrollTop === e.currentTarget.clientHeight;
    if (bottom) props.handleNewPage();
  };

  const handleRefresh = (e: React.MouseEvent<SVGSVGElement, MouseEvent>) => {
    e.preventDefault();
    scrollRef.current.scrollTo({top: 0});
    props.handleNewResult();
  };

  const withLink = (text: string) => (
    text
      .split(/\s+/)
      .map(t =>
        /#\w+/.test(t) ? (<Link key={t} component="button" variant="body2" onClick={() => { }}>{t}</Link>) : t
      )
      .reduce((r: any[], a: any) => r.concat(a, " "), [" "])
  );

  const isEmpty = (text: string) => (
    !/\S/.test(text)
  );

  return (
    <Box className={props.classes.box}>
      <Paper square className={props.classes.paper} onScroll={handleScroll} ref={scrollRef}>
        <Typography className={props.classes.text} variant="h5" gutterBottom>
          Timeline
        </Typography>
        <List className={props.classes.list}>
          {Object.entries(tweetsByDate).map(([key, value]: [string, Types.Tweet[]], dateIndex: number) => (
            <React.Fragment key={dateIndex}>
              <ListSubheader className={props.classes.subheader}>
                {getDateString(key)}
              </ListSubheader>
              {value.map((tweet: Types.Tweet, tweetIndex: number) => (
                <ListItem button key={tweetIndex}>
                  <ListItemAvatar>
                    <Avatar alt="Profile Picture" src={tweet.image_url} />
                  </ListItemAvatar>
                  <ListItemText primary={
                    <Box className={props.classes.listItemText}>
                      <strong>{tweet.user_name}</strong>{ "@" }{tweet.user_id}
                      <Divider/>
                      {withLink(tweet.text)}
                    </Box>
                  } secondary={tweet.timestamp} />
                </ListItem>
              ))}
            </React.Fragment>
          ))}
        </List>
      </Paper>
      <AppBar position="fixed" color="primary" className={props.classes.appBar}>
        <Toolbar>
          <Fab color="secondary" aria-label="add" className={props.classes.fabButton}>
            <RefreshIcon onClick={handleRefresh}/>
          </Fab>
          <Box className={props.classes.grow} />
          <IconButton color="inherit" onClick={props.handleDialogOpen}>
            <SearchIcon/>
          </IconButton>
        </Toolbar>
      </AppBar>
    </Box>
  );
});
