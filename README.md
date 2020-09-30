### Yet U - Server

A simple crud server written in clojure for yet analytics

### Setup
1. This assumes that you have a local instance of postgres running (if not, you can run `docker run -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_DB=yetu -e POSTGRES_PASSWORD=password postgres:alpine` to spin up the DB).
2. Using the psql command line tool, run the scripts in migrate.sql to create the schema and seed the table with the data specified in the requirements.
3. The API itself can be accessed with `lein run`

Please do not hesitate to contact me if any of this set up proves problematic