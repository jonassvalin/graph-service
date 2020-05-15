(ns graph-service.node-resource-test
  (:require
    [clojure.test :refer :all]
    [clojure.string :refer [includes? ends-with?]]

    [halboy.navigator :as navigator]
    [halboy.resource :as hal]

    [graph-service.test-support.data
     :refer [random-new-node]]
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

  (deftest node-resource-GET-on-success
    (let [address
          (core/address @test-system)

          some-property (data/random-uuid)
          other-property (data/random-uuid)

          new-node {:some-property  some-property
                    :other-property other-property}

          discovery-result
          (navigator/discover address {:follow-redirects false})

          create-node-result
          (navigator/post discovery-result :nodes new-node)

          fetched-node-result
          (navigator/get create-node-result :self)

          fetched-node-resource
          (navigator/resource create-node-result)]
      (testing "returns status code 201"
        (is (= 201 (navigator/status create-node-result))))

      (testing "returns status code 201"
        (is (= 200 (navigator/status fetched-node-result))))

      (testing "has location header"
        (is (some? (navigator/location create-node-result))))

      (testing "has a self link"
        (is (some? (hal/get-href fetched-node-resource :self))))

      (testing "has an id"
        (is (some? (hal/get-property fetched-node-resource :id))))

      (testing "has given properties"
        (is (= some-property (hal/get-property
                               fetched-node-resource :someProperty)))
        (is (= other-property (hal/get-property
                                fetched-node-resource :otherProperty)))))))
