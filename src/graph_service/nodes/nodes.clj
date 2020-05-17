(ns graph-service.nodes.nodes
  (:require
    [clojurewerkz.neocons.rest.nodes :as nn]))

(defn create [database node]
  (let [properties (or (:properties node) {})]
    (nn/create (:handle database) properties)))
