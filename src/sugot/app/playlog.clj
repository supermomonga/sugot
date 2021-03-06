(ns sugot.app.playlog
  "Notifications mostly to lingr.
  This is not specific for notifications inside gameplay"
  (:require [clojure.string :as s]
            [sugot.lib :as l]
            [sugot.world]))

(defn PlayerLoginEvent [event]
  (sugot.world/strike-lightning-effect (.getLocation (.getPlayer event)))
  (l/post-lingr (format "[LOGIN] %s logged in." (.getName (.getPlayer event)))))

(defn PlayerQuitEvent [event]
  (l/post-lingr (format "[LOGOUT] %s logged out." (.getName (.getPlayer event)))))

(defn PlayerBedEnterEvent [event]
  (l/broadcast-and-post-lingr (format "[BED] %s went to bed." (.getName (.getPlayer event)))))

(defn PlayerDeathEvent [event]
  (l/broadcast-and-post-lingr (format "[DEATH] %s" (.getDeathMessage event))))
