(ns graph-service.relationship.urls
  (:require
    [hype.core :as urls]))

(defn relationship-url-for
  [request routes relationship]
  (urls/absolute-url-for request routes :relationship
    {:path-params
     {:relationship-id (:id relationship)}}))
