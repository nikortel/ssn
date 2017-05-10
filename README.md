# ssn

A Clojure library designed to social security number validation via spec.

## Usage

### Validator

```clojure
(:require [ssn.finnish :as ssn]
          [clojure.spec.alpha :as s]
          [clojure.spec.gen.alpha :as gen])

;;Validate social security number
(s/valid? ::ssn/social-security-number "040597-9753")
```
### Generators

Usage of the generator functions needs test.check library. In Leiningen add this to your project.clj
```clojure
:profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}}
```

```clojure
;;Generate a social security number for specific birthdate and gender
(ssn/generate-social-security-number {::ssn/day 1
                                      ::ssn/month 1
                                      ::ssn/year 1986
                                      ::ssn/gender :female})

;;Generate a random social security number
(gen/generate (s/gen ::ssn/social-security-number))
```
## Testing

NOTE: Default test setup for cljs tests requires PhantomJS 

To run clj and cljs tests execute
```
lein test-all-once
```

For clj tests
```
lein test
```

For cjls tests
```
lein test-cljs
```

## License

Copyright © 2017 Niko Kortelainen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
