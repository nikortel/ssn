(ns ssn.finnish-test
  (:require [ssn.finnish :as ssn]
            [ssn.utils :as utils]
            [clojure.test.check.generators]
            #?(:clj  [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :refer-macros [deftest is testing run-tests]])
            #?(:clj  [clojure.spec.alpha :as s]
               :cljs [cljs.spec :as s])
            #?(:clj  [clojure.spec.gen.alpha :as gen]
               :cljs [cljs.spec.impl.gen :as gen])))

(deftest format-spec-test
  (testing "SSN conforms to format"
    (is (ssn/valid-format? "040597-959K")))
  (testing "Number does not conform to format"
    (is (not (ssn/valid-format? 1))))
  (testing "SSN with birth day number 32 does not conform to format"
    (is (not (ssn/valid-format? "320597-959K"))))
  (testing "Earliest birth date conforms to format"
    (is (ssn/valid-format? "010150+931C")))
  (testing "Birth year before 1850 does not conform to format"
    (is (not (ssn/valid-format? "010149+931C"))))
  (testing "Birth day at 2020 conforms to format"
    (is (ssn/valid-format? "010120A901N"))))

(deftest check-mark-base-test
  (testing "Returns check mark base"
    (is (= 40597959 (ssn/check-mark-base "040597-959K")))))

(deftest is-positive-single-digit-or-zero?-test
  (testing "Returns true when number is positive single digit - min value"
    (is (ssn/is-positive-single-digit-or-zero? 0)))
  (testing "Returns true when number is positive single digit - max value"
    (is (ssn/is-positive-single-digit-or-zero? 9)))
  (testing "Returns false when number is negative single digit"
    (is (not (ssn/is-positive-single-digit-or-zero? -1))))
  (testing "Returns false when number is positive double digit"
    (is (not (ssn/is-positive-single-digit-or-zero? 10))))
  (testing "Returns false when number is positive decimal"
    (is (not (ssn/is-positive-single-digit-or-zero? 1.1)))))

(deftest check-mark-test
  (testing "Returns check mark from check-marks-table"
    (is (= "K" (ssn/check-mark 40597959))))
  (testing "Returns check mark without conversion from table"
    (is (= "3" (ssn/check-mark 40597975)))))

(deftest check-mark-valid?-test
  (testing "Returns true when checkmark is valid"
    (is (ssn/check-mark-valid? "040597-959K")))
  (testing "Returns false when checkmark is not valid"
    (is (not (ssn/check-mark-valid? "040597-959Y")))))

(deftest social-security-number-spec-test
  (testing "1849 social security number is not valid"
    (is (not (s/valid? ::ssn/social-security-number "010149+9635"))))
  (testing "1850 social security number is valid"
    (is (s/valid? ::ssn/social-security-number "010150+931C")))
  (testing "Numbers only social security number is valid"
    (is (s/valid? ::ssn/social-security-number "040597-9753")))
  (testing "Social security number with check mark 0 is valid"
    (is (s/valid? ::ssn/social-security-number "060597-9010"))))

(deftest day-spec-test
  (testing "0 is not a day in social security number"
    (is (not (s/valid? ::ssn/day 0))))
  (testing "1 is a day in social security number"
    (is (s/valid? ::ssn/day 1)))
  (testing "31 is a day in social security number"
    (is (s/valid? ::ssn/day 31)))
  (testing "32 is a day in social security number"
    (is (not (s/valid? ::ssn/day 32)))))

(deftest month-spec-test
  (testing "0 is not a month in social security number"
    (is (not (s/valid? ::ssn/month 0))))
  (testing "1 is a month in social security number"
    (is (s/valid? ::ssn/month 1)))
  (testing "12 is a month in social security number"
    (is (s/valid? ::ssn/month 12)))
  (testing "13 is a month in social security number"
    (is (not (s/valid? ::ssn/month 13)))))

(deftest year-spec-test
  (testing "1849 is not a year in social security number"
    (is (not (s/valid? ::ssn/year 1849))))
  (testing "1850 is a year in social security number"
    (is (s/valid? ::ssn/year 1850)))
  (testing "2099 is a year in social security number"
    (is (s/valid? ::ssn/year 2099)))
  (testing "2100 is a year in social security number"
    (is (not (s/valid? ::ssn/year 2100)))))

(deftest gender-spec-test
  (testing ":male is a gender in social security number"
    (is (s/valid? ::ssn/gender :male)))
  (testing ":female is a gender in social security number"
    (is (s/valid? ::ssn/gender :female)))
  (testing ":other is not a gender in social security number"
    (is (not (s/valid? ::ssn/gender :other)))))

(deftest generate-person-number-test-with-odd-random-value
  (with-redefs [rand-int (fn [max-value] 1)]
    (testing "Generates odd person number for male"
      (is (odd? (utils/str->int (ssn/generate-person-number :male)))))
    (testing "Generates even person number for female"
      (is (even? (utils/str->int (ssn/generate-person-number :female)))))
    (testing "Generates person number with length 3 for male"
      (is (= 3 (count (ssn/generate-person-number :male)))))
    (testing "Generates person number with length 3 for female"
      (is (= 3 (count (ssn/generate-person-number :female)))))))

(deftest generate-person-number-test-with-even-random-value
  (with-redefs [rand-int (fn [max-value] 2)]
    (testing "Generates odd person number for male"
      (is (odd? (utils/str->int (ssn/generate-person-number :male)))))
    (testing "Generates even person number for female"
      (is (even? (utils/str->int (ssn/generate-person-number :female)))))
    (testing "Generates person number with length 3 for male"
      (is (= 3 (count (ssn/generate-person-number :male)))))
    (testing "Generates person number with length 3 for female"
      (is (= 3 (count (ssn/generate-person-number :female)))))))

(deftest generate-person-number-test-with-random-value-that-do-not-need-leading-zeros
  (with-redefs [rand-int (fn [max-value] 100)]
    (testing "Generates odd person number for male"
      (is (odd? (utils/str->int (ssn/generate-person-number :male)))))
    (testing "Generates even person number for female"
      (is (even? (utils/str->int (ssn/generate-person-number :female)))))
    (testing "Generates person number with length 3 for male"
      (is (= 3 (count (ssn/generate-person-number :male)))))
    (testing "Generates person number with length 3 for female"
      (is (= 3 (count (ssn/generate-person-number :female)))))))

(deftest generate-social-security-number-test
  (testing "Generates valid social security numbers for random persons"
    (let [sample-persons (gen/sample (s/gen ::ssn/person))]
      (is (every?
           #(s/valid? ::ssn/social-security-number (ssn/generate-social-security-number %))
           sample-persons)))))

(deftest social-security-number-spec-test-with-generator
  (testing "Generates valid social security numbers from spec"
    (let [sample-social-security-numbers (gen/sample (s/gen ::ssn/social-security-number))]
      (is (every?
           #(s/valid? ::ssn/social-security-number %)
           sample-social-security-numbers)))))
