## DataStax Enterprise Platform Workshop

![](http://www.datastax.com/wp-content/themes/datastax-2014-08/images/common/logo.png)

Ryan Knight
[DataStax](http://datastax.com)
[@knight_cloud](https://twitter.com/knight_cloud)

## Setup Instructions

The workshop requires the following software:
* Java 8
* Install [Maven](https://maven.apache.org/download.cgi)
* Install DataStax Enterprise 4.7

### Install & configure DSE

* Download the latest [DataStax Enterprise 4.6 Release](www.datastax.com/downloads)
  * There are a number of different install options inculding a virtual machine or plain tarball install.
  * A tarball can be downloaded from the [Enterprise Downloads](http://downloads.datastax.com/enterprise/)
* Create a single node cluster either on your local laptop, a virtual machine or a cloud server.
  * A tarball install is as simple as unzipping it and running DSE.
* If you are running in the cloud (like Amazon EC2 or GCE) in Cassandra.yaml set the IP addresses:
  * Listener address: 127.0.0.1
  * RPC address: 127.0.0.1
* Ensure that user that runs DSE has write permissions to the following directories.  (These are the default directories used by a tarball install, a GUI install uses different directories):
  * Cassandra data files /var/lib/cassandra
  * Spark data file location to /var/lib/spark
  * Set the log file locations with correct write permissions:
  * Cassandra log files /var/log/cassandra
  * Spark log files /var/log/spark
* Running DSE from a terminal window with Spark enabled (tarball install):
  * cd to the dse directory (i.e. cd ~/dse-4.7.0)
  * run DSE with Spark Enabled:  bin/dse cassandra -k
* Running DSE with Spark from a package install [see these docs](http://docs.datastax.com/en/datastax_enterprise/4.6/datastax_enterprise/spark/sparkStart.html)
* Verify cqlsh is running.  In a separate terminal window:
  * cd to the dse directory (i.e. cd ~/dse-4.7.0)
  * run cqlsh:   bin/cqlsh localhost
* Verify the Spark Master is up by going to [url of the spark master](http://localhost:7080/)

## Next Steps

* [Intro to CQL and Cassandra Data Modeling](IntroCassandra/STOCK_CQL.md)
* [Intro to Cassandra and Java](CassandraJava/README.md)
* [Intro to Spark and Cassandra](IntroSparkCassandra/README.md)

## Attributions

The Cassandra Retail demo was copied from Steve Lownethal
[Retail Demo](https://github.com/slowenthal/retail)
