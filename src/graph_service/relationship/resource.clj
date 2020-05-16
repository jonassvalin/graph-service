(ns graph-service.relationship.resource
  (:require
    [graph-service.shared.resources :as r]
    [graph-service.relationship.urls
     :refer [relationship-url-for]]
    [graph-service.relationship.mappers
     :refer [relationship->relationship-resource]]
    [graph-service.relationships.relationships :as relationships]))

(defn relationship-resource-handler-for
  [{:keys [routes database] :as dependencies}]
  (r/hal-resource-handler-for dependencies
    :allowed-methods [:post]

    :post!
    (fn [{:keys [request]}]
      (let [relationship-details (:body request)
            relationship (relationships/create database relationship-details)]
        {:location     (relationship-url-for request routes relationship)
         :relationship relationship}))

    :handle-created
    (fn [{:keys [request relationship]}]
      (relationship->relationship-resource
        request routes relationship))))
