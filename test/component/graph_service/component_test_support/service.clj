(ns graph-service.component-test-support.service
  (:require
    [environ.core :refer [env]]
    [configurati.core
     :refer [define-configuration
             with-specification
             with-source
             with-key-fn
             yaml-file-source
             map-source
             env-source]]
    [configurati.key-fns :refer [remove-prefix]]
    [graph-service.service
     :refer [service-configuration-specification]]))

(defn service-configuration-for [host port]
  (define-configuration
    (with-specification service-configuration-specification)
    (with-source (map-source {:service-host host :service-port port}))))

(defn address [host port]
  (format "http://%s:%s" host port))
