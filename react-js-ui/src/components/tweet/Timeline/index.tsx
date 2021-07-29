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
import React, { FC, useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import IconButton from "@material-ui/core/IconButton";
import Paper from "@material-ui/core/Paper";
import Fab from "@material-ui/core/Fab";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemAvatar from "@material-ui/core/ListItemAvatar";
import ListItemText from "@material-ui/core/ListItemText";
import ListSubheader from "@material-ui/core/ListSubheader";
import Avatar from "@material-ui/core/Avatar";
import RefreshIcon from "@material-ui/icons/Refresh";
import SearchIcon from "@material-ui/icons/Search";
import Link from '@material-ui/core/Link';
import Divider from '@material-ui/core/Divider';
import { styles } from "./styles";
import { getTweets } from "../../../state/ducks/tweets/selectors";
import { reqLatestTweets } from "../../../state/ducks/tweets/actions";

const sortByDate = (a: string, b: string) =>
  new Date(b).valueOf() - new Date(a).valueOf();

const getDateOf = (tweet) => {
  const date = new Date(tweet.get("timestamp"));
  date.setMilliseconds(0);
  date.setSeconds(0);
  date.setMinutes(0);
  date.setHours(0);
  return date.toISOString();
};

const getDateString = (date) =>
  new Date(date).toLocaleDateString(undefined, {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });

const groupByDate = (tweets) => {
  const groups = tweets.reduce((dates, tweet) => {
    const date = getDateOf(tweet);
    if (!dates[date]) dates[date] = [];
    dates[date] = dates[date].concat(tweet);
    return dates;
  }, {});
  return {
    dates: Object.keys(groups).sort(sortByDate),
    tweetsByDate: groups,
  };
};

interface Props extends WithStyles<typeof styles> {
}

const Timeline: FC<Props> = (props: Props) => {
  const dispatch = useDispatch();

  const tweets = useSelector(getTweets);

  const [ dates, setDates ] = useState<string[]>([]);

  const [ tweetsByDate, setTweetsByDate ] = useState({});

  const [isFadeIn, setIsFadeIn] = useState(false);

  useEffect(() => {
    dispatch(reqLatestTweets({
      text: "",
      hashtags: [],
    }));
  }, []);

  useEffect(() => {
    const { dates, tweetsByDate } = groupByDate(tweets);
    setDates(dates);
    setTweetsByDate(tweetsByDate);
  }, [tweets]);

  const withLink = (text) => (
    text
      .split(/\s+/)
      .map(t =>
        /#\w+/.test(t) ? (<Link component="button" variant="body2" onClick={() => { }}>{t}</Link>) : t
      )
      .reduce((r, a) => r.concat(a, " "), [" "])
  );

  return (
    <div className={props.classes.container}>
      <Paper square className={props.classes.paper}>
        <Typography className={props.classes.text} variant="h5" gutterBottom>
          Timeline
        </Typography>
        <List className={props.classes.list}>
          {Object.entries(tweetsByDate).map(([key, value], index) => (
            <React.Fragment key={index}>
              <ListSubheader className={props.classes.subheader}>{getDateString(key)}</ListSubheader>
              {value.map((tweet, index) => (
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
                  } secondary={tweet.get("timestamp")} />
              </ListItem>
              ))}
            </React.Fragment>
          ))}
        </List>
      </Paper>
      <AppBar position="fixed" color="primary" className={props.classes.appBar}>
        <Toolbar>
          <Fab color="secondary" aria-label="add" className={props.classes.fabButton}>
            <RefreshIcon />
          </Fab>
          <div className={props.classes.grow} />
          <IconButton color="inherit">
            <SearchIcon />
          </IconButton>
        </Toolbar>
      </AppBar>
    </div>
  );
};

export default withStyles(styles, { withTheme: true })(Timeline);
