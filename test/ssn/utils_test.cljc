(ns ssn.utils-test
  (:require [ssn.utils :as utils]
            #?(:clj  [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :refer-macros [deftest is testing run-tests]])
            #?(:clj  [clojure.spec.test.alpha :as stest]
               :cljs [cljs.spec.test :as stest])))

#?(:clj (deftest rand-int-in-range-spec-test
          (testing "rand-int-in-range conforms to spec"
            (is (true? (get-in (first (stest/check 'ssn.utils/rand-int-in-range)) [:clojure.spec.test.check/ret :result]))))))
