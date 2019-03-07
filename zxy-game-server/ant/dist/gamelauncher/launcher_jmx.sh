#!/bin/bash
# start server
# author ludd
# creat 2014-6-23
IP=`ifconfig  | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}'`;
APP_NAME="-DappName=${PWD##*/}";
ARGS="-Djava.rmi.server.hostname=$IP -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
java -server -jar $ARGS $APP_NAME lib/zxy-game-server.jar 
