(ns ssn.finnish
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

(def check-marks [[10 "A"] [11 "B"] [12 "C"] [13 "D"] [14 "E"] [15 "F"] [16 "H"]
                  [17 "J"] [18 "K"] [19 "L"] [20 "M"] [21 "N"] [22 "P"] [23 "R"]
                  [24 "S"] [25 "T"] [26 "U"] [27 "V"] [28 "W"] [29 "X"] [30 "Y"]])

(def century-symbols [["18" "+"] ["19" "-"] ["20" "A"]])

(def finnish-ssn-format #"^(0[1-9]|[12]\d|3[01])(0[1-9]|1[0-2])([5-9]\d\+|\d\d[-A])\d{3}[\dA-Z]$")
(def person-number-length 3)
(def birthdate-length 6)
(def check-mark-mod 31)

(defn check-mark-base
  "Takes in Finnish social security number and returns base for check mark calculation"
  [social-security-number]
  (->>
   (drop-last social-security-number)
   (take-last person-number-length)
   (concat (take birthdate-length social-security-number))
   (apply str)
   (Integer/parseInt)))

(defn is-positive-single-digit-or-zero?
  "Determines if the number is a positive single digit number or zero"
  [number]
  (and (integer? number) (< number 10) (>= number 0)))

(defn convert-check-mark
  "Converts given number to a corresponding check mark"
  [check-mark-number]
  (->
   (filter #(= (first %) check-mark-number) check-marks)
   (flatten)
   (last)))

(defn check-mark
  "Takes in chack mark base (birthdate and person number as integer) from social security number and returns the correct check mark for it"
  [check-mark-base]
  (let [check-mark-number (mod check-mark-base check-mark-mod)]
       (if (is-positive-single-digit-or-zero? check-mark-number)
         (str check-mark-number)
         (convert-check-mark check-mark-number))))

(defn valid-format?
  "Validates the generic format of a finnish social security number"
  [social-security-number]
  (and (string? social-security-number) (re-matches finnish-ssn-format social-security-number)))

(defn check-mark-valid?
  "Validates that the check mark in the social security number is correct"
  [social-security-number]
  (let [check-mark-base (check-mark-base social-security-number)
        calculated-check-mark (check-mark check-mark-base)
        actual-check-mark (str (last social-security-number))]
    (= calculated-check-mark actual-check-mark)))

(defn generate-person-number
  [gender]
  (let [person-number (rand-int 999)
        gender-fixed-person-number (case gender
                                     :male (if (odd? person-number) person-number (+ 1 person-number))
                                     :female (if (even? person-number) person-number (+ 1 person-number)))]
    (format "%03d" gender-fixed-person-number)))

(defn century-symbol
  [year]
  (let [century (apply str (take 2 (str year)))]
    (->
     (filter #(= (first %) century) century-symbols)
     (flatten)
     (last))))

(defn generate-social-security-number
  "Takes in data in spec ::person and produces valid social security number"
  [person]
  (let [day-padded (format "%02d" (::day person))
        month-padded (format "%02d" (::month person))
        year-without-century (apply str (take-last 2 (str (::year person))))
        person-number (generate-person-number (::gender person))
        check-mark-base (-> (str day-padded
                                 month-padded
                                 year-without-century
                                 person-number)
                            (Integer/parseInt))
        check-mark (check-mark check-mark-base)]
    (str day-padded
         month-padded
         year-without-century
         (century-symbol (::year person))
         person-number
         check-mark)))

(defn generate-random-social-security-number
  []
  (->
   (gen/generate (s/gen ::person))
   (generate-social-security-number)))

(s/def ::social-security-number (s/with-gen
                                  (s/and valid-format?
                                         check-mark-valid?)
                                  #(gen/return (generate-random-social-security-number))))

(s/def ::day (s/int-in 1 32))
(s/def ::month (s/int-in 1 13))
(s/def ::year (s/int-in 1850 2100))
(s/def ::gender #{:male :female})
(s/def ::person (s/keys :req [::day ::month ::year ::gender]))
