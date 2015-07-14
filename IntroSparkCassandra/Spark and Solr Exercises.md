## Spark and Solr Exercises with the Movie Lens Data

* The template class to load and analyze the Movie Lens Data in Load Move Lens Data.  The true flag limits the raitings data so only the raitings on the first 100 movies is loaded to prevent OOM errors. It can be run with:

`dse spark-submit --class movieAnalysis.LoadMovieData ./target/IntroSparkCassandra-0.1.jar true`

* Search and Analytics can be combined to find and analyze specific data.  In this exercise we will use search for verses that contain specific words and then count the number of words in that verse.

* Data can be filtered using a solr quer by adding a where clause to the function that loads the data.  For example:

```
        sparkContextJavaFunctions
              .cassandraTable("search_demo", "verses", mapRowTo(MovieData.class))
              .where("solr_query='title:*Death* AND category:Drama'");
```

* Modify the class SearchMovieData method readMovieData to search for specific movies and then run AnalyzeMovieData with:

`dse spark-submit --class movieAnalysis.LoadMovieData ./target/IntroSparkCassandra-0.1.jar true`

*  Find the average of the movies use Spark SQL

`dse spark-submit --class movieAnalysis.AnalyzeMovieData --executor-memory 2G ./target/IntroSparkCassandra-0.1.jar`

