(ns sugot.app.limited-world-test
  (:require [clojure.test :refer :all]
            [sugot.app.limited-world :refer :all]
            [sugot.lib :as l]
            [sugot.models :as m]
            [sugot.mocks :as mocks])
  (:import [sugot.models P]
           [sugot.mocks SugotWorld]))

(defrecord SugotPlayerMoveEvent [getTo])

(deftest PlayerMoveEvent-test
  (testing "send-message for the player if it's very far"
    (let [l (mocks/location (SugotWorld.  "world") 400 50 0)
          event (SugotPlayerMoveEvent. l)
          p (P. "dummy-player" nil nil)]
      (with-redefs [rand-int (fn [_] 0)
                    l/send-message (fn [p message]
                                     :ok)]
        (is (= :ok
               (PlayerMoveEvent event p)))))))
