(ns graph-service.component-test-support.api
  (:require
    [halboy.navigator :as navigator]
    [halboy.resource :as hal]
    [graph-service.test-support.data :as data]))

(defn create-node [discovery]
  (navigator/resource
    (navigator/post discovery :nodes (data/random-node))))

(defn create-relationship [discovery from to]
  (->
    discovery
    (navigator/post :relationships
      (data/random-relationship
        {:from (hal/get-property from :id)
         :to   (hal/get-property to :id)}))
    (navigator/resource)))
