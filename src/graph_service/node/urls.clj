(ns graph-service.node.urls
  (:require
    [hype.core :as urls]))

(defn node-url-for
  [request routes node]
  (urls/absolute-url-for request routes :node
    {:path-params
     {:node-id (:id node)}}))
