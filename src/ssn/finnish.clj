(ns ssn.finnish
  (:require [clojure.spec.alpha :as s]))

(def check-marks [[10 "A"] [11 "B"] [12 "C"] [13 "D"] [14 "E"] [15 "F"] [16 "H"]
                  [17 "J"] [18 "K"] [19 "L"] [20 "M"] [21 "N"] [22 "P"] [23 "R"]
                  [24 "S"] [25 "T"] [26 "U"] [27 "V"] [28 "W"] [29 "X"] [30 "Y"]])

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
  "Takes in Finnish social security number and returns the checkmark"
  [social-security-number]
  (let [check-mark-number (mod (check-mark-base social-security-number) check-mark-mod)]
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
  (= (str (last social-security-number)) (check-mark social-security-number)))

(s/def ::social-security-number (s/and valid-format?
                                       check-mark-valid?))
