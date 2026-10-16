(ns com.team766.bearscouts.response
  "HTTP request and response handler"
  (:require [aleph.http :as http]
            [clojure.data.json :as json]
            [com.team766.bearscouts.logging :as logger]
            [com.team766.bearscouts.database :as db]))

(defn make-response [status body]
  {:status status
   :headers {"content-type" "application/json"}
   :body body})

(defn bad-request []
  (make-response 400 (json/write-str {:error "Bad Request"})))

(defn handler [db req]
  (case (:uri req)
    "/check-online" (make-response 200 (json/write-str {:status "up"}))
    "/register" (if (nil? (:body req))
                  (bad-request)
                  (let [body (slurp (:body req)) ; :body is java.io.ByteArrayInputStream
                      req-json (json/read-str (if (nil? body) "{}" body) :key-fn keyword)]
                    (if (or (nil? (:name req-json)) (nil? (:student-id req-json)))
                      (bad-request)
                      (do
                        (db/add-student! db req-json)
                        (make-response 200 (json/write-str {:status "successful"}))))))
     (do
       (logger/log (str "Received request for invalid resource: " (:uri req)))
       (make-response 404 (json/write-str {:error "Not Found"})))))
 
(defn run [port dbname]
  (logger/log (str "HTTP server starting using " dbname))
  (def db (db/open-db! dbname))
  (http/start-server (partial handler db) {:port port}))
