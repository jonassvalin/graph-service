(ns graph-service.relationship.resource
  (:require
    [graph-service.shared.resources :as r]
    [graph-service.relationship.urls
     :refer [relationship-url-for]]
    [graph-service.relationship.mappers
     :refer [relationship->relationship-resource]]
    [graph-service.relationship.relationship
     :as relationship]))

(defn relationship-resource-handler-for
  [{:keys [routes database] :as dependencies}]
  (r/hal-resource-handler-for dependencies

    :initialize-context
    (fn [context]
      (let [relationship-id (get-in context [:request :params :relationship-id])
            relationship (relationship/by-id database relationship-id)]
        {:relationship relationship}))

    :handle-ok
    (fn [{:keys [request relationship]}]
      (relationship->relationship-resource
        request routes relationship))))
