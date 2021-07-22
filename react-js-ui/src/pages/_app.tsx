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
import React, { FC, useState } from "react";
import { AppProps } from "next/app";
import Head from "next/head";
import { ThemeProvider } from "@material-ui/core/styles";
import CssBaseline from "@material-ui/core/CssBaseline";
import { wrapper } from "../state/store";
import Specs from "../specs";
import theme from "../theme";
import Header from "../components/app/Header";
import LeftDrawer from "../components/app/LeftDrawer";
import Page from "../components/app/Page";

const App: FC<AppProps> = ({ Component, pageProps }) => {
  const [navDrawerOpen, setNavDrawerOpen] = useState(false);

  React.useEffect(() => {
    const jssStyles = document.querySelector("#jss-server-side");
    if (jssStyles) jssStyles.parentElement!.removeChild(jssStyles);
  }, []);

  const handleChangeNavDrawer = () => {
    setNavDrawerOpen(!navDrawerOpen);
  };

  return (
    <React.Fragment>
      <Head>
        <title>Mutad UI</title>
        <meta
          name="viewport"
          content="minimum-scale=1, initial-scale=1, width=device-width"
        />
      </Head>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Header
          title={"mutad"}
          github={"https://github.com/ognis1205/mutad"}
          handleChangeNavDrawer={handleChangeNavDrawer}
          navDrawerOpen={navDrawerOpen}
        />
        <LeftDrawer
          navDrawerOpen={navDrawerOpen}
          handleChangeNavDrawer={handleChangeNavDrawer}
          menus={Specs.menus}
        />
        <Page navDrawerOpen={navDrawerOpen}>
          <Component {...pageProps} />
        </Page>
      </ThemeProvider>
    </React.Fragment>
  );
};

export default wrapper.withRedux(App);
