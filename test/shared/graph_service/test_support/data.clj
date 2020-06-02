(ns graph-service.test-support.data
  (:require
    [clojure.string :refer [join]]
    [faker.lorem :as lorem]
    [clj-time.core :as time])
  (:import
    [java.util UUID]))

(defn random-uuid []
  (str (UUID/randomUUID)))

(defn random-words []
  (join " " (take 5 (lorem/words))))

(defn random-created-at []
  (time/now))

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
