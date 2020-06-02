(ns graph-service.test-support.data
  (:require
    [clojure.string :refer [join]])
  (:import
    [java.util UUID]))

(defn random-uuid []
  (str (UUID/randomUUID)))

(defn random-node
  ([] (random-node {}))
  ([{:keys [properties]
     :or   {properties {:some-property  "some-value"
                        :other-property "other-value"}}}]
   {:properties properties}))

(defn random-relationship
  [{:keys [to
           from
           relationship-type
           properties]
    :or   {relationship-type "friend"
           properties        {:some-property  "some-value"
                              :other-property "other-value"}}}]
  {:to         to
   :from       from
   :type       relationship-type
   :properties properties})
