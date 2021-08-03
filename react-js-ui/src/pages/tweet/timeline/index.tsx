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
import { NextPage } from "next";
import Timeline from "../../../components/tweet/Timeline";
import TimelineSearch from "../../../components/tweet/TimelineSearch";
import {
  Actions,
} from "../../../state/ducks/tweets";

const Index: NextPage = () => {
  const dispatch = Redux.useDispatch();

  const scrollRef = React.createRef<HTMLUListElement>();

  const [dialogOpen, setDialogOpen] = React.useState<boolean>(false);

  const [text, setText] = React.useState<string>("");

  const [hashtags, setHashtags] = React.useState<string>("");

  const [now, setNow] = React.useState<number>((new Date()).getTime());

  const [page, setPage] = React.useState<number>(0);

  const handleOpen = () => {
    setDialogOpen(true);
  };

  const handleClose = () => {
    setDialogOpen(false);
  };

  const handleText = (text: string) => {
    setText(text);
  };

  const handleHashtags = (hashtags: string) => {
    setHashtags(hashtags);
  };

  const toHashtags = (hashtags: string) => (
    /\S/.test(hashtags) ? hashtags.split(/\s+/) : []
  );

  const handleScroll = () => {
    setPage(page + 1);
    dispatch(Actions.updLatestTweets({
      before: now,
      text: text,
      hashtags: toHashtags(hashtags),
      page: page,
      size: 50,
    }));
  };

  const handleSearch = () => {
    scrollRef.current.scrollTo({top: 0});
    setNow((new Date()).getTime());
    setPage(0);
    dispatch(Actions.reqLatestTweets({
      before: now,
      text: text,
      hashtags: toHashtags(hashtags),
      page: 0,
      size: 50,
    }));
  };

  React.useEffect(() => {
    dispatch(Actions.reqLatestTweets({
      before: now,
      text: text,
      hashtags: toHashtags(hashtags),
      page: page,
      size: 50,
    }));
  }, []);

  return (
    <React.Fragment>
      <Timeline
        ref={scrollRef}
        text={text}
        hashtags={hashtags}
        onDialog={handleOpen}
        onScroll={handleScroll}
        onSearch={handleSearch}/>
      <TimelineSearch
        text={text}
        hashtags={hashtags}
        dialogOpen={dialogOpen}
        onDialog={handleClose}
        onText={handleText}
        onHashtags={handleHashtags}
        onSearch={handleSearch}/>
    </React.Fragment>
  );
};

export default Index;
