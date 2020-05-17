(ns graph-service.outgoing-relationships.mappers
  (:require
    [halboy.resource :as hal]
    [graph-service.node.urls
     :refer [node-url-for]]
    [graph-service.outgoing-relationships.urls
     :refer [outgoing-relationships-url-for]]
    [graph-service.relationship.mappers
     :refer [relationship->relationship-resource]]))

(defn outgoing-relationships->outgoing-relationships-resource
  [request routes node relationships]
  (->
    (hal/new-resource)
    (hal/add-hrefs
      {:self (outgoing-relationships-url-for request routes node)})
    (hal/add-resource :outgoing-relationships
      (map #(relationship->relationship-resource
              request routes %)
        relationships))))
