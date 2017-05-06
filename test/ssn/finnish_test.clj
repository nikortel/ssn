(ns ssn.finnish-test
  (:require [ssn.finnish :refer :all]
            [clojure.test :refer :all]
            [clojure.spec.alpha :as s]))

(deftest format-spec-test
  (testing "SSN conforms to format"
    (is (valid-format? "040597-959K")))
  (testing "Number does not conform to format"
    (is (not (valid-format? 1))))
  (testing "SSN with birth day number 32 does not conform to format"
    (is (not (valid-format? "320597-959K"))))
  (testing "Earliest birth date conforms to format"
    (is (valid-format? "010150+931C")))
  (testing "Birth year before 1850 does not conform to format"
    (is (not (valid-format? "010149+931C"))))
  (testing "Birth day at 2020 conforms to format"
    (is (valid-format? "010120A901N"))))

(deftest check-mark-base-test
  (testing "Returns check mark base"
    (is (= 40597959 (check-mark-base "040597-959K")))))

(deftest is-positive-single-digit?-test
  (testing "Returns true when number is positive single digit - min value"
    (is (is-positive-single-digit? 0)))
  (testing "Returns true when number is positive single digit - max value"
    (is (is-positive-single-digit? 9)))
  (testing "Returns false when number is negative single digit"
    (is (not (is-positive-single-digit? -1))))
  (testing "Returns false when number is positive double digit"
    (is (not (is-positive-single-digit? 10))))
  (testing "Returns false when number is positive decimal"
    (is (not (is-positive-single-digit? 1.1)))))

(deftest check-mark-test
  (testing "Returns check mark from check-marks-table"
    (is (= "K" (check-mark "040597-959K"))))
  (testing "Returns check mark candidate"
    (is (= "3" (check-mark "040597-9753")))))

(deftest check-mark-valid?-test
  (testing "Returns true when checkmark is valid"
    (is (check-mark-valid? "040597-959K")))
  (testing "Returns false when checkmark is not valid"
    (is (not (check-mark-valid? "040597-959Y")))))

(deftest social-security-number-spec-test
  (testing "1849 social security number is not valid"
    (is (not (s/valid? :ssn.finnish/social-security-number "010149+9635"))))
  (testing "1850 social security number is valid"
    (is (s/valid? :ssn.finnish/social-security-number "010150+931C")))
  (testing "Numbers only social security number is valid"
    (is (s/valid? :ssn.finnish/social-security-number "040597-9753"))))
