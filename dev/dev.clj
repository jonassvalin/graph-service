(ns dev
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application.
  Call `(reset)` to reload modified code and (re)start the system.
  The system under development is `system`, referred from
  `com.stuartsierra.component.repl/system`.
  See also https://github.com/stuartsierra/component.repl"
  (:require
    [clojure.java.javadoc :refer [javadoc]]
    [clojure.pprint :refer [pprint]]
    [clojure.reflect :refer [reflect]]
    [clojure.repl :refer [apropos dir doc find-doc pst source]]
    [clojure.tools.namespace.repl :refer [refresh refresh-all clear]]
    [eftest.runner :refer [run-tests find-tests]]

    [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
    [graph-service.core :refer [new-graph-service]]))

(defn dev-system
  "Constructs a system map suitable for interactive development."
  []
  (new-graph-service))

(set-init (fn [_] (dev-system)))
