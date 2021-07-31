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
import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import Immutable from "immutable";
import {
  AppBar,
  Avatar,
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
import { getTweets } from "../../../state/ducks/tweets/selectors";
import { reqLatestTweets, updLatestTweets } from "../../../state/ducks/tweets/actions";

const getDateOf = (tweet: Immutable.Map<string, any>) => {
  const date = new Date(tweet.get("timestamp"));
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

const groupByDate = (tweets: Immutable.List<Immutable.Map<string, any>>) => {
  return tweets.reduce((dates, tweet) => {
    const date = getDateOf(tweet);
    if (!dates[date]) dates[date] = [];
    dates[date] = dates[date].concat(tweet);
    return dates;
  }, {});
};

interface Props extends WithStyles<typeof styles> {
}

export default withStyles(styles, { withTheme: true })((props: Props) => {
  const dispatch = useDispatch();

  const tweets = useSelector(getTweets);

  const [tweetsByDate, setTweetsByDate] = useState({});

  const [text, setText] = useState<string>("");

  const [hashtags, setHashtags] = useState<string[]>([]);

  const [now, setNow] = useState<string>((new Date()).toISOString());

  const [page, setPage] = useState<number>(0);

  useEffect(() => {
    dispatch(reqLatestTweets({
      before: now,
      text: text,
      hashtags: hashtags,
      page: page,
      size: 50,
    }));
  }, []);

  useEffect(() => {
    setTweetsByDate(groupByDate(tweets));
  }, [tweets]);

  const handleScroll = (e: any) => {
    e.preventDefault();
    const bottom = e.target.scrollHeight - e.target.scrollTop === e.target.clientHeight;
    if (bottom) {
      setPage(page + 1);
      dispatch(updLatestTweets({
        before: now,
        text: text,
        hashtags: hashtags,
        page: page,
        size: 50,
      }));
    }
  };

  const handleRefresh = (e: any) => {
    e.preventDefault();
    setNow((new Date()).toISOString());
    setPage(0);
    dispatch(reqLatestTweets({
      before: now,
      text: text,
      hashtags: hashtags,
      page: 0,
      size: 50,
    }));
  };

  const withLink = (text: string) => (
    text
      .split(/\s+/)
      .map(t =>
        /#\w+/.test(t) ? (<Link component="button" variant="body2" onClick={() => { }}>{t}</Link>) : t
      )
      .reduce((r: any[], a: any) => r.concat(a, " "), [" "])
  );

  return (
    <div className={props.classes.container}>
      <Paper square className={props.classes.paper} onScroll={handleScroll}>
        <Typography className={props.classes.text} variant="h5" gutterBottom>
          Timeline
        </Typography>
        <List className={props.classes.list}>
          {Object.entries(tweetsByDate).map(([key, value]: [string, Immutable.Map<string, any>[]], index: number) => (
            <React.Fragment key={index}>
              <ListSubheader className={props.classes.subheader}>{getDateString(key)}</ListSubheader>
              {value.map((tweet: Immutable.Map<string, any>, _: number) => (
              <ListItem button>
                <ListItemAvatar>
                  <Avatar alt="Profile Picture" src={tweet.get("image_url")} />
                </ListItemAvatar>
                <ListItemText primary={
                  <div className={props.classes.listItemText}>
                    <strong>{ tweet.get("user_name") }</strong>{ "@" }{ tweet.get("user_id") }
                    <Divider/>
                    { withLink(tweet.get("text")) }
                  </div>
                  } secondary={ tweet.get("timestamp") } />
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
          <div className={props.classes.grow} />
          <IconButton color="inherit">
            <SearchIcon />
          </IconButton>
        </Toolbar>
      </AppBar>
    </div>
  );
});
