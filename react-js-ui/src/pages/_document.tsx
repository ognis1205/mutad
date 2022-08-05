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
import * as NextDocument from "next/document";
import * as MaterialStyles from "@material-ui/core/styles";
import Document from "next/document";
import * as Themes from "../themes";

export default class Doc extends Document {
  static async getInitialProps(
    ctx: NextDocument.DocumentContext
  ): Promise<NextDocument.DocumentInitialProps> {
    const sheets = new MaterialStyles.ServerStyleSheets();

    const originalRenderPage = ctx.renderPage;

    ctx.renderPage = () =>
      originalRenderPage({
        enhanceApp: (App) => (props) => sheets.collect(<App {...props} />),
      });

    const initialProps = await Document.getInitialProps(ctx);

    return {
      ...initialProps,
      styles: [
        ...React.Children.toArray(initialProps.styles),
        sheets.getStyleElement(),
      ],
    };
  }

  render(): React.ReactElement {
    return (
      <NextDocument.Html lang="en">
        <NextDocument.Head>
          <meta
            name="theme-color"
            content={Themes.defaultTheme.palette.primary.main}
          />
          <link
            rel="stylesheet"
            href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"
          />
        </NextDocument.Head>
        <body>
          <NextDocument.Main />
          <NextDocument.NextScript />
        </body>
      </NextDocument.Html>
    );
  }
}
