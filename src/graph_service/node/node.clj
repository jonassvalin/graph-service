(ns graph-service.node.node
  (:require
    [clojurewerkz.neocons.rest.nodes :as nn]))

(defn by-id [database id]
  (let [handle (:handle database)]
    (nn/get handle id)))
