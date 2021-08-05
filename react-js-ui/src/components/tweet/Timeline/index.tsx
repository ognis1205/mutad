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
import {
  AppBar,
  Avatar,
  Box,
  Divider,
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
import * as Context from "../../../contexts/tweet/timeline";

const getDateOf = (tweet: Context.Tweet.Model) => {
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

const groupByDate = (tweets: Context.Tweet.Model[]) => {
  return tweets.reduce((dates, tweet) => {
    const date = getDateOf(tweet);
    if (!dates[date]) dates[date] = [];
    dates[date] = dates[date].concat(tweet);
    return dates;
  }, {});
};

interface Props extends WithStyles<typeof styles> {
  onRefresh: () => void;
}

export default withStyles(styles, { withTheme: true })(React.forwardRef<HTMLUListElement, Props>((props, scrollRef) => {
  const {state, dispatch} = React.useContext(Context.store);

  const [tweetsByDate, setTweetsByDate] = React.useState({});

  React.useEffect(() => {
    dispatch(Context.Actions.init(dispatch));
  }, []);

  React.useEffect(() => {
    setTweetsByDate(groupByDate(state.latest));
  }, [state.latest]);

  const handleSearch = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    dispatch(Context.Actions.open());
  };

  const handleScroll = (e: React.UIEvent<HTMLElement>) => {
    e.preventDefault();
    const residual = e.currentTarget.scrollHeight - e.currentTarget.scrollTop;
    const height   = e.currentTarget.clientHeight;
    if (residual === height)
      dispatch(Context.Actions.more(
        state.text,
        state.hashtags,
        state.page,
        dispatch));
  };

  const handleRefresh = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    props.onRefresh();
    dispatch(Context.Actions.refresh(
      state.text,
      state.hashtags,
      dispatch));
  };

  const withLink = (text: string) => {
    const link = (text: string, index: number) => (
      <Link key={index} component="button" variant="body2" onClick={() => { }}>
        {text}
      </Link>
    );

    return text
      .split(/\s+/)
      .map((text, index) =>
        /#\w+/.test(text) ? link(text, index) : text
      )
      .reduce((r: any[], a: any) => r.concat(a, " "), [" "])
  };

  return (
    <Box className={props.classes.box}>
      <Paper square className={props.classes.paper} onScroll={handleScroll} ref={scrollRef}>
        <Typography className={props.classes.text} variant="h5" gutterBottom>
          Timeline
        </Typography>
        <List className={props.classes.list}>
          {Object.entries(tweetsByDate).map(([key, value]: [string, Context.Tweet.Model[]], dateIndex: number) => (
            <React.Fragment key={dateIndex}>
              <ListSubheader className={props.classes.subheader}>
                {getDateString(key)}
              </ListSubheader>
              {value.map((tweet: Context.Tweet.Model, tweetIndex: number) => (
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
      <AppBar className={props.classes.appBar}>
        <Toolbar>
          <Box className={props.classes.grow} />
          <IconButton color="inherit" onClick={handleSearch}>
            <SearchIcon/>
          </IconButton>
          <IconButton color="inherit" onClick={handleRefresh}>
            <RefreshIcon/>
          </IconButton>
        </Toolbar>
      </AppBar>
    </Box>
  );
}));
