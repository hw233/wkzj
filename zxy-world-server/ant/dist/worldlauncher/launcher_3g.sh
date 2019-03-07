#!/bin/sh
java -server -jar -Xmx3g -Xms3g -Xmn1g -XX:ParallelGCThreads=8 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails lib/ztt-world-server.jar