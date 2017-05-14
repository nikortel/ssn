(ns ssn.utils
  (:require [clojure.string :as string]
            #?(:clj  [clojure.spec.alpha :as s]
               :cljs [cljs.spec.alpha :as s])))

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
  {:pre [(int? min)
	 (int? max)
	 (<= min max)]}
  (+ min (rand-int (- max min))))

(s/fdef rand-int-in-range
        :args (s/and (s/cat :min (s/int-in 0 #?(:clj  (Integer/MAX_VALUE)
                                                :cljs (js/Number.MAX_SAFE_INTEGER)))
                            :max (s/int-in 1 #?(:clj  (Integer/MAX_VALUE)
                                                :cljs (js/Number.MAX_SAFE_INTEGER))))
                     #(<= (:min %) (:max %)))
        :ret int?
        :fn #(<= (-> % :args :min)
                 (:ret %)
                 (-> % :args :max)))
