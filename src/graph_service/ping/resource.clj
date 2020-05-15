(ns graph-service.ping.resource
  (:require
    [halboy.resource :as hal]
    [hype.core :as urls]
    [graph-service.shared.resources :as r]))

(defn ping-response []
  {:message "pong"})

(defn ping-resource-handler-for [{:keys [routes] :as dependencies}]
  (r/hal-resource-handler-for dependencies
    :handle-ok
    (fn [{:keys [request]}]
      (hal/add-properties
        (hal/new-resource {:href (urls/absolute-url-for request routes :ping)})
        (ping-response)))))
