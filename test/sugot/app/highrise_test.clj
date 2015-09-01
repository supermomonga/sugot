(ns sugot.app.highrise-test
  (:require [clojure.test :refer :all]
            [sugot.app.highrise :refer :all]
            [sugot.lib :as l]
            [sugot.mocks :as mocks])
  (:import [org.bukkit Location]))

(deftest a-test
  (testing "prevent spawning monster at high space"
    (let [entity nil
          reason org.bukkit.event.entity.CreatureSpawnEvent$SpawnReason/NATURAL
          world (mocks/world "world")
          cancelled (ref false)
          event (reify mocks/SugotCreatureSpawnEvent
                  (getEntity [this] entity)
                  (getSpawnReason [this] reason)
                  (getLocation [this] (mocks/location world 0 120 0))
                  (setCancelled [this bool]
                    (dosync
                      (ref-set cancelled true))))]
      (is (= true
             (do
               (CreatureSpawnEvent event)
               @cancelled))))))
