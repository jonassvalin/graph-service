(ns graph-service.outgoing-relationships.resource
  (:require
    [graph-service.shared.resources :as r]
    [graph-service.node.urls
     :refer [node-url-for]]
    [graph-service.node.mappers
     :refer [node->node-resource]]
    [graph-service.outgoing-relationships.mappers
     :refer [outgoing-relationships->outgoing-relationships-resource]]
    [graph-service.node.node :as node]
    [graph-service.relationships.relationships :as relationships]))

(defn outgoing-relationships-resource-handler-for
  [{:keys [routes database] :as dependencies}]
  (r/hal-resource-handler-for dependencies
    :allowed-methods [:get]

    :initialize-context
    (fn [context]
      (let [node-id (get-in context [:request :params :node-id])
            node (node/by-id database node-id)]
        {:node node}))

    :handle-ok
    (fn [{:keys [request node]}]
      (let [relationships (relationships/outgoing-by-node database node)]
        (outgoing-relationships->outgoing-relationships-resource
          request routes node relationships)))))
