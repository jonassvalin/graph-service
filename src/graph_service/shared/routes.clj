(ns graph-service.shared.routes
  (:require
    [bidi.bidi :as bidi])
  (:import
    [clojure.lang ILookup]))

(deftype ParameterMirror []
  ILookup
  (valAt [_ k] (str k))
  (valAt [_ k _] (str k)))

(defn- handler-for [routes request]
  (let [uri (:uri request)
        match (bidi/match-route routes uri)
        handler (:handler match)]
    handler))

(defn route-template-factory [routes]
  (let [parameter-mirror (ParameterMirror.)]
    (fn [request]
      (let [handler (handler-for routes request)]
        (if (= handler :no-route)
          (:uri request)
          (bidi/unmatch-pair routes
            {:handler handler
             :params  parameter-mirror}))))))

(defn route-label-factory [routes]
  (fn [request _]
    (let [handler (handler-for routes request)]
      (if (= handler :no-route)
        {}
        {:route (name handler)}))))
