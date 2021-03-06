(ns ssn.finnish
  (:require [ssn.utils :as utils]
            #?(:clj  [clojure.spec.alpha :as s]
               :cljs [cljs.spec.alpha :as s])
            #?(:clj  [clojure.spec.gen.alpha :as gen]
               :cljs [cljs.spec.gen.alpha :as gen])))

(def check-marks [[10 "A"] [11 "B"] [12 "C"] [13 "D"] [14 "E"] [15 "F"] [16 "H"]
                  [17 "J"] [18 "K"] [19 "L"] [20 "M"] [21 "N"] [22 "P"] [23 "R"]
                  [24 "S"] [25 "T"] [26 "U"] [27 "V"] [28 "W"] [29 "X"] [30 "Y"]])

(def century-symbols [["18" "+"] ["19" "-"] ["20" "A"]])

(def finnish-ssn-format #"^(0[1-9]|[12]\d|3[01])(0[1-9]|1[0-2])([5-9]\d\+|\d\d[-A])\d{3}[\dA-Z]$")
(def person-number-length 3)
(def birthdate-length 6)
(def check-mark-mod 31)
(def zero-pad-length-2 (partial utils/zero-pad 2))
(def zero-pad-length-3 (partial utils/zero-pad 3))

(defn person-number
  [social-security-number]
  (->> (drop-last social-security-number)
       (take-last person-number-length)
       (apply str)))

(defn check-mark-base
  "Takes in Finnish social security number and returns base for check mark calculation"
  [social-security-number]
  (->> (person-number social-security-number)
       (concat (take birthdate-length social-security-number))
       (apply str)
       utils/str->int))

(defn convert-check-mark
  "Converts given number to a corresponding check mark"
  [check-mark-number]
  (->> check-marks
       (utils/filter-flatten-last #(= (first %) check-mark-number))))

(defn check-mark
  "Takes in chack mark base (birthdate and person number as integer) from social security number and returns the correct check mark for it"
  [check-mark-base]
  (let [check-mark-number (mod check-mark-base check-mark-mod)]
       (if (<= 0 check-mark-number 9)
         (str check-mark-number)
         (convert-check-mark check-mark-number))))

(def social-security-number->check-mark (comp check-mark check-mark-base))

(defn valid-format?
  "Validates the generic format of a finnish social security number"
  [social-security-number]
  (and (string? social-security-number)
       (re-matches finnish-ssn-format social-security-number)))

(defn check-mark-valid?
  "Validates that the check mark in the social security number is correct"
  [social-security-number]
  (let [calculated-check-mark (social-security-number->check-mark social-security-number)
        actual-check-mark (str (last social-security-number))]
    (= calculated-check-mark actual-check-mark)))

(defn person-number-valid?
  [social-security-number]
  (let [person-number-integer (utils/str->int (person-number social-security-number))]
    (<= 2 person-number-integer 999)))

(defn generate-person-number
  "Generates person identification code from 002 (smallest possible real person identification number) to 999.
  Returns odd numbers for :male gender and even numbers for :female."
  [gender]
  (let [person-number (utils/rand-int-in-range 2 999)
        gender-fixed-person-number (case gender
                                     :male (if (odd? person-number) person-number (+ 1 person-number))
                                     :female (if (even? person-number) person-number (+ 1 person-number)))]
    (zero-pad-length-3 gender-fixed-person-number)))

(defn century-symbol
  [year]
  (let [century (apply str (take 2 (str year)))]
    (->> century-symbols
         (utils/filter-flatten-last #(= (first %) century)))))

(defn generate-social-security-number
  "Takes in data in spec ::person and produces valid social security number"
  [person]
  (let [day-padded (zero-pad-length-2 (::day person))
        month-padded (zero-pad-length-2 (::month person))
        century-symbol (century-symbol (::year person))
        year-without-century (apply str (take-last 2 (str (::year person))))
        person-number (generate-person-number (::gender person))
        check-mark-base (-> (str day-padded
                                 month-padded
                                 year-without-century
                                 person-number)
                            (utils/str->int))
        check-mark (check-mark check-mark-base)]
    (str day-padded
         month-padded
         year-without-century
         century-symbol
         person-number
         check-mark)))

(def generate-random-social-security-number
  (comp generate-social-security-number gen/generate (partial s/gen ::person)))

(s/def ::social-security-number
  (s/with-gen
    (s/and valid-format?
           check-mark-valid?
           person-number-valid?)
    #(gen/return (generate-random-social-security-number))))

(s/def ::day (s/int-in 1 32))
(s/def ::month (s/int-in 1 13))
(s/def ::year (s/int-in 1850 2100))
(s/def ::gender #{:male :female})
(s/def ::person (s/keys :req [::day ::month ::year ::gender]))

(s/fdef generate-social-security-number
        :args (s/cat :person ::person)
        :ret string?
        :fn #(s/valid? ::social-security-number (:ret %)))
