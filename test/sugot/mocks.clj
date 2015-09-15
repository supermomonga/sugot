(ns sugot.mocks
  (:import [org.bukkit Material]))

(defprotocol Entity (getEntity [this]))
(defprotocol Cause (getCause [this]))
(defprotocol Name (getName [this]))

(defprotocol ^:private SugotBlock
  (getType [this])
  (getData [this]))

(defn block [^Material type ^Byte data]
  (reify
    SugotBlock
    (getType [this] type)
    (getData [this] data)))

(defn world [name]
  (reify Name
    (getName [this] name)))

(defprotocol SugotLocation
  (getWorld [this])
  (getX [this])
  (getY [this])
  (getZ [this])
  (clone [this])
  (add [this x y z])
  (getBlock [this]))

(defn location
  ([world x y z]
    (location world x y z {}))
  ([world x y z block-map]
    (let [state (atom {:x x :y y :z z})]
      (reify SugotLocation
        (getWorld [this] world)
        (getX [this] (:x @state))
        (getY [this] (:y @state))
        (getZ [this] (:z @state))
        (clone [this]
          (location world
                    (.getX this)
                    (.getY this)
                    (.getZ this)
                    block-map))
        (add [this x0 y0 z0]
          (swap! state (fn [coll]
                         (conj coll {:x (+ (:x coll) x0)
                                     :y (+ (:y coll) y0)
                                     :z (+ (:z coll) z0)})))
          nil)
        (getBlock [this]
          (get block-map [(.getX this)
                          (.getY this)
                          (.getZ this)]))))))

(defprotocol Location
  (getLocation [this]))

(defn player
  ([name]
    (player name nil))
  ([name loc]
    (reify
      Name
      (getName [this] name)
      Location
      (getLocation [this] loc))))

(defprotocol Cancel
  (isCancelled [this])
  (setCancelled [this bool]))

(defprotocol ItemDrop
  (getItemDrop [this]))

(defprotocol ItemStack
  (getItemStack [this]))

(defprotocol ItemMeta
  (getItemMeta [this]))

(defprotocol DisplayName
  (getDisplayName [this]))

(defprotocol SugotCreatureSpawnEvent
  ; Use with Cancel and Location
  (getEntity [this])
  (getSpawnReason [this]))

(defprotocol Player
  (getPlayer [this]))

(defprotocol PlayerMoveEvent
  ; Use with Player
  (getTo [this])
  (getFrom [this]))

(defprotocol PlayerToggleSneakEvent
  ; Use with Player
  (isSneaking [this]))
