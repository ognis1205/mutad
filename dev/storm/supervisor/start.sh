#!/bin/bash

# Start the Supervisor
nohup storm supervisor &
status=$?
if [ $status -ne 0 ]; then
  echo "failed to start supervisor: $status"
  exit $status
fi

while sleep 60; do
  ps aux | grep "java -server -Ddaemon.name=supervisor" | grep -q -v grep
  SU_STATUS=$?
  if [ $SU_STATUS -ne 0 ]; then
    echo "the processes has already exited."
    exit 1
  fi
done