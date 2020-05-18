(ns graph-service.component-test-support.system
  (:require
    [freeport.core :refer [get-free-port!]]

    [graph-service.shared.database
     :refer [new-database]]
    [graph-service.service
     :refer [new-service]]
    [graph-service.component-test-support.database
     :refer [database-configuration]]
    [graph-service.component-test-support.service
     :refer [service-configuration-for
             address]]
    [graph-service.core :as core]))

(defn new-test-system []
  (let [host "localhost"
        port (get-free-port!)]
    (merge
      (core/new-graph-service
        {:database database-configuration
         :service  (service-configuration-for host port)})
      {:address (address host port)})))

(defn with-system-lifecycle [system-atom]
  (fn [f] (try
            (do
              (reset! system-atom (core/start @system-atom))
              (f))
            (finally
              (reset! system-atom (core/stop @system-atom))))))
