(ns graph-service.nodes.resource
  (:require
    [halboy.resource :as hal]
    [graph-service.shared.resources :as r]

    [graph-service.node.urls
     :refer [node-url-for]]
    [graph-service.node.mappers
     :refer [node->node-resource]]
    [graph-service.shared.generators :as generators]
    [graph-service.nodes.nodes :as nodes]))

(defn nodes-resource-handler-for
  [{:keys [routes database] :as dependencies}]
  (r/hal-resource-handler-for dependencies
    :allowed-methods [:post :get]

    :handle-ok
    (fn [{:keys [request]}]
      {}
      ;(let [nodes
      ;      (doall
      ;        (find-all-nodes database))
      ;
      ;      node-resources
      ;      (map
      ;        #(node->node-resource
      ;           request
      ;           routes %)
      ;        nodes)]
      ;
      ;  (hal/add-resource
      ;    (hal/new-resource
      ;      {:href
      ;       (nodes-url-for
      ;         request routes)})
      ;    :nodes
      ;    node-resources))
      )

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
