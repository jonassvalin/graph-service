(ns graph-service.shared.database
  (:require
    [com.stuartsierra.component :as component]
    [configurati.core
     :refer [define-configuration
             define-configuration-specification
             with-parameter
             with-source
             with-specification
             with-key-fn
             map-source]]
    [configurati.key-fns :refer [remove-prefix]]
    [clojurewerkz.neocons.rest :as nr]))

(def database-configuration-specification
  (define-configuration-specification
    (with-key-fn (remove-prefix :database))
    (with-parameter :database-user)
    (with-parameter :database-password)))

(def database-configuration
  (define-configuration
    (with-specification database-configuration-specification)
    (with-source (map-source
                   {:database-user     "neo4j"
                    :database-password "test"}))))

(defn datasource-for [{:keys [user password]}]
  (let [connection-string (str "http://" user ":" password "@localhost:7474/db/data")]
    (nr/connect connection-string)))

(defrecord Database
  [configuration handle]
  component/Lifecycle

  (start [component]
    (assoc component :handle (datasource-for configuration)))

  (stop [component]
    component))

(defn new-database []
  (map->Database {}))
