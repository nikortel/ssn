(ns ssn.core
  (:require [ssn.finnish :as ssn]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen])
  (:gen-class))

(defn -main [& args]
  (println "010186-566W valid?" (s/valid? :ssn.finnish/social-security-number "010186-566W"))
  (println "SSN:" (gen/generate (s/gen :ssn.finnish/social-security-number)))
  (println "SSN:" (ssn/generate-social-security-number {:ssn.finnish/day 1
                                                        :ssn.finnish/month 1
                                                        :ssn.finnish/year 1986
                                                        :ssn.finnish/gender :female}))
  )
