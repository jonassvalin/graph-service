(ns graph-service.main
  (:require
    [graph-service.core :as core])
  (:gen-class))

(defn -main [& _]
  (let [graph-service (core/start (core/new-graph-service))]
    (.addShutdownHook
      (Runtime/getRuntime)
      (new Thread #(core/stop graph-service)))))
