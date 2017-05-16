(ns ssn.utils
  (:require [clojure.string :as string]
            #?(:clj  [clojure.spec.alpha :as s]
               :cljs [cljs.spec.alpha :as s])
            #?(:clj  [clojure.spec.gen.alpha :as gen]
               :cljs [cljs.spec.gen.alpha :as gen])))

(defn str->int
  "Takes in a string and converts it to an integer"
  [text]
  #?(:clj  (java.lang.Integer/parseInt text)
     :cljs (js/parseInt text)))

(defn zero-pad
  "Converts value to a string and pads it to length with leading zeros"
  [length value]
  (let [string-value (str value)
        value-length (count string-value)]
    (if (> value-length length)
      (str string-value)
      (str (apply str (take (- length value-length) (repeat "0"))) string-value))))

(defn rand-int-in-range
  "Returns a random integer between min (inclusive) and max (exclusive)."
  [min max]
  {:pre [(int? min)
         (int? max)
         (<= min max)]}
  (+ min (rand-int (- max min))))

(defn filter-flatten-last
  "Takes in sequence, applies the filter, flattens the result and returns last"
  [filter-fn seq]
  (->> seq
       (filter filter-fn)
       flatten
       last))

;;Defines safe positive integer range that will not cause overflow
(s/def ::positive-integer (s/int-in 0 #?(:clj  (Integer/MAX_VALUE)
                                         :cljs js/Number.MAX_VALUE)))
(s/fdef rand-int-in-range
        :args (s/and
               (s/cat :min ::positive-integer
                      :max ::positive-integer)
               #(<= (:min %) (:max %)))
        :ret int?
        :fn #(<= (-> % :args :min)
                 (:ret %)
                 (-> % :args :max)))

(s/def ::number-or-string (s/or :number number?
                                :string string?))

;;Defines a reasonable positive integer
;;for values that cause performance issues when too large
(s/def ::reasonable-positive-integer (s/int-in 0 100))

(s/fdef zero-pad
        :args (s/cat :length ::reasonable-positive-integer
                     :value ::number-or-string)
        :ret string?
        :fn #(>= (-> % :ret count)
                 (-> % :args :length)))

(s/def ::numeric-string
  (s/with-gen
    (s/and string?
           #(re-matches #"^\d+$" %))
    #(gen/return (str (rand-int-in-range 0 #?(:clj  (Integer/MAX_VALUE)
                                              :cljs js/Number.MAX_VALUE))))))
(s/fdef str->int
        :args (s/cat :text ::numeric-string)
        :ret int?)
