(defproject clojure-getting-started "1.0.0-SNAPSHOT"
  :description "Yet Interview back end"
  :url "http://clojure-getting-started.herokuapp.com"
  :license {:name "Eclipse Public License v1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[io.pedestal/pedestal.service "0.5.7"]
                 [io.pedestal/pedestal.jetty  "0.5.7"]
                 [com.layerware/hugsql  "0.4.9"]
                 [org.clojure/tools.namespace  "0.2.11"]
                 [org.clojure/tools.logging   "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [org.postgresql/postgresql "42.1.4"]]
  :min-lein-version "2.0.0"
  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :main catalog.core
  :aot [catalog.core]
  :uberjar-name "yet-u.jar"
  :profiles {:production {:env {:production true}}})