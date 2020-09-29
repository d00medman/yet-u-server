(ns catalog.sql.courses
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "catalog/sql/courses.sql")