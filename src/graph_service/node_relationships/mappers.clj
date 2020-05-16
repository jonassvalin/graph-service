(ns graph-service.node-relationships.mappers
  (:require
    [halboy.resource :as hal]
    [graph-service.node.urls
     :refer [node-url-for]]
    [graph-service.node-relationships.urls
     :refer [node-relationships-url-for]]))

(defn node-relationships->node-relationships-resource
  [request routes node relationships]
  (->
    (hal/new-resource)
    (hal/add-hrefs
      {:self (node-relationships-url-for request routes node)})
    (hal/add-resource :relationships
      [])))
