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
import { createStyles } from "@material-ui/core/styles";

export const styles = (theme: any) =>
  createStyles({
    timeline: {
      paddingLeft: '16px',
      paddingRight: '16px',
//      maxHeight: '100%',
//      overflow: 'scroll',
      //      margin: 'auto',
      position:'absolute',
//    top: '5%',
//    left: '5%',
      overflow: 'scroll',
      height: '100%',
//      width: '100%',
      display:'block'
    },
    date: {
      marginBottom: '1em',
    },
    muiAvatar: {
      width: 50,
      height: 50,
    },
    listItemText: {
      whiteSpace: 'nowrap',
      overflow: 'hidden',
      textOverflow: 'ellipsis',
    },
  });
