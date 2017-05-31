(defproject ssn "0.1.0-SNAPSHOT"
  :description "Library for validating social security numbers"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17"]
                 [org.clojure/clojurescript "1.9.562"]]

  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}}

  :plugins [[lein-cljsbuild "1.1.6"]
            [lein-doo "0.1.7"]]

  :cljsbuild {:builds [{:source-paths ["src"]
                        :compiler {:optimizations
                                   :whitespace
                                   :pretty-print true}}
                       {:id "test"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "resources/public/js/testable.js"
                                   :main ssn.cljs-test-runner
                                   :optimizations :none}}]}

  :aliases {"test-cljs" ["doo" "once" "phantom" "test"]
            "test-all-once" ["do" "test," "test-cljs"]}
  :monkeypatch-clojure-test false)
