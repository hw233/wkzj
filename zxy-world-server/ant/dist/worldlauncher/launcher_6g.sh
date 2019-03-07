#!/bin/sh
java -server -jar -Xmx6g -Xms6g -Xmn2g -XX:ParallelGCThreads=8 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails lib/ztt-world-server.jar