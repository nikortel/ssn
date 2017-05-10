(ns ssn.cljs-test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [ssn.finnish-test]))

(doo-tests 'ssn.finnish-test)
