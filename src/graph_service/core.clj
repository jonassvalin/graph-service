(ns graph-service.core
  (:require
    [com.stuartsierra.component :as component]
    [configurati.core :as conf]

    [graph-service.service :as svc]
    [graph-service.shared.database :as db]))

(defn new-graph-service
  ([] (new-graph-service {}))
  ([configuration]
    (component/system-map
      :database-configuration
      (conf/resolve (:database
                      configuration
                      db/database-configuration))

      :database
      (component/using
        (db/new-database)
        {:configuration
         :database-configuration})

      :service-configuration
      (conf/resolve (:service
                      configuration
                      svc/service-configuration))

      :service (component/using
                 (svc/new-service)
                 [:service-configuration
                  :database]))))

(defn start [graph-service]
  (component/start-system graph-service))

(defn stop [graph-service]
  (component/stop-system graph-service))

(defn address [graph-service]
  (svc/address (:service graph-service)))
