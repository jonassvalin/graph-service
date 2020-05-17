(ns graph-service.node.mappers
  (:require
    [halboy.resource :as hal]
    [graph-service.node.urls
     :refer [node-url-for]]
    [graph-service.outgoing-relationships.urls
     :refer [outgoing-relationships-url-for]]))

(defn node->node-resource
  [request routes node]
  (->
    (hal/new-resource)
    (hal/add-hrefs
      {:self          (node-url-for request routes node)
       :outgoing-relationships (outgoing-relationships-url-for request routes node)})
    (hal/add-properties
      (:data node))
    (hal/add-property
      :id (:id node))))
