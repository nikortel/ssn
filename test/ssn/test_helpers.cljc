(ns ssn.test-helpers)

(defn stest-result
  "Takes in spec test result and returns true for ok result or the whole result for fail"
  [result]
  (if (true? (get-in (first result) #?(:clj  [:clojure.spec.test.check/ret :result]
                                       :cljs [:clojure.test.check/ret :result])))
    true
    result))
