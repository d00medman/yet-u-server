(ns catalog.config)
(def db
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname "//localhost:5432/yetu"
   :user "postgres"
   :password "password"})