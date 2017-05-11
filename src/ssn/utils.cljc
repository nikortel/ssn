(ns ssn.utils
  (:require [clojure.string :as string]))

(defn str->int
  "Takes in a string and converts it to an integer"
  [text]
  #?(:clj  (java.lang.Integer/parseInt text)
     :cljs (js/parseInt text)))

(defn zero-pad
  "Converts value to a string and pads it to length with leading zeros"
  ([length value]
   (if (> 1 length)
     (str value)
     (str (string/join (take (- length (count (str value))) (repeat "0"))) value))))
