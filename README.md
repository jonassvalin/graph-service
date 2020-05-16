Graph-service
====================
Generated using leiningen

### To run the tests 
First start a neo4j version 3.5 database using Docker:

```
docker run \
     --name testneo4j \
     -p7474:7474 -p7687:7687 \
     -d \
     -v $HOME/neo4j/data:/data \
     -v $HOME/neo4j/logs:/logs \
     -v $HOME/neo4j/import:/var/lib/neo4j/import \
     -v $HOME/neo4j/plugins:/plugins \
     --env NEO4J_AUTH=neo4j/test \
     neo4j:3.5
```

Then run tests by running `lein test`
