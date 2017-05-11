(ns ssn.cljs-test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [ssn.finnish-test]
            [ssn.utils-test]))

(doo-tests 'ssn.finnish-test
           'ssn.utils-test)
