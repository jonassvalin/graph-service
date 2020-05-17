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

(defn outgoing-by-node [database node]
  (nrl/outgoing-for (:handle database) (:id node)))
