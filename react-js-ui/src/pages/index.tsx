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
import { useState } from "react";
import Head from "next/head";
import Link from "next/link";
import { useTheme, makeStyles, Theme } from "@material-ui/core/styles";
import {
  Typography,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
} from "@material-ui/core";

const useStyle = makeStyles({
  root: (props: Theme) => ({
    paddingTop: props.spacing(10),
    paddingLeft: props.spacing(5),
    paddingRight: props.spacing(5),
  }),
});

export default function Home() {
  const [dialogOpen, setDialogOpen] = useState(true);
  const classes = useStyle(useTheme());
  return (
    <div className={classes.root}>
      <Head>
        <title>My page title</title>
        <meta name="viewport" content="initial-scale=1.0, width=device-width" />
        <link
          rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"
        />
      </Head>
      <Dialog
        open={dialogOpen}
        onClose={() => {
          setDialogOpen(false);
        }}
      >
        <DialogTitle>Dialog Sample</DialogTitle>
        <DialogContent>
          <DialogContentText>Easy to use Material UI Dialog.</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            color="primary"
            onClick={() => {
              setDialogOpen(false);
            }}
          >
            OK
          </Button>
        </DialogActions>
      </Dialog>
      <Typography variant="h1" gutterBottom={true}>
        Material-UI
      </Typography>
      <Typography variant="subtitle1" gutterBottom={true}>
        example project
      </Typography>
      <Typography gutterBottom={true}>
        <Link href="/about">
          <a>Go to the about page</a>
        </Link>
      </Typography>
      <Button
        variant="contained"
        color="secondary"
        onClick={() => {
          setDialogOpen(true);
        }}
      >
        Shot Dialog
      </Button>
      <style jsx={true}>{`
        .root {
          text-align: center;
        }
      `}</style>
    </div>
  );
}
