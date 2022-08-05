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
import * as Material from "@material-ui/core";
import RefreshIcon from "@material-ui/icons/Refresh";
import SearchIcon from "@material-ui/icons/Search";
import withStyles, { WithStyles } from "@material-ui/core/styles/withStyles";
import styles from "./styles";
import * as Context from "../../../../contexts/tweet/timeline";

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
  onRefresh?: () => void;
}

export default withStyles(styles, { withTheme: true })(
  React.forwardRef<HTMLUListElement, Props>((props, scrollRef) => {
    const { state, dispatch } = React.useContext(Context.store);

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
      const height = e.currentTarget.clientHeight;
      if (residual === height)
        dispatch(
          Context.Actions.more(state.text, state.hashtags, state.page, dispatch)
        );
    };

    const handleRefresh = (e: React.MouseEvent<HTMLButtonElement>) => {
      e.preventDefault();
      if (props.onRefresh) props.onRefresh();
      dispatch(Context.Actions.refresh(state.text, state.hashtags, dispatch));
    };

    const withLink = (text: string) => {
      const link = (text: string, index: number) => (
        <Material.Link key={index} component="button" variant="body2">
          {text}
        </Material.Link>
      );

      return text
        .split(/\s+/)
        .map((text, index) => (/#\w+/.test(text) ? link(text, index) : text))
        .reduce(
          (r: React.ReactNode[], a: React.ReactNode) => r.concat(a, " "),
          [" "]
        );
    };

    return (
      <Material.Box className={props.classes.box}>
        <Material.Paper
          square
          className={props.classes.paper}
          onScroll={handleScroll}
          ref={scrollRef}
        >
          <Material.Typography
            className={props.classes.text}
            variant="h5"
            gutterBottom
          >
            Timeline
          </Material.Typography>
          <Material.List className={props.classes.list}>
            {Object.entries(tweetsByDate).map(
              (
                [key, value]: [string, Context.Tweet.Model[]],
                dateIndex: number
              ) => (
                <React.Fragment key={dateIndex}>
                  <Material.ListSubheader className={props.classes.subheader}>
                    {getDateString(key)}
                  </Material.ListSubheader>
                  {value.map(
                    (tweet: Context.Tweet.Model, tweetIndex: number) => (
                      <Material.ListItem button key={tweetIndex}>
                        <Material.ListItemAvatar>
                          <Material.Avatar
                            alt="Profile Picture"
                            src={tweet.image_url}
                          />
                        </Material.ListItemAvatar>
                        <Material.ListItemText
                          primary={
                            <Material.Box
                              className={props.classes.listItemText}
                            >
                              <strong>{tweet.user_name}</strong>
                              {"@"}
                              {tweet.user_id}
                              <Material.Divider />
                              {withLink(tweet.text)}
                            </Material.Box>
                          }
                          secondary={tweet.timestamp}
                        />
                      </Material.ListItem>
                    )
                  )}
                </React.Fragment>
              )
            )}
          </Material.List>
        </Material.Paper>
        <Material.AppBar className={props.classes.appBar}>
          <Material.Toolbar>
            <Material.Box className={props.classes.grow} />
            <Material.IconButton color="inherit" onClick={handleSearch}>
              <SearchIcon />
            </Material.IconButton>
            <Material.IconButton color="inherit" onClick={handleRefresh}>
              <RefreshIcon />
            </Material.IconButton>
          </Material.Toolbar>
        </Material.AppBar>
      </Material.Box>
    );
  })
);
