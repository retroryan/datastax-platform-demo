package movieAnalysis;

import com.datastax.driver.core.Session;
import com.datastax.spark.connector.cql.CassandraConnector;
import com.datastax.spark.connector.japi.CassandraJavaUtil;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import sparkUtils.SparkConfSetup;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * dse hadoop fs -copyFromLocal /arcadia/ml-10M100K/ratings.dat /ml-10M100K/ratings.dat
 * dse hadoop fs -ls /ml-10M100K
 * dse spark-submit --class movieAnalysis.LoadMovieData ./target/IntroSparkCassandra-0.1.jar true
 */
public class LoadMovieData implements Serializable {

    public static String DATA_FILE_DIR = "/ml-10M100K/";
    public static String USER_RATINGS_DAT = "ratings.dat";
    public static String MOVIE_DAT = "movies.dat";

    public static void main(String[] args) throws IOException {

        Boolean limitLoad = false;
        if (args.length > 0) {
            limitLoad = Boolean.valueOf(args[0]);
            System.out.println("limitLoad = " + limitLoad);
        }

        JavaSparkContext javaSparkContext = SparkConfSetup.getJavaSparkContext();

        CassandraConnector connector = SparkConfSetup.getCassandraConnector();
        initCassandra(connector);

        JavaRDD<MovieData> movieDataJavaRDD = LoadMovieData.loadMovieData(javaSparkContext);

        if (limitLoad)
            LoadMovieData.loadLimitedRatingsData(javaSparkContext, movieDataJavaRDD);
        else
            LoadMovieData.loadRatingsData(javaSparkContext);

        javaSparkContext.stop();
    }


    private static void initCassandra(CassandraConnector connector) {

        try (Session session = connector.openSession()) {
            session.execute("TRUNCATE movie_db.rating_by_movie");
            session.execute("TRUNCATE movie_db.movies");
        }
    }

    private static JavaRDD<RatingData> loadRatingsData(JavaSparkContext javaSparkContext) {
        JavaRDD<String> rawRatingsData = javaSparkContext.textFile(DATA_FILE_DIR + USER_RATINGS_DAT);

        JavaRDD<RatingData> ratingDataRDD = rawRatingsData
                .map(nxtLine -> processRatingsDataLine(nxtLine))
                .filter(nxtOptData -> nxtOptData.isPresent())
                .map(nxtOptData -> nxtOptData.get());

        ratingDataRDD.take(20).forEach(r -> System.out.println("r = " + r));
        long count = ratingDataRDD.count();
        System.out.println("count = " + count);

        CassandraJavaUtil.javaFunctions(ratingDataRDD)
                .writerBuilder("movie_db", "rating_by_movie", CassandraJavaUtil.mapToRow(RatingData.class))
                .saveToCassandra();

        return ratingDataRDD;
    }

    private static JavaRDD<RatingData> loadLimitedRatingsData(JavaSparkContext javaSparkContext,JavaRDD<MovieData> movieDataRDD ) {
        JavaRDD<String> rawRatingsData = javaSparkContext.textFile(DATA_FILE_DIR + USER_RATINGS_DAT);

        List<MovieData> movieDataList = movieDataRDD.take(100);
        List<Integer> movieIdList = movieDataList.stream().map(movie -> movie.getMovie_id()).collect(Collectors.toList());


        JavaRDD<RatingData> ratingDataRDD = rawRatingsData
                .map(nxtLine -> processRatingsDataLine(nxtLine))
                .filter(nxtOptData -> keepRatingData(movieIdList, nxtOptData))
                .map(nxtOptData -> nxtOptData.get());

        ratingDataRDD.take(20).forEach(r -> System.out.println("r = " + r));
        long count = ratingDataRDD.count();
        System.out.println("count = " + count);

        CassandraJavaUtil.javaFunctions(ratingDataRDD)
                .writerBuilder("movie_db", "rating_by_movie", CassandraJavaUtil.mapToRow(RatingData.class))
                .saveToCassandra();

        return ratingDataRDD;
    }

    private static boolean keepRatingData(List<Integer> movieIdList, Optional<RatingData> nxtOptData) {
        return nxtOptData.isPresent() && (movieIdList.contains(nxtOptData.get().getMovie_id()));
    }


    private static Optional<RatingData> processRatingsDataLine(String line) {
        Optional<RatingData> optRatingData = Optional.empty();

        if (line != null && line.length() > 0) {
            String[] split = line.split("::");
            if (split.length == 4) {
                int user_id = Integer.parseInt(split[0]);
                int movie_id = Integer.parseInt(split[1]);
                float rating = Float.parseFloat(split[2]);
                int timestamp = Integer.parseInt(split[3]);

                RatingData ratingData = new RatingData(user_id, movie_id, rating, timestamp);
                optRatingData = Optional.of(ratingData);
            }
        }
        return optRatingData;
    }

    private static JavaRDD<MovieData> loadMovieData(JavaSparkContext javaSparkContext) {
        JavaRDD<String> rawMovieData = javaSparkContext.textFile(DATA_FILE_DIR + MOVIE_DAT);

        JavaRDD<MovieData> movieDataRDD = rawMovieData
                .map(nxtLine -> processMovieDataLine(nxtLine))
                .filter(nxtOptData -> nxtOptData.isPresent())
                .map(nxtOptData -> nxtOptData.get());

        movieDataRDD.take(20).forEach(r -> System.out.println("r = " + r));
        long count = movieDataRDD.count();
        System.out.println("count = " + count);

        CassandraJavaUtil.javaFunctions(movieDataRDD)
                .writerBuilder("movie_db", "movies", CassandraJavaUtil.mapToRow(MovieData.class))
                .saveToCassandra();

        return movieDataRDD;
    }

    private static Optional<MovieData> processMovieDataLine(String line) {
        Optional<MovieData> optMovieData = Optional.empty();

        if (line != null && line.length() > 0) {
            String[] split = line.split("::");
            if (split.length == 3) {
                int id = Integer.parseInt(split[0]);
                String title = split[1];
                String[] categoryArray = split[2].split("\\|");
                Set<String> categories = null;
                if (categoryArray.length > 0)
                    categories = new HashSet<>(Arrays.asList(categoryArray));

                MovieData movieData = new MovieData(id, title, categories);
                optMovieData = Optional.of(movieData);
            }
        }
        return optMovieData;
    }

}
