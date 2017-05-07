(ns ssn.core
  (:require [ssn.finnish :as ssn])
  (:gen-class))

(defn -main [& args]
  (println "SSN:" (ssn/generate-random-social-security-number)))
