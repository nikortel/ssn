(defproject ssn "0.1.0-SNAPSHOT"
  :description "Library for validating social security numbers"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 ;;[org.clojure/clojurescript "1.9.521"]
                 ]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}}
  ;;:plugins [[lein-cljsbuild "1.1.6"]]
  ;; :cljsbuild {:builds [{:source-paths ["src"]
  ;;                       :compiler {:optimizations
  ;;                                  :whitespace
  ;;                                  :pretty-print true}}]}
  ;; :hooks [leiningen.cljsbuild]
  )
