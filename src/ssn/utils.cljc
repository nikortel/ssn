(ns ssn.utils)

(defn str->int
  "Takes in a string and converts it to an integer"
  [text]
  #?(:clj  (java.lang.Integer/parseInt text)
     :cljs (js/parseInt text)))
