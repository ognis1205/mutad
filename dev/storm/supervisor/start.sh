#!/bin/bash

nohup storm supervisor &
status=$?
if [ $status -ne 0 ]; then
  echo "failed to start supervisor: $status"
  exit $status
fi

while sleep 60; do
  ps aux | grep "java -server -Ddaemon.name=supervisor" | grep -q -v grep
  supervisor_status=$?
  if [ $supervisor_status -ne 0 ]; then
    echo "storm supervisor is temporarily down."
#    exit 1
  fi
done