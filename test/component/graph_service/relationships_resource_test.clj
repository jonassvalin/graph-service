(ns graph-service.relationships-resource-test
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

  (deftest relationships-resource-POST-on-success
    (let [address (core/address @test-system)

          discovery-result (navigator/discover
                             address {:follow-redirects false})

          node-id-1 (navigator/resource
                      (navigator/post discovery-result :nodes {}))
          node-id-2 (navigator/resource
                      (navigator/post discovery-result :nodes {}))


          some-property (data/random-uuid)
          other-property (data/random-uuid)
          relationship-type "friend"

          new-relationship {:from       (hal/get-property node-id-1 :id)
                            :to         (hal/get-property node-id-2 :id)
                            :type       relationship-type
                            :properties {:some-property  some-property
                                         :other-property other-property}}

          relationships-result (navigator/post discovery-result :relationships
                                 new-relationship)

          relationships-resource (navigator/resource relationships-result)]
      (testing "returns status code 201"
        (is (= 201 (navigator/status relationships-result))))

      (testing "has a self link"
        (is (some? (hal/get-href relationships-resource :self))))

      (testing "has a from link"
        (is (= (hal/get-href node-id-1 :self)
              (hal/get-href relationships-resource :from))))

      (testing "has a to link"
        (is (= (hal/get-href node-id-2 :self)
              (hal/get-href relationships-resource :to))))

      (testing "has given properties"
        (is (some? (hal/get-property relationships-resource :id)))
        (is (= some-property (hal/get-property
                               relationships-resource :someProperty)))
        (is (= other-property (hal/get-property
                                relationships-resource :otherProperty)))))))
