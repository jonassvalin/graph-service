(ns graph-service.service
  (:require
    [com.stuartsierra.component :as component]

    [environ.core :refer [env]]
    [bidi.ring :refer [make-handler]]

    [ring.adapter.jetty :as ring-jetty]
    [ring.middleware.resource :as resource]
    [ring.middleware.params :as params]
    [ring.middleware.content-type :as content-type]
    [ring.middleware.not-modified :as not-modified]
    [ring.middleware.keyword-params :as keyword-params]
    [ring.middleware.ssl :as ssl]

    [configurati.core
     :refer [define-configuration-specification
             define-configuration
             with-specification
             with-parameter
             with-source
             with-key-fn
             env-source]]
    [configurati.key-fns :refer [remove-prefix]]

    [graph-service.shared.middleware :as middleware]
    [graph-service.shared.logging :as logging]
    [graph-service.shared.tracing :as tracing]

    [graph-service.ping.resource
     :refer [ping-resource-handler-for]]
    [graph-service.discovery.resource
     :refer [discovery-resource-handler-for]]
    [graph-service.node.resource
     :refer [node-resource-handler-for]]
    [graph-service.nodes.resource
     :refer [nodes-resource-handler-for]]
    [graph-service.outgoing-relationships.resource
     :refer [outgoing-relationships-resource-handler-for]]
    [graph-service.relationship.resource
     :refer [relationship-resource-handler-for]]
    [graph-service.relationships.resource
     :refer [relationships-resource-handler-for]]
    [graph-service.default.resource
     :refer [no-route-resource-handler]])
  (:import
    [org.eclipse.jetty.server.handler StatisticsHandler HandlerWrapper]
    [org.eclipse.jetty.server Server]))

(defn make-routes []
  [""
   [["/" :discovery]
    ["/ping" :ping]
    ["/health" :health]
    [["/nodes/" :node-id]
     [["" :node]
      ["/outgoing-relationships" :outgoing-relationships]]]
    [["/nodes"] :nodes]
    [["/relationships/" :relationship-id] :relationship]
    [["/relationships"] :relationships]
    [true :no-route]]])

(defn make-resource-handlers [dependencies]
  {:discovery
   (discovery-resource-handler-for dependencies)
   :ping
   (ping-resource-handler-for dependencies)
   :node
   (node-resource-handler-for dependencies)
   :nodes
   (nodes-resource-handler-for dependencies)
   :outgoing-relationships
   (outgoing-relationships-resource-handler-for dependencies)
   :relationship
   (relationship-resource-handler-for dependencies)
   :relationships
   (relationships-resource-handler-for dependencies)
   :no-route
   (no-route-resource-handler dependencies)})

(defn app-for [dependencies]
  (let [routes (make-routes)
        dependencies (assoc dependencies :routes routes)
        resource-handlers (make-resource-handlers dependencies)]
    (->
      (make-handler routes resource-handlers)
      (resource/wrap-resource "public")
      (ssl/wrap-forwarded-scheme)
      (content-type/wrap-content-type)
      (not-modified/wrap-not-modified)
      (keyword-params/wrap-keyword-params)
      (params/wrap-params)
      (middleware/wrap-exception))))

(def service-configuration-specification
  (define-configuration-specification
    (with-key-fn (remove-prefix :service))
    (with-parameter :service-host :default "0.0.0.0")
    (with-parameter :service-port :default 1234 :type :integer)))

(def service-configuration
  (define-configuration
    (with-specification service-configuration-specification)
    (with-source (env-source :prefix :graph-service))))

(def a-minute 60000)

(defn kilobytes->bytes [kb]
  (* 1024 kb))

(defn configure-for-graceful-shutdown
  [^Server server]
  (let [^HandlerWrapper stats-handler (StatisticsHandler.)
        default-handler (.getHandler server)]
    (.setHandler stats-handler default-handler)
    (.setHandler server stats-handler)
    (.setStopTimeout server a-minute)
    (.setStopAtShutdown server true)))

(defn configure-audit-logging
  [^Server server]
  (let [^HandlerWrapper request-log-handler
        (logging/request-logging-handler-factory)
        default-handler (.getHandler server)]
    (.setHandler request-log-handler default-handler)
    (.setHandler server request-log-handler)))

(defn configure-correlation-id-handler
  [^Server server]
  (let [^HandlerWrapper correlation-id-handler
        (tracing/tracing-handler-factory)
        default-handler (.getHandler server)]
    (.setHandler correlation-id-handler default-handler)
    (.setHandler server correlation-id-handler)))

(defn- format-address [host port]
  (format "http://%s:%s" host port))

(defrecord Service
  [service-configuration
   database
   service]

  component/Lifecycle

  (start [component]
    (let [service (ring-jetty/run-jetty
                    (app-for
                      {:database database})
                    {:host  (:host service-configuration)
                     :port  (:port service-configuration)
                     :join? false
                     :request-header-size
                            (kilobytes->bytes 32)

                     :response-header-size
                            (kilobytes->bytes 32)

                     :configurator
                            (fn [server]
                              (doto server
                                (configure-audit-logging)
                                (configure-correlation-id-handler)
                                (configure-for-graceful-shutdown)))})]
      (assoc component
        :service service
        :address (format-address
                   (:host service-configuration)
                   (:port service-configuration)))))

  (stop [component]
    (let [^Server service (:service component)]
      (when service
        (.stop service))
      (dissoc component :service :address))))

(defn new-service []
  (map->Service {}))

(defn address [service]
  (:address service))
