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
