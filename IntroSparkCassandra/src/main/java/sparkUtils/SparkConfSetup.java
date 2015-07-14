package sparkUtils;

import com.datastax.spark.connector.cql.CassandraConnector;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.cassandra.CassandraSQLContext;

public interface SparkConfSetup {

    static SparkConf getSparkConf() {
        return new SparkConf()
                .setAppName("SimpleSpark");
    }

    static JavaSparkContext getJavaSparkContext() {
        SparkContext sparkContext = new SparkContext(getSparkConf());
        return new JavaSparkContext(sparkContext);
    }

    static CassandraConnector getCassandraConnector() {
        return CassandraConnector.apply((getSparkConf()));
    }

}
