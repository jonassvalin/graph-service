(ns graph-service.nodes.urls
  (:require
    [hype.core :as urls]))

(defn nodes-url-for
  [request routes]
  (urls/absolute-url-for request routes :nodes))
