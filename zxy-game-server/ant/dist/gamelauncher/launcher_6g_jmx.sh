IP=`ifconfig  | grep 'inet addr:'| grep -v '127.0.0.1' | cut -d: -f2 | awk '{ print $1}'`;
APP_NAME="-DappName=${PWD##*/}";
ARGS="-Djava.rmi.server.hostname=$IP -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
java -server -jar -Xmx6g -Xms6g -Xmn2g -XX:ParallelGCThreads=8 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+PrintGCDetails $ARGS $APP_NAME lib/zxy-game-server.jar
