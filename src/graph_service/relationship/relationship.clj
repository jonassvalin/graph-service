(ns graph-service.relationship.relationship
  (:require
    [clojurewerkz.neocons.rest.relationships :as nrl]))

(defn by-id [database id]
  (let [handle (:handle database)
        relationship (nrl/get handle id)]
    (merge
      relationship
      {:from (:start relationship)
       :to   (:end relationship)})))
