(ns graph-service.node-relationships.urls
  (:require
    [hype.core :as urls]))

(defn node-relationships-url-for
  [request routes node]
  (urls/absolute-url-for request routes :node-relationships
    {:path-params
     {:node-id (:id node)}}))
