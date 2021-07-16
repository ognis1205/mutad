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
import React, { FC                } from 'react';
import        { AppProps          } from 'next/app';
import        { MuiThemeProvider  } from '@material-ui/core/styles';
import        { wrapper           } from '../store';
import Head        from 'next/head'
import CssBaseline from '@material-ui/core/CssBaseline';
import theme       from '../themes/red';


const App: FC<AppProps> = ({Component, pageProps}) => (
  <MuiThemeProvider theme={theme}>
    <Head>
      <meta charSet="utf-8" />
      <meta name="viewport" content="minimum-scale=1, initial-scale=1, width=device-width, shrink-to-fit=no" />
      <meta name="theme-color" content={theme.palette.primary.main} />
      <title>MutadUI</title>
    </Head>
    <CssBaseline />
    <Component {...pageProps} />
  </MuiThemeProvider>
);

export default wrapper.withRedux(App);
