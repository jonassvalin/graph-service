(ns graph-service.nodes.resource
  (:require
    [graph-service.shared.resources :as r]
    [graph-service.node.urls
     :refer [node-url-for]]
    [graph-service.node.mappers
     :refer [node->node-resource]]
    [graph-service.nodes.nodes :as nodes]))

(defn nodes-resource-handler-for
  [{:keys [routes database] :as dependencies}]
  (r/hal-resource-handler-for dependencies
    :allowed-methods [:post]

    :post!
    (fn [{:keys [request]}]
      (let [node-details (:body request)
            node (nodes/create database node-details)]
        {:location (node-url-for request routes node)
         :node     node}))

    :handle-created
    (fn [{:keys [request node]}]
      (node->node-resource
        request routes node))))
