(ns sugot.app.playlog-test
  (:require [clojure.test :refer :all]
            [sugot.app.playlog :refer :all]
            [sugot.lib :as l]
            [sugot.mocks :as mocks]
            [sugot.world]))

(deftest PlayerLoginEvent-test
  (testing "notifies to lingr"
    (with-redefs [l/post-lingr (fn [msg] {:msg msg})
                  sugot.world/strike-lightning-effect (fn [loc] nil)]
      ; TODO Player.getLocation is required
      #_ (is (= {:msg "[LOGIN] dummy-player logged in."}
             (PlayerLoginEvent
               (reify mocks/Player
                 (getPlayer [this] (mocks/player "dummy-player")))
               (P. "dummy-player" nil nil)))))))

(deftest PlayerQuitEvent-test
  (testing "notifies to lingr"
    (with-redefs [l/post-lingr (fn [msg] {:msg msg})]
      (is (= {:msg "[LOGOUT] dummy-player logged out."}
             (PlayerQuitEvent (reify mocks/Player
                                (getPlayer [this] (mocks/player "dummy-player")))))))))

(deftest PlayerBedEnterEvent-test
  (testing "notifies both to lingr and server"
    (with-redefs [l/broadcast-and-post-lingr (fn [msg] {:post-lingr msg})]
      ; TODO test if braodcast is also called
      (is (= {:post-lingr "[BED] dummy-player went to bed."}
             (PlayerBedEnterEvent (reify mocks/Player
                                    (getPlayer [this] (mocks/player "dummy-player")))))))))

; TODO TODO TODO
#_ (deftest PlayerDeathEvent-test
  (testing "notifies both to lingr and server"
    (let [player nil
          event
          (let [drops nil
                dropped-exp nil
                new-exp nil
                new-total-exp nil
                new-level nil
                death-message "dummy-death-message"]
            ; PlayerDeathEvent(
            ;   Player player, List<ItemStack> drops, int droppedExp, int newExp, int newTotalExp, int newLevel, String deathMessage)
            (proxy [] []
              (getDeathMessage [this] "dummy-death-message")))]
          (with-redefs [l/broadcast-and-post-lingr (fn [msg] {:post-lingr msg})]
            (is (= nil
                   (PlayerDeathEvent event (P. "dummy-player" nil player))))))))

; (defn fixture [f]
;   ; before
;   (f)
;   ; after
;   )
; 
; (use-fixtures :each fixture)
