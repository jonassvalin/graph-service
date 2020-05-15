(ns graph-service.discovery-resource-test
  (:require
    [clojure.test :refer :all]

    [halboy.resource :as hal]
    [halboy.navigator :as navigator]

    [org.bovinegenius.exploding-fish :refer [absolute?]]
    [clojure.string :refer [ends-with?]]

    [graph-service.component-test-support.system
     :refer [new-test-system
             with-system-lifecycle]]
    [graph-service.component-test-support.database
     :refer [with-empty-database]]
    [graph-service.core :as core]))

(let [test-system (atom (new-test-system))]
  (use-fixtures :once (with-system-lifecycle test-system))
  (use-fixtures :each (with-empty-database test-system))

  (deftest discovery-resource-GET-on-success
    (let [address (core/address @test-system)
          result (navigator/discover address)
          resource (navigator/resource result)]
      (testing "returns status code 200"
        (is (= 200 (navigator/status result))))

      (testing "includes a self link"
        (let [self-link (hal/get-href resource :self)]
          (is (absolute? self-link))
          (is (ends-with? self-link "/"))))

      (testing "includes a link to discovery"
        (let [discovery-link (hal/get-href resource :discovery)]
          (is (absolute? discovery-link))
          (is (ends-with? discovery-link "/"))))

      (testing "includes a link to ping"
        (let [ping-link (hal/get-href resource :ping)]
          (is (absolute? ping-link))
          (is (ends-with? ping-link "/ping"))))

      (testing "includes a link to node"
        (let [node-link
              (hal/get-href resource :node)]
          (is (absolute? node-link))
          (is (ends-with?
                node-link
                "/nodes/{nodeId}"))))

      (testing "includes a link to nodes"
        (let [node-link
              (hal/get-href resource :nodes)]
          (is (absolute? node-link))
          (is (ends-with?
                node-link
                "/nodes")))))))
