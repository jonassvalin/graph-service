(ns graph-service.component-test-support.assertions
  (:require [clojure.set :refer [subset?]]))

(defn submap? [map-1 map-2]
  (clojure.set/subset?
    (set map-1)
    (set map-2)))
