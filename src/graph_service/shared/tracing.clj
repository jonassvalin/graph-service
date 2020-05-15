(ns graph-service.shared.tracing
  (:require
    [cambium.core :as log])
  (:import
    [javax.servlet.http HttpServletRequest]
    [java.util UUID]
    [org.eclipse.jetty.server.handler HandlerWrapper]))

(def ^:private thread-local-context
  (ThreadLocal.))

(defn get-tracing-context
  []
  (merge {} (.get thread-local-context)))

(defn set-tracing-context
  [m]
  (.set thread-local-context m)
  thread-local-context)

(defn tracing-handler-factory []
  (proxy [HandlerWrapper] []
    (handle [target baseRequest ^HttpServletRequest request response]
      (let [correlation-id (or
                             (.getHeader request "correlation-id")
                             (str (UUID/randomUUID)))
            customer-id (.getHeader request "customer-id")
            customer-session-id (.getHeader request "customer-session-id")
            request-context {:correlation-id      correlation-id
                             :customer-id         customer-id
                             :customer-session-id customer-session-id}
            _ (set-tracing-context request-context)]
        (log/with-logging-context
          {:trace request-context}
          (proxy-super handle target baseRequest request response))
        (.remove thread-local-context)))))
