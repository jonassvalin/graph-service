(ns graph-service.default.resource
  (:require [graph-service.shared.logging :refer [log-warn]]))

(def response-message
  "{\"message\": \"Resource not found\"}")

(defn no-route-resource-handler [_]
  (fn [context]
    (do
      (log-warn {}
        (let [request-method (:request-method context)
              uri (:uri context)]
          (format "Route %s %s not found" request-method uri)))
      {:status 404 :body response-message})))
