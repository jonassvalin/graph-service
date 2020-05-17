(ns graph-service.client.client
  (:require
    [graph-service.shared.logging :refer [log-warn]]
    [halboy.resource :as hal]
    [halboy.navigator :as navigator]))

(defn- get-outgoing-relationships [source]
  (->
    (str source)
    (navigator/discover)
    (navigator/get :outgoingRelationships)
    (navigator/resource)
    (hal/get-resource :outgoingRelationships)))

(defn shortest-from [from to]
  (let [outgoing-relationships (get-outgoing-relationships from)
        leads-to-destination (->
                               #(= to (hal/get-href % :to))
                               (filter outgoing-relationships)
                               (first))]
    (if leads-to-destination
      [(hal/get-property leads-to-destination :id)]
      (when-not (empty? outgoing-relationships)
        (let [paths (map
                      #(let [shortest-path (shortest-from
                                             (hal/get-href % :to) to)]
                         (when
                           shortest-path
                           (conj shortest-path (hal/get-property % :id))))
                      outgoing-relationships)
              shortest-successful-path (as-> paths all-paths
                                         (filter some? all-paths)
                                         (sort-by count all-paths)
                                         (first all-paths))]
          (when shortest-successful-path
            shortest-successful-path))))))

(defn find-shortest-path [from to]
  (let [path (shortest-from from to)]
    (when path (reverse path))))

