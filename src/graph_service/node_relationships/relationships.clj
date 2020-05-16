(ns graph-service.node-relationships.relationships
  (:require
    [clojurewerkz.neocons.rest.relationships :as nrl]))

(defn create [database node]
  (let [handle (:handle database)]
    ;(nrl/create handle node)
    ))
