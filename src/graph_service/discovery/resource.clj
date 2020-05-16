(ns graph-service.discovery.resource
  (:require
    [halboy.resource :as hal]
    [org.bovinegenius.exploding-fish :refer [path]]
    [hype.core :as urls]
    [graph-service.shared.resources :as r]))

(defn discovery-resource-handler-for [{:keys [routes] :as dependencies}]
  (r/hal-resource-handler-for dependencies
    :handle-ok
    (fn [{:keys [request]}]
      (hal/add-links
        (hal/new-resource)
        {:self
         {:href (urls/absolute-url-for request routes :discovery)}

         :ping
         {:href (urls/absolute-url-for request routes :ping)}

         :node
         {:templated :true
          :href      (urls/absolute-url-for
                       request
                       routes
                       :node
                       {:path-params
                        {:node-id
                         "{nodeId}"}})}

         :nodes
         {:href (urls/absolute-url-for request routes :nodes)}

         :relationships
         {:href (urls/absolute-url-for request routes :relationships)}}))))
