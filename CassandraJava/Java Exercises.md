#Cassandra and Java Exercises

The goal of this exercise is to run the Java Program with Maven.  The application creates a Cluster Connection to Cassandra.

## Run the Java Program - Loading the Movie Lens Data into Cassandra

* Download the movielens 10 million ratings data set from http://grouplens.org/datasets/movielens/

* Modify the LoadData file to point to the correct directory where you have download the data and run:

`mvn install exec:java`
