#!/bin/sh
APP_NAME="-DappName=${PWD##*/}";
java -server -jar -Xmx8g -Xms8g -Xmn3g -XX:ParallelGCThreads=8 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails $APP_NAME lib/zxy-game-server.jar
