(ns ssn.core
  (:require [ssn.finnish :as ssn]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen])
  (:gen-class))

(defn -main [& args]
  (println "010186-566W valid?" (s/valid? ::ssn/social-security-number "010186-566W"))
  (println "SSN:" (gen/generate (s/gen ::ssn/social-security-number)))
  (println "SSN:" (ssn/generate-social-security-number {::ssn/day 1
                                                        ::ssn/month 1
                                                        ::ssn/year 1986
                                                        ::ssn/gender :female}))
  )
