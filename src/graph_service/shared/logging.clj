(ns graph-service.shared.logging
  (:require
    [cambium.codec :as codec]
    [cambium.core :as log]
    [cambium.logback.json.flat-layout :as flat])
  (:import
    [org.slf4j.bridge SLF4JBridgeHandler]
    [org.eclipse.jetty.server.handler HandlerWrapper]
    [org.eclipse.jetty.server Request]
    [javax.servlet.http HttpServletRequest HttpServletResponse]))

(defn setup-logging []
  (do
    (flat/set-decoder! codec/destringify-val)
    (SLF4JBridgeHandler/removeHandlersForRootLogger)
    (SLF4JBridgeHandler/install)))

(defmacro log-debug [context formatted-string]
  `(log/log :debug ~context nil ~formatted-string))

(defmacro log-info [context formatted-string]
  `(log/log :info ~context nil ~formatted-string))

(defmacro log-warn [context formatted-string]
  `(log/log :warn ~context nil ~formatted-string))

(defn get-error-context [context ^Throwable exception formatted-string]
  (let [exception-class-name (.getCanonicalName (class exception))
        exception-stacktrace (map str (.getStackTrace exception))
        exception-description (str exception)]
    (merge
      {:exception-type        exception-class-name
       :exception-message     formatted-string
       :exception-stacktrace  exception-stacktrace
       :exception-description exception-description}
      context)))

(defmacro log-error
  ([context formatted-string]
   `(log/log :error ~context (:exception ~context) ~formatted-string))
  ([context formatted-string exception]
   `(log-error (get-error-context ~context ~exception ~formatted-string)
      (format "Unhandled exception occurred: %s" ~formatted-string))))

(defn obfuscate-sensitive-headers
  [header-val-fn ^String header-name]
  (if (.equalsIgnoreCase header-name "authorization")
    "*******"
    (header-val-fn header-name)))

(defn get-header-map [header-names header-val-fn]
  (let [get-header-with-value
        (fn [header-name]
          {header-name (obfuscate-sensitive-headers
                         header-val-fn header-name)})
        headers-with-vals (map get-header-with-value header-names)]
    (into {} headers-with-vals)))

(defmulti get-headers
  (fn [^Object request-or-response] (.getClass request-or-response)))
(defmethod get-headers HttpServletResponse [^HttpServletResponse response]
  (get-header-map
    (.getHeaderNames response)
    #(.getHeaders response %)))
(defmethod get-headers HttpServletRequest [^HttpServletRequest request]
  (get-header-map
    (enumeration-seq (.getHeaderNames request))
    #(enumeration-seq (.getHeaders request %))))

(defn request-logging-handler-factory
  []
  (proxy [HandlerWrapper] []
    (handle [^String target
             ^Request baseRequest
             ^HttpServletRequest request
             ^HttpServletResponse response]
      (proxy-super handle target baseRequest request response)
      (let [status (.getStatus response)
            latency (- (System/currentTimeMillis)
                      (.getTimeStamp baseRequest))]
        (log-info {:request  {:secure      (.isSecure request)
                              :method      (.getMethod request)
                              :uri         (.getRequestURI request)
                              :headers     (get-headers request)
                              :query       (.getQueryString request)
                              :httpVersion (.getProtocol request)}
                   :response {:statusCode status
                              :headers    (get-headers response)
                              :latency    latency}}
          (str "handled: " status))))))
