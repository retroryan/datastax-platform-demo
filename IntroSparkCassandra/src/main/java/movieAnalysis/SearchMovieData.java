package movieAnalysis;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.datastax.spark.connector.japi.SparkContextJavaFunctions;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.api.java.JavaSQLContext;
import org.apache.spark.sql.api.java.JavaSchemaRDD;
import sparkUtils.SparkConfSetup;

import java.io.IOException;
import java.io.Serializable;

/**
 * dse spark-submit --class movieAnalysis.SearchMovieData --executor-memory 2G ./target/IntroSparkCassandra-0.1.jar '*Death*' 'Drama'
 */
public class SearchMovieData implements Serializable {

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

        JavaRDD<MovieData> movieDataRDD = SearchMovieData.readMovieData(javaSparkContext);
        long moviesCount = movieDataRDD.count();
        System.out.println("found moviesCount = " + moviesCount);

        javaSparkContext.stop();
    }

    private static JavaRDD<MovieData> readMovieData(JavaSparkContext javaSparkContext) {

        SparkContextJavaFunctions sparkContextJavaFunctions = CassandraJavaUtil.javaFunctions(javaSparkContext);

        JavaRDD<MovieData> movieDataRDD = sparkContextJavaFunctions
                .cassandraTable("movie_db", "movies", CassandraJavaUtil.mapRowTo(MovieData.class));

        return movieDataRDD;
    }


}
