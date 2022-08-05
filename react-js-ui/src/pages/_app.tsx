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
import * as React from "react";
import * as NextApp from "next/app";
import * as Material from "@material-ui/core";
import * as MaterialStyles from "@material-ui/core/styles";
import Head from "next/head";
import * as Metadata from "../metadata";
import * as Themes from "../themes";
import Header from "../components/app/Header";
import LeftDrawer from "../components/app/LeftDrawer";
import Page from "../components/app/Page";

const Mutad: React.FC<NextApp.AppProps> = ({ Component, pageProps }) => {
  const [navDrawerOpen, setNavDrawerOpen] = React.useState(false);

  React.useEffect(() => {
    const jssStyles = document.querySelector("#jss-server-side");
    jssStyles?.parentElement?.removeChild(jssStyles);
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
      <MaterialStyles.ThemeProvider theme={Themes.defaultTheme}>
        <Material.CssBaseline />
        <Header
          title={"mutad"}
          github={"https://github.com/ognis1205/mutad"}
          handleChangeNavDrawer={handleChangeNavDrawer}
          navDrawerOpen={navDrawerOpen}
        />
        <LeftDrawer
          navDrawerOpen={navDrawerOpen}
          handleChangeNavDrawer={handleChangeNavDrawer}
          menu={Metadata.contents.menu}
        />
        <Page navDrawerOpen={navDrawerOpen}>
          <Component {...pageProps} />
        </Page>
      </MaterialStyles.ThemeProvider>
    </React.Fragment>
  );
};

export default Mutad;
