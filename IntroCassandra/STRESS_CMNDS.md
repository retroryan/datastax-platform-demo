Run cassandra-stress to populate the cluster with 50,000 partitions using 1 client thread and without any warmup.

```
cassandra-stress write n=50000 no-warmup -rate threads=1
nodetool flush
nodetool status
```

find the tables used by cassandra stress using cqlsh and describing Keyspace1

`cassandra-stress read n=50000 no-warmup -rate threads=1 -node node0`

http://www.datastax.com/dev/blog/improved-cassandra-2-1-stress-tool-benchmark-any-schema
http://docs.datastax.com/en/cassandra/2.1/cassandra/tools/toolsCStressOutput_c.html



Use cassandra-stress to write 50,000 partitions with no warmup, a single client thread, and a replication factor of 3.

```
cqlsh  
DROP KEYSPACE IF EXISTS "Keyspace1";

nodetool flush

￼cassandra-stress write n=50000 no-warmup -rate threads=1 \ -schema replication\(factor=3\) -node node0

nodetool flush

cassandra-stress read n=50000 no-warmup -rate threads=1 -node node0
```
