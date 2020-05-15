(ns graph-service.main
  (:require
    [graph-service.core :as core]
    [graph-service.shared.logging :refer [setup-logging]])
  (:gen-class))

(defn -main [& _]
  (do
    (setup-logging)
    (let [graph-service (core/start (core/new-graph-service))]
      (.addShutdownHook
        (Runtime/getRuntime)
        (new Thread #(core/stop graph-service))))))
