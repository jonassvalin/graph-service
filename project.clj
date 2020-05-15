(def version
  (or (System/getenv "VERSION")
    "0.0.0+LOCAL"))

(defproject graph-service version
  :description "B-Social Graph Service"
  :url "https://github.com/b-social/graph-service"
  :license "Proprietary"
  :dependencies [[org.clojure/clojure "1.10.1"]

                 [b-social/liberator-mixin "0.0.31"]
                 [b-social/spec-validate "0.1.9"]
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

                 [org.slf4j/jcl-over-slf4j "1.7.25"]
                 [org.slf4j/jul-to-slf4j "1.7.25"]
                 [org.slf4j/log4j-over-slf4j "1.7.25"]
                 [ch.qos.logback/logback-classic "1.2.3"
                  :exclusions [org.slf4j/slf4j-api]]
                 [cambium/cambium.core "0.9.2"]
                 [cambium/cambium.logback.json "0.4.2"]
                 [cambium/cambium.logback.core "0.4.2"]
                 [cambium/cambium.codec-cheshire "0.9.2"]
                 [io.aviso/logging "0.3.1"]
                 [io.aviso/pretty "0.1.35"]

                 [configurati "0.4.0"]
                 [clj-yaml "0.4.0"]
                 [medley "1.0.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [camel-snake-kebab "672421b575737c5496b7ddcfb83cf150b0d0bc75"]
                 [clj-time "0.14.4"]
                 [org.bovinegenius/exploding-fish "0.3.6"]

                 [hikari-cp "2.6.0"]
                 [org.clojure/tools.reader "1.3.2"]]
  :plugins [[org.slf4j/slf4j-api "1.7.28"]
            [ch.qos.logback/logback-classic "1.2.3"
             :exclusions [org.slf4j/slf4j-api org.slf4j/slf4j-log4j12]]

            [lein-eftest "0.5.3"]
            [lein-cprint "1.3.0"]
            [lein-ancient "0.6.15"]
            [lein-kibit "0.1.6"]
            [lein-cljfmt "0.6.1"]
            [lein-bikeshed "0.5.1"]
            [jonase/eastwood "0.3.3"]
            [reifyhealth/lein-git-down "0.3.5"]]

  :middleware [lein-git-down.plugin/inject-properties]

  :git-down
  {camel-snake-kebab {:coordinates clj-commons/camel-snake-kebab}}

  :repositories [["public-github" {:url "git://github.com"}]]

  :profiles {:shared      {:dependencies
                           [[nrepl "0.6.0"]
                            [org.clojure/tools.namespace "0.2.11"]
                            [com.stuartsierra/component.repl "0.2.0"]

                            [ring/ring-mock "0.3.2"]

                            [freeport "1.0.0"]
                            [faker "0.3.2"]
                            [http-kit.fake "0.2.2"]

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
                    ["with-profile" "unit" "eftest" ":all"]
                    ["with-profile" "persistence" "eftest" ":all"]
                    ["with-profile" "component" "eftest" ":all"]]})
