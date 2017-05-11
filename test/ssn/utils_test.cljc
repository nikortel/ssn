(ns ssn.utils-test
  (:require [ssn.utils :as utils]
            #?(:clj  [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :refer-macros [deftest is testing run-tests]])))

(deftest positive-single-digit-or-zero?-test
  (testing "Returns true when number is positive single digit - min value"
    (is (utils/positive-single-digit-or-zero? 0)))
  (testing "Returns true when number is positive single digit - max value"
    (is (utils/positive-single-digit-or-zero? 9)))
  (testing "Returns false when number is negative single digit"
    (is (not (utils/positive-single-digit-or-zero? -1))))
  (testing "Returns false when number is positive double digit"
    (is (not (utils/positive-single-digit-or-zero? 10))))
  (testing "Returns false when number is positive decimal"
    (is (not (utils/positive-single-digit-or-zero? 1.1)))))
