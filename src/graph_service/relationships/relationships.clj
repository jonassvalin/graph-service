(ns graph-service.relationships.relationships
  (:require
    [clojurewerkz.neocons.rest.relationships :as nrl]
    [graph-service.node.node :as node]))

(defn create [database relationship]
  (let [from (node/by-id database (:from relationship))
        to (node/by-id database (:to relationship))
        type (keyword (:type relationship))
        properties (:properties relationship)
        relationship (nrl/create (:handle database) from to type properties)]
    (merge
      relationship
      {:from from
       :to   to})))
