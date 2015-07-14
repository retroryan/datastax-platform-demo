#Cassandra and Java Exercises

The goal of this exercise is to run the Java Program with Maven.  The application creates a Cluster Connection to Cassandra.

## Run the Java Program - Loading the Movie Lens Data into Cassandra


* Download the movielens 10 million ratings data set from http://grouplens.org/datasets/movielens/

* copy the movie data into a directory with no spaces!!  The directory is configured in pom.xml which gets messed up with spaces.

* Create the schema for the movie data using the cql in conf/movie_db.cql

* java -jar DataStaxWorkshop-0.1-jar-with-dependencies.jar ml-10M100K/movies.dat node0

* Add the code in load Data to save the data by first creating a prepared insert statement and then in saveMovieData using the prepared statement.  An insert uses the following syntax:

  INSERT INTO sample_ks.messages1 (body,dt,ch,cu,sent) VALUES (?, ?, ?);

* Modify the pom.xml configuration main class parameters to point to the correct directory where you have download the data and run:

`mvn install exec:java`

* Use cqlsh to verify the data was loaded
