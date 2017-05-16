(ns ssn.utils-test
  (:require [ssn.test-helpers :as help]
            [ssn.utils]
            #?(:clj  [clojure.test :refer [deftest testing is]]
               :cljs [cljs.test :refer-macros [deftest is testing run-tests]])
            #?(:clj  [clojure.spec.test.alpha :as stest]
               :cljs [cljs.spec.test.alpha :as stest])
            #?(:clj  [clojure.spec.alpha :as s]
               :cljs [cljs.spec.alpha :as s])))

;;Not yet working with cljs
#?(:clj (deftest rand-int-in-range-spec-test
          (testing "rand-int-in-range conforms to spec"
            (is (true? (help/stest-result (stest/check `ssn.utils/rand-int-in-range)))))))

#?(:clj (deftest zero-pad-spec-test
          (testing "zero-pad conforms to spec"
            (is (true? (help/stest-result (stest/check `ssn.utils/zero-pad)))))))

#?(:clj (deftest str->int-spec-test
          (testing "str->int conforms to spec"
            (is (true? (help/stest-result (stest/check `ssn.utils/str->int)))))))
