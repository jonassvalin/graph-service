(ns graph-service.node.resource
  (:require
    [graph-service.shared.resources :as r]
    [graph-service.node.urls
     :refer [node-url-for]]
    [graph-service.node.mappers
     :refer [node->node-resource]]
    [graph-service.node.node :as node]))

(defn node-resource-handler-for
  [{:keys [routes database] :as dependencies}]
  (r/hal-resource-handler-for dependencies

    :initialize-context
    (fn [context]
      (let [node-id (get-in context [:request :params :node-id])
            node (node/by-id database node-id)]
        {:node node}))

    :handle-ok
    (fn [{:keys [request node]}]
      (node->node-resource
        request routes node))))
