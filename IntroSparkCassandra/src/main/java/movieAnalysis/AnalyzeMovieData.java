package movieAnalysis;

import com.datastax.spark.connector.cql.CassandraConnector;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.SparkContextJavaFunctions;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import scala.Tuple2;
import sparkUtils.SparkConfSetup;

import java.io.IOException;
import java.io.Serializable;

/**
 * dse spark-submit --class movieAnalysis.AnalyzeMovieData --executor-memory 2G ./target/IntroSparkCassandra-0.1.jar '*Death*' 'Drama'
 */
public class AnalyzeMovieData implements Serializable {

    public static String DATA_FILE_DIR = "/ml-10M100K/";
    public static String USER_RATINGS_DAT = "ratings.dat";

    public static void main(String[] args) throws IOException {

        String movieSearch = "";
        String movieCategory = "";
        if (args.length > 1) {
            movieSearch = args[0];
            System.out.println("movieSearch = " + movieSearch);
            movieCategory = args[1];
            System.out.println("movieCategory = " + movieCategory);
        }

        JavaSparkContext javaSparkContext = SparkConfSetup.getJavaSparkContext();

        JavaRDD<MovieData> movieDataRDD = AnalyzeMovieData.readMovieData(javaSparkContext);
        long moviesCount = movieDataRDD.count();
        System.out.println("found moviesCount = " + moviesCount);


        JavaRDD<RatingData> ratingDataRDD = AnalyzeMovieData.readRatingsData(javaSparkContext);
        long ratingsCount = ratingDataRDD.count();
        System.out.println("ratingsCount = " + ratingsCount);

        demoSparkSql(javaSparkContext, ratingDataRDD);


        javaSparkContext.stop();
    }

    private static void demoSparkSql(JavaSparkContext javaSparkContext, JavaRDD<RatingData> ratingDataRDD) {
        JavaSQLContext sqlContext = new JavaSQLContext(javaSparkContext);

        JavaSchemaRDD ratingDataSchemaRDD =   sqlContext.applySchema(ratingDataRDD, RatingData.class);
        sqlContext.registerRDDAsTable(ratingDataSchemaRDD, "ratings");

        JavaSchemaRDD schemaRDD = sqlContext.sql("select movie_id, avg(rating) as rating from ratings group by movie_id");
        schemaRDD.take(100).forEach(r -> System.out.println("r = " + r));

        // If you want to use the results for additional queries use:
        // schemaRDD.registerTempTable("average_ratings");

    }

    private static JavaRDD<MovieData> readMovieData(JavaSparkContext javaSparkContext) {

        SparkContextJavaFunctions sparkContextJavaFunctions = CassandraJavaUtil.javaFunctions(javaSparkContext);

        JavaRDD<MovieData> movieDataRDD = sparkContextJavaFunctions
                .cassandraTable("movie_db", "movies", CassandraJavaUtil.mapRowTo(MovieData.class));

        return movieDataRDD;
    }

    private static JavaRDD<RatingData> readRatingsData(JavaSparkContext javaSparkContext) {

        SparkContextJavaFunctions sparkContextJavaFunctions = CassandraJavaUtil.javaFunctions(javaSparkContext);

        JavaRDD<RatingData> ratingDataRDD = sparkContextJavaFunctions
                .cassandraTable("movie_db", "rating_by_movie", CassandraJavaUtil.mapRowTo(RatingData.class));

        ratingDataRDD.take(20).forEach(r -> System.out.println("r = " + r));

        return ratingDataRDD;
    }


}
