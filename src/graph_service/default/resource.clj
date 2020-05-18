(ns graph-service.default.resource)

(def response-message
  "{\"message\": \"Resource not found\"}")

(defn no-route-resource-handler [_]
  (fn [_]
    {:status 404 :body response-message}))
