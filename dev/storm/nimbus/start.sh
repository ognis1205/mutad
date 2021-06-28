#!/bin/bash

nohup storm nimbus &
status=$?
if [ $status -ne 0 ]; then
  echo "failed to start nimbus: $status"
  exit $status
fi

nohup storm ui &
status=$?
if [ $status -ne 0 ]; then
  echo "failed to start ui: $status"
  exit $status
fi

while sleep 60; do
  ps aux | grep "java -server -Ddaemon.name=nimbus" | grep -q -v grep
  NI_STATUS=$?
  ps aux | grep "java -server -Ddaemon.name=ui"     | grep -q -v grep
  UI_STATUS=$?
  if [ $NI_STATUS -ne 0 -o $UI_STATUS -ne 0 ]; then
    echo "the processes has already exited."
    exit 1
  fi
done