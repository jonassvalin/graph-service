(ns graph-service.relationships.urls
  (:require
    [hype.core :as urls]))

(defn relationships-url-for
  [request routes]
  (urls/absolute-url-for request routes :relationships))
