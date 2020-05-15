(ns graph-service.shared.middleware
  (:require
    [jason.convenience :as json]
    [graph-service.shared.logging :refer [log-error]])
  (:import (clojure.lang ExceptionInfo)))

(defn wrap-exception [handler]
  (fn [request]
    (try
      (handler request)
      (catch RuntimeException e
        (let [exception-class-name (.getCanonicalName (class e))
              exception-message (.getMessage e)
              exception-stacktrace (map str (.getStackTrace e))
              exception-description (str e)]
          (log-error {:exception-type        exception-class-name
                      :exception-stacktrace  exception-stacktrace
                      :exception-message     exception-message
                      :exception-description exception-description}
            (format "Unhandled exception occurred: %s" exception-message))
          {:headers {"content-type" "application/json"}
           :status  500
           :body    (if (instance? ExceptionInfo e)
                      (json/->wire-json (ex-data e))
                      (str e))})))))
