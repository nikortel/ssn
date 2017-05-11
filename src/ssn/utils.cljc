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

(defn rand-int-in-range
  "Returns a random integer between min (inclusive) and max (exclusive)."
  [min max]
  {:pre [(integer? min)
	 (integer? max)
	 (<= min max)]}
  (+ min (rand-int (- max min))))

(defn positive-single-digit-or-zero?
  "Determines if the number is a positive single digit number or zero"
  [number]
  (and (integer? number) (< number 10) (>= number 0)))
