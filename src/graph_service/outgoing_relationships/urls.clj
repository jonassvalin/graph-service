(ns graph-service.outgoing-relationships.urls
  (:require
    [hype.core :as urls]))

(defn outgoing-relationships-url-for
  [request routes node]
  (urls/absolute-url-for request routes :outgoing-relationships
    {:path-params
     {:node-id (:id node)}}))
