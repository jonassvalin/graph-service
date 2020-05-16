(ns graph-service.node-relationships.resource
  (:require
    [graph-service.shared.resources :as r]
    [graph-service.node.urls
     :refer [node-url-for]]
    [graph-service.node.mappers
     :refer [node->node-resource]]
    [graph-service.node-relationships.mappers
     :refer [node-relationships->node-relationships-resource]]
    [graph-service.node.node :as node]))

(defn node-relationships-resource-handler-for
  [{:keys [routes database] :as dependencies}]
  (r/hal-resource-handler-for dependencies
    :allowed-methods [:get]

    :initialize-context
    (fn [context]
      (let [node-id (get-in context [:request :params :node-id])
            node (node/by-id database node-id)]
        {:node node}))

    :handle-created
    (fn [{:keys [request node]}]
      (node->node-resource
        request routes node))

    :handle-ok
    (fn [{:keys [request routes node relationships]}]
      (node-relationships->node-relationships-resource
        request routes node relationships))))
