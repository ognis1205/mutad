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

  const [dialogOpen, setDialogOpen] = React.useState<boolean>(false);

  const [text, setText] = React.useState<string>("");

  const [hashtags, setHashtags] = React.useState<string>("");

  const [now, setNow] = React.useState<string>((new Date()).toISOString());

  const [page, setPage] = React.useState<number>(0);

  const handleDialogOpen = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    setDialogOpen(true);
  };

  const handleDialogClose = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    setDialogOpen(false);
  };

  const handleNewText = (text: string) => {
    setText(text);
  };

  const handleNewHashtags = (hashtags: string) => {
    setHashtags(hashtags);
  };

  const toHashtags = (hashtags: string) => (
    /\S/.test(hashtags) ? hashtags.split(/\s+/) : []
  );

  const handleNewPage = () => {
    setPage(page + 1);
    dispatch(Actions.updLatestTweets({
      before: now,
      text: text,
      hashtags: toHashtags(hashtags),
      page: page,
      size: 50,
    }));
  };

  const handleNewResult = () => {
    setNow((new Date()).toISOString());
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
        text={text}
        hashtags={hashtags}
        handleDialogOpen={handleDialogOpen}
        handleNewPage={handleNewPage}
        handleNewResult={handleNewResult}/>
    <TimelineSearch
      text={text}
      hashtags={hashtags}
      dialogOpen={dialogOpen}
      handleDialogClose={handleDialogClose}
      handleNewText={handleNewText}
      handleNewHashtags={handleNewHashtags}
      handleNewResult={handleNewResult}/>
    </React.Fragment>
  );
};

export default Index;
