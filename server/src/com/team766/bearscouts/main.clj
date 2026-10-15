(ns com.team766.bearscouts.main
  "Entry point for Bearscouts server"
  (:require [clojure.string :as string]
            [clojure.tools.cli :as cli]
            [com.team766.bearscouts.logging :as logger]
            [com.team766.bearscouts.response :as response]))

(def cli-options
  [["-p" "--port PORT" "HTTP server port number"
    :default 8080
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-r" "--run" "Run HTTP server"]
   [nil  "--db" "Database file to use"
    :default "bearscouts.db"]
   ["-v" "--verbose" "Log verbosity"]
   ["-h" "--help" "Show this help"]])

(defn exit [msg]
  (println msg)
  (System/exit 0))

(defn make-error [errors]
  (str "Error occurred while parsing arguments:\n\n"
       (string/join \newline errors)))

(defn make-usage [summary]
  (->> ["Bearscouts HTTP server"
        ""
        "Options:"
        summary
        ""
        "Refer to README for more information."]
       (string/join \newline)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]}
        (cli/parse-opts args cli-options)]
    (logger/set-verbose! (:verbose options))
    (cond ; Parse CLI args, show appropriate error or usage
      (:help options) (exit (make-usage summary))
      errors (exit (make-error errors))
      (:run options) (response/run (:port options) (:db options))
      :else (exit (make-usage summary)))))
      
