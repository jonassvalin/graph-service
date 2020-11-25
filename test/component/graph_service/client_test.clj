(ns graph-service.client-test
  (:require
    [clojure.test :refer :all]
    [clojure.string :refer [includes? ends-with?]]

    [halboy.navigator :as navigator]
    [halboy.resource :as hal]

    [graph-service.component-test-support.system
     :refer [new-test-system
             with-system-lifecycle]]
    [graph-service.component-test-support.database
     :refer [with-empty-database]]
    [graph-service.component-test-support.assertions
     :refer [submap?]]
    [graph-service.core :as core]
    [graph-service.component-test-support.api
     :as api]
    [graph-service.client.client :as client]))

(let [test-system (atom (new-test-system))]
  (use-fixtures :once (with-system-lifecycle test-system))
  (use-fixtures :each (with-empty-database test-system))

  (deftest client-on-no-path
    (let [address (core/address @test-system)

          discovery-result (navigator/discover
                             address {:follow-redirects false})

          node-1 (api/create-node discovery-result)
          node-2 (api/create-node discovery-result)
          node-3 (api/create-node discovery-result)

          rel-1-to-2 (api/create-relationship discovery-result node-1 node-2)]

      (testing "returns nil if no path found"
        (let [path (client/find-shortest-path
                     (hal/get-href node-1 :self)
                     (hal/get-href node-3 :self))]
          (is (= path nil))))))

  (deftest client-on-1-layer
    (let [address (core/address @test-system)

          discovery-result (navigator/discover
                             address {:follow-redirects false})

          node-1 (api/create-node discovery-result)
          node-2 (api/create-node discovery-result)
          node-3 (api/create-node discovery-result)

          rel-1-to-2 (api/create-relationship discovery-result node-1 node-2)
          rel-1-to-3 (api/create-relationship discovery-result node-1 node-3)]

      (testing "correctly identifies shortest path"
        (let [path (client/find-shortest-path
                     (hal/get-href node-1 :self)
                     (hal/get-href node-3 :self))]
          (is (= path [(hal/get-property rel-1-to-3 :id)]))))))

  (deftest client-on-multiple-layers
    (let [address (core/address @test-system)

          discovery-result (navigator/discover
                             address {:follow-redirects false})

          node-1 (api/create-node discovery-result)
          node-2 (api/create-node discovery-result)
          node-3 (api/create-node discovery-result)
          node-4 (api/create-node discovery-result)
          node-5 (api/create-node discovery-result)
          node-6 (api/create-node discovery-result)
          node-7 (api/create-node discovery-result)

          rel-1-to-6 (api/create-relationship discovery-result node-1 node-6)
          rel-6-to-7 (api/create-relationship discovery-result node-6 node-7)

          rel-1-to-2 (api/create-relationship discovery-result node-1 node-2)

          rel-1-to-3 (api/create-relationship discovery-result node-1 node-3)
          rel-3-to-5 (api/create-relationship discovery-result node-3 node-5)
          rel-5-to-7 (api/create-relationship discovery-result node-5 node-7)]

      (testing "correctly identifies shortest path"
        (let [path (client/find-shortest-path
                     (hal/get-href node-1 :self)
                     (hal/get-href node-7 :self))]
          (is (= path [(hal/get-property rel-1-to-6 :id)
                       (hal/get-property rel-6-to-7 :id)])))))))
