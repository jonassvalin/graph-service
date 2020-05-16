(ns graph-service.node.urls
  (:require
    [hype.core :as urls]
    [clojure.string :as str]))

(defn node-url-for
  [request routes node]
  (let [node-id (or (:id node) (last (str/split node #"/")))]
    (urls/absolute-url-for request routes :node
      {:path-params
       {:node-id node-id}})))
