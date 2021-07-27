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
import React, { FC, useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import Card from '@material-ui/core/Card';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import Link from '@material-ui/core/Link';
import MuiAvatar from '@material-ui/core/Avatar';
import Divider from '@material-ui/core/Divider';
import Typography from '@material-ui/core/Typography';
import { styles } from "./styles";
import { getTweets } from "../../../state/ducks/tweets/selectors";
import { reqLatestTweets } from "../../../state/ducks/tweets/actions";

interface Props extends WithStyles<typeof styles> {
}

interface TweetProps extends WithStyles<typeof styles> {
  tweets: any;
}

const TweetsByDate: FC<TweetProps> = (props: TweetProps) => {
  const withLink = (text) => (
    text
      .split(/\s+/)
      .map(t =>
        /#\w+/.test(t) ? (<Link component="button" variant="body2" onClick={() => { }}>{t}</Link>) : t
      )
      .reduce((r, a) => r.concat(a, " "), [" "])
  );

  return (
    <Card>
      <List>
        {props.tweets.map((item, index) => (
          <ListItem key={index}>
            <ListItemAvatar>
              <MuiAvatar
                className={props.classes.avatar}
                src={item.get("image_url")}
              />
            </ListItemAvatar>
            <ListItemText
              primary={
                <div className={props.classes.truncate}>
                  <strong>{ item.get("user_name") }</strong>{ "@" }{ item.get("user_id") }
                  <Divider/>
                  { withLink(item.get("text")) }
                </div>
              }
              secondary={ new Date(item.get("timestamp")).toLocaleString() }
            />
          </ListItem>
        ))}
      </List>
    </Card>
  );
};

const StyledTweetsByDate = withStyles(styles, { withTheme: true })(TweetsByDate);

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
    weekday: 'long',
    year: 'numeric',
    month: 'long',
    day: 'numeric',
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

const Timeline: FC<Props> = (props: Props) => {
  const dispatch = useDispatch();

  const tweets = useSelector(getTweets);

  const [ dates, setDates ] = useState<string[]>([]);

  const [ tweetsByDate, setTweetsByDate ] = useState({});

  useEffect(() => {
    dispatch(reqLatestTweets({
      text: "",
      hashtags: [],
    }));
    setInterval(() => {
      dispatch(reqLatestTweets({
        text: "",
        hashtags: [],
      }));
    }, 30000);
  }, []);

  useEffect(() => {
    const { dates, tweetsByDate } = groupByDate(tweets);
    setDates(dates);
    setTweetsByDate(tweetsByDate);
  }, [tweets]);

  return (
    <div className={props.classes.timelineContainer}>
      {dates.map(date => (
        <div key={date} className={props.classes.date}>
          <Typography gutterBottom>
            {getDateString(date)}
          </Typography>
          <StyledTweetsByDate tweets={tweetsByDate[date]}/>
        </div>
      ))}
    </div>
  );
};

export default withStyles(styles, { withTheme: true })(Timeline);
