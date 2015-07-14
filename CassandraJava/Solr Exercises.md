## Making the Movie Lens Data Searchable with Solr

* Use the dsetool to create a solr core of the movies:

`dsetool create_core movie_db.movies reindex=true generateResources=true`

* Verify the Solr core was created by browsing to the [Solr Management Interface](http://localhost:8983/solr) and trying some basic queries.

* Experiment with different Solr Queries in the management interface.

* In cqlsh you can also run some queries, i.e.:

`select * from movies where solr_query = 'categories:Drama';`

`select * from movies where solr_query = '{"q":"categories:Drama","facet":{"field":"title"}}';`

`select * from movies where solr_query = '{"q":"categories:*","facet":{"field":"categories"}}';`

For more example queries see the [DSE Search Tutorial](http://docs.datastax.com/en/datastax_enterprise/4.7/datastax_enterprise/srch/srchTutCQL.html)
