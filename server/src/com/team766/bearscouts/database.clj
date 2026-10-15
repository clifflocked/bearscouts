(ns com.team766.bearscouts.database
  "Database interface for Bearscouts server"
  (:require [next.jdbc :as jdbc]
            [com.team766.bearscouts.logging :as logger]))


(defn with-db [name fun]
  (def ds (jdbc/get-datasource
              {:dbtype "h2" :dbname name}))

  (fun ds))

(defn open-db! [name]
  (def ds (jdbc/get-datasource {:dbtype "sqlite" :dbname name}))
  (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS students (
student_id integer PRIMARY KEY,
name text NOT NULL,
admin integer NOT NULL)"])
  ds)

(defn add-student! [db json]
  (def query [(str "INSERT INTO students VALUES (" (:student-id json) ", '" (:name json) "', 0)")])
  (jdbc/execute! db query))
