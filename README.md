# ssn

A Clojure library designed to social security number validation via spec.

## Usage

```clojure
(:require [ssn.finnish :as ssn]
          [clojure.spec.alpha :as s]
          [clojure.spec.gen.alpha :as gen])

;;Validate social security number
(s/valid? :ssn.finnish/social-security-number "040597-9753")

;;Generate a social security number for specific birthdate and gender
(ssn/generate-social-security-number {:ssn.finnish/day 1
                                      :ssn.finnish/month 1
                                      :ssn.finnish/year 1986
                                      :ssn.finnish/gender :female})

;;Generate a random social security number
(gen/generate (s/gen :ssn.finnish/social-security-number))
```

## License

Copyright © 2017 Niko Kortelainen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
