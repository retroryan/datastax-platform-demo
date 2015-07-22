#!/bin/bash

nodes='54.69.118.192 52.25.57.155 52.27.134.178 52.10.3.86 54.68.249.203 52.27.98.110 52.25.21.191'
for node in $nodes
do
    scp -i /apps/demo_rsa target/IntroSparkCassandra-0.1.jar root@$node:/root
done
