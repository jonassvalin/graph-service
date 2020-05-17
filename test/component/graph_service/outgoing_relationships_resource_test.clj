(ns graph-service.outgoing-relationships-resource-test
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

  (deftest outgoing-relationships-resource-GET-on-no-relationships
    (let [address (core/address @test-system)

          some-property (data/random-uuid)
          other-property (data/random-uuid)

          new-node {:some-property  some-property
                    :other-property other-property}

          discovery-result (navigator/discover
                             address {:follow-redirects false})

          create-node-result (navigator/post discovery-result :nodes new-node)

          relationships-result (navigator/get
                                 create-node-result :outgoingRelationships)

          relationships-resource (navigator/resource relationships-result)]
      (testing "returns status code 200"
        (is (= 200 (navigator/status relationships-result))))

      (testing "has a self link"
        (is (some? (hal/get-href relationships-resource :self))))))

  (deftest outgoing-relationships-resource-GET-on-success
    (let [address (core/address @test-system)

          discovery-result (navigator/discover
                             address {:follow-redirects false})

          node-1-result (navigator/post discovery-result :nodes {})
          node-1-resource (navigator/resource
                            node-1-result)
          node-2-resource (navigator/resource
                            (navigator/post discovery-result :nodes {}))
          node-3-resource (navigator/resource
                            (navigator/post discovery-result :nodes {}))

          relationship-1-to-2 (data/random-relationship
                                {:from (hal/get-property node-1-resource :id)
                                 :to   (hal/get-property node-2-resource :id)})

          relationship-1-to-3 (data/random-relationship
                                {:from (hal/get-property node-1-resource :id)
                                 :to   (hal/get-property node-3-resource :id)})

          result-1-to-2 (navigator/post discovery-result :relationships
                          relationship-1-to-2)

          result-1-to-3 (navigator/post discovery-result :relationships
                          relationship-1-to-3)

          relationships-result (navigator/get
                                 node-1-result :outgoingRelationships)
          relationships-resource (navigator/resource relationships-result)
          relationships (hal/get-resource
                          relationships-resource :outgoingRelationships)]

      (testing "successfully creates relationships"
        (is (= 201 (navigator/status result-1-to-2)))
        (is (= 201 (navigator/status result-1-to-3)))
        (is (= 2 (count relationships))))

      (testing "relationships have correct fields"
        (let [resource-1-to-2 (second relationships)]
          (is (= (hal/get-href node-1-resource :self)
                (hal/get-href resource-1-to-2 :from)))
          (is (= (hal/get-href node-2-resource :self)
                (hal/get-href resource-1-to-2 :to))))
        (let [resource-1-to-3 (first relationships)]
          (is (= (hal/get-href node-1-resource :self)
                (hal/get-href resource-1-to-3 :from)))
          (is (= (hal/get-href node-3-resource :self)
                (hal/get-href resource-1-to-3 :to))))))))
