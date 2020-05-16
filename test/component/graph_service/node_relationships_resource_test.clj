(ns graph-service.node-relationships-resource-test
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
    [graph-service.test-support.data :as data]))

(let [test-system (atom (new-test-system))]
  (use-fixtures :once (with-system-lifecycle test-system))
  (use-fixtures :each (with-empty-database test-system))

  (deftest relationships-resource-GET-on-no-relationships
    (let [address (core/address @test-system)

          some-property (data/random-uuid)
          other-property (data/random-uuid)

          new-node {:some-property  some-property
                    :other-property other-property}

          discovery-result (navigator/discover
                             address {:follow-redirects false})

          create-node-result (navigator/post discovery-result :nodes new-node)

          relationships-result (navigator/get
                                 create-node-result :relationships)

          relationships-resource (navigator/resource relationships-result)]
      (testing "returns status code 200"
        (is (= 200 (navigator/status relationships-result))))

      (testing "has a self link"
        (is (some? (hal/get-href relationships-resource :self)))))))
