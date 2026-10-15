(ns com.team766.bearscouts.logging
  "Logger for Bearscouts server")

(def ^:private verbose? (atom false))

(defn set-verbose! [v]
  (reset! verbose? (boolean v)))

(defn log* [ns-name & msgs]
  (when @verbose?
    (apply println (str ns-name ":") msgs)))

(defmacro log [& msgs]
  `(log* ~(str *ns*) ~@msgs))
