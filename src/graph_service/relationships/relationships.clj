(ns graph-service.relationships.relationships
  (:require
    [clojurewerkz.neocons.rest.relationships :as nrl]

    [graph-service.node.node :as node]))

(defn create [database relationship]
  (let [from (node/by-id database (:from relationship))
        to (node/by-id database (:to relationship))
        type (keyword (:type relationship))
        properties (:properties relationship)]
    (nrl/create (:handle database) from to type properties)))

(defn all-by-node [database node]
  (nrl/all-for (:handle database) (:id node)))
