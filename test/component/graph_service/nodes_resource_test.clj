(ns graph-service.nodes-resource-test
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

  (deftest nodes-resource-POST-on-success
    (let [address (core/address @test-system)

          some-property (data/random-uuid)
          other-property (data/random-uuid)

          new-node (data/random-node
                     {:properties {:some-property  some-property
                                   :other-property other-property}})

          discovery-result (navigator/discover
                             address {:follow-redirects false})

          create-node-result (navigator/post discovery-result :nodes new-node)

          created-node-resource (navigator/resource create-node-result)]
      (testing "returns status code 201"
        (is (= 201 (navigator/status create-node-result))))

      (testing "has location header"
        (is (some? (navigator/location create-node-result))))

      (testing "has a self link"
        (is (some? (hal/get-href created-node-resource :self))))

      (testing "has given properties"
        (is (some? (hal/get-property created-node-resource :id)))
        (is (= some-property (hal/get-property
                               created-node-resource :someProperty)))
        (is (= other-property (hal/get-property
                                created-node-resource :otherProperty)))))))
