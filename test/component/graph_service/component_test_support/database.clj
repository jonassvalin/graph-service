(ns graph-service.component-test-support.database
  (:require
    [environ.core :refer [env]]
    [configurati.core
     :refer [define-configuration
             with-specification
             with-source
             with-key-fn
             yaml-file-source
             map-source
             env-source]]
    [configurati.key-fns :refer [remove-prefix]]
    [graph-service.shared.database
     :refer [database-configuration-specification]]
    [clojurewerkz.neocons.rest.cypher :as cy]))

(def database-configuration
  (define-configuration
    (with-specification database-configuration-specification)
    (with-source (map-source
                   {:database-user     "neo4j"
                    :database-password "test"}))))

(defn clear-database [system]
  (let [handle (get-in system [:database :handle])]
    (cy/tquery handle "MATCH (n) DETACH DELETE n;")))

(defn with-empty-database [system-atom]
  (fn [f]
    (do
      (clear-database @system-atom)
      (f))))
