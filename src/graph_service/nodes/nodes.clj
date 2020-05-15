(ns graph-service.nodes.nodes
  (:require
    [clojurewerkz.neocons.rest.nodes :as nn]))

(defn create [database node]
  (let [handle (:handle database)]
    (nn/create handle node)))
