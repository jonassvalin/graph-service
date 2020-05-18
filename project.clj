(def version
  (or (System/getenv "VERSION")
    "0.0.0+LOCAL"))

(defproject graph-service version
  :description "B-Social Graph Service"
  :url "https://github.com/b-social/graph-service"
  :license "Proprietary"
  :main graph-service.main
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [b-social/liberator-mixin "0.0.31"]
                 [b-social/hype "0.0.17"]
                 [b-social/jason "0.1.3"]
                 [clojurewerkz/neocons "3.2.0"]
                 [bidi "2.1.4"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-ssl "0.3.0"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [cheshire "5.8.1"]
                 [halboy "4.0.1"]
                 [liberator "0.15.2"]
                 [compojure "1.6.1"]
                 [cambium/cambium.codec-cheshire "0.9.2"]
                 [io.aviso/pretty "0.1.35"]

                 [configurati "0.4.0"]
                 [com.stuartsierra/component "0.3.2"]]
  :plugins [[lein-eftest "0.5.3"]
            [lein-cprint "1.3.0"]
            [lein-ancient "0.6.15"]
            [lein-kibit "0.1.6"]
            [lein-cljfmt "0.6.1"]
            [lein-bikeshed "0.5.1"]
            [jonase/eastwood "0.3.3"]]

  :profiles {:shared      {:dependencies
                           [[nrepl "0.6.0"]
                            [org.clojure/tools.namespace "0.2.11"]
                            [com.stuartsierra/component.repl "0.2.0"]
                            [ring/ring-mock "0.3.2"]
                            [freeport "1.0.0"]
                            [faker "0.3.2"]
                            [eftest "0.5.3"]
                            [mockery "0.1.4"]]}
             :dev         [:shared {:source-paths ["dev"]}]
             :unit        [:shared {:test-paths ^:replace ["test/shared"
                                                           "test/unit"]}]
             :persistence [:shared {:test-paths ^:replace ["test/shared"
                                                           "test/persistence"]
                                    :eftest     {:multithread? false}}]
             :component   [:shared {:test-paths ^:replace ["test/shared"
                                                           "test/component"]
                                    :eftest     {:multithread? false}}]
             :uberjar     {:main graph-service.main
                           :aot  :all}
             :server      {:main graph-service.main
                           :aot  :all}}
  :test-paths ["test/shared" "test/unit" "test/persistence" "test/component"]
  :target-path "build/app/%s/"
  :eastwood {:config-files ["config/linter.clj"]}
  :cljfmt {:indents ^:replace {#".*" [[:inner 0]]}}
  :aliases {"test" ["do"
                    ["with-profile" "component" "eftest" ":all"]]})
