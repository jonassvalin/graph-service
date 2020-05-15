(ns graph-service.node.mappers
  (:require
    [halboy.resource :as hal]
    [graph-service.node.urls
     :refer [node-url-for]]))

(defn node->node-resource
  [request routes node]
  (->
    (node-url-for request routes node)
    (hal/new-resource)
    (hal/add-properties
      (:data node))
    (hal/add-property
      :id (:id node))))
