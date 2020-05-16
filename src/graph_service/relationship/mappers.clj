(ns graph-service.relationship.mappers
  (:require
    [halboy.resource :as hal]
    [graph-service.node.urls
     :refer [node-url-for]]
    [graph-service.relationship.urls
     :refer [relationship-url-for]]))

(defn relationship->relationship-resource
  [request routes relationship]
  (->
    (hal/new-resource)
    (hal/add-hrefs
      {:self (relationship-url-for request routes relationship)
       :from (node-url-for request routes (:start relationship))
       :to   (node-url-for request routes (:end relationship))})
    (hal/add-properties
      (:data relationship))
    (hal/add-property
      :id (:id relationship))))
