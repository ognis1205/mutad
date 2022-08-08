#!/bin/bash

nohup storm dev-zookeeper &
status=$?
if [ $status -ne 0 ]; then
  echo "failed to start dev-zookeeper: $status"
  exit $status
fi

nohup storm nimbus &
status=$?
if [ $status -ne 0 ]; then
  echo "failed to start nimbus: $status"
  exit $status
fi

nohup storm supervisor &
status=$?
if [ $status -ne 0 ]; then
  echo "failed to start supervisor: $status"
  exit $status
fi

nohup storm drpc &
status=$?
if [ $status -ne 0 ]; then
  echo "failed to start drpc: $status"
  exit $status
fi

nohup storm ui &
status=$?
if [ $status -ne 0 ]; then
  echo "failed to start ui: $status"
  exit $status
fi

while sleep 60; do
  ps aux | grep "java -server -Ddaemon.name=dev_zookeeper" | grep -q -v grep
  zookeeper_status=$?
  ps aux | grep "java -server -Ddaemon.name=nimbus" | grep -q -v grep
  nimbus_status=$?
  ps aux | grep "java -server -Ddaemon.name=supervisor" | grep -q -v grep
  supervisor_status=$?
  ps aux | grep "java -server -Ddaemon.name=drpc" | grep -q -v grep
  drpc_status=$?
  ps aux | grep "java -server -Ddaemon.name=ui"     | grep -q -v grep
  ui_status=$?

  if [ $zookeeper_status -ne 0 ]; then
    echo "storm dev-zookeeper is temporarily down."
  fi
  if [ $nimbus_status -ne 0 ]; then
    echo "storm nimbus is temporarily down."
  fi
  if [ $supervisor_status -ne 0 ]; then
    echo "storm supervisor is temporarily down."
  fi
  if [ $drpc_status -ne 0 ]; then
    echo "storm drpc is temporarily down."
  fi
  if [ $ui_status -ne 0 ]; then
    echo "storm ui is temporarily down."
  fi
done
