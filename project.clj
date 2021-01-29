(def version
  (or (System/getenv "VERSION")
    "0.0.0+LOCAL"))

(defproject graph-service version
  :description "Graph Service"
  :url "https://github.com/jonassvalin/graph-service"
  :license "Proprietary"
  :main graph-service.main
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [b-social/liberator-mixin "0.0.56"]
                 [b-social/hype "1.0.0"]
                 [b-social/jason "0.1.5"]
                 [clojurewerkz/neocons "3.2.0"]
                 [bidi "2.1.6"]
                 [ring/ring-core "1.8.2"]
                 [ring/ring-ssl "0.3.0"]
                 [ring/ring-jetty-adapter "1.8.2"]
                 [cheshire "5.10.0"]
                 [halboy "5.1.1"]
                 [liberator "0.15.3"]
                 [compojure "1.6.2"]
                 [cambium/cambium.codec-cheshire "1.0.0"]
                 [io.aviso/pretty "0.1.37"]

                 [io.logicblocks/configurati "0.5.4"]
                 [com.stuartsierra/component "1.0.0"]]
  :plugins [[lein-eftest "0.5.9"]
            [lein-cprint "1.3.3"]
            [lein-ancient "0.6.15"]
            [lein-kibit "0.1.8"]
            [lein-cljfmt "0.7.0"]
            [lein-bikeshed "0.5.2"]
            [jonase/eastwood "0.3.10"]]

  :profiles {:shared      {:dependencies
                           [[nrepl "0.8.3"]
                            [org.clojure/tools.namespace "1.0.0"]
                            [com.stuartsierra/component.repl "0.2.0"]
                            [ring/ring-mock "0.4.0"]
                            [freeport "1.0.0"]
                            [faker "0.3.2"]
                            [eftest "0.5.9"]
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
