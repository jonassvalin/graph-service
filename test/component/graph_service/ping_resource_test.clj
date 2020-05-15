(ns graph-service.ping-resource-test
  (:require
    [clojure.test :refer :all]

    [halboy.resource :as hal]
    [halboy.navigator :as navigator]

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

  (deftest ping-resource-GET-on-success
    (let [address (core/address @test-system)

          result (-> (navigator/discover address)
                   (navigator/get :ping))
          resource (navigator/resource result)]
      (testing "returns status code 200"
        (is (= 200 (navigator/status result))))

      (testing "includes a self link"
        (is (ends-with? (hal/get-href resource :self) "/ping")))

      (testing "includes a link to discovery"
        (is (ends-with? (hal/get-href resource :discovery) "/")))

      (testing "returns a pong message"
        (is (= "pong" (hal/get-property resource :message)))))))
