(ns graph-service.shared.generators
  (:import (java.util UUID)))

(defn uuid [] (str (UUID/randomUUID)))
