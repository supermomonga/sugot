(ns sugot.core
  (:import [org.bukkit.craftbukkit Main]
           [org.bukkit Bukkit]))

(defn apps []
  ; So far I didn't find the way how to automatically collects all namespaces.
  #{'sugot.app.convo 'sugot.app.staging})

(def bukkit-events
  {"AsyncPlayerChatEvent" org.bukkit.event.player.AsyncPlayerChatEvent
   "PlayerLoginEvent" org.bukkit.event.player.PlayerLoginEvent})

(defn listeners [ns-symbol]
  ; the ns-symbol has to be `require`d in advance.
  (into {} (for [[fname-sym f] (ns-interns ns-symbol)
                 :let [klass (bukkit-events (name fname-sym))]
                 :when klass]
             [klass [f]])))

(defn defevent [pm event-type f]
  (let [plugin (reify org.bukkit.plugin.Plugin
                 (getConfig [this] nil)
                 (getDatabase [this] nil)
                 (getDataFolder [this] nil)
                 (getDefaultWorldGenerator [this worldName id] nil)
                 (getDescription [this] nil)
                 (getLogger [this] nil)
                 (getName [this] "sugot")
                 (getPluginLoader [this] nil)
                 (getResource [this filename] nil)
                 (getServer [this] (Bukkit/getServer))
                 (isEnabled [this] true)
                 (isNaggable [this] nil)
                 (onDisable [this] nil)
                 (onEnable [this] (prn 'onEnable this))
                 (onLoad [this] (prn 'onLoad this))
                 (reloadConfig [this] nil)
                 (saveConfig [this] nil)
                 (saveDefaultConfig [this] nil)
                 (saveResource [this resourcePath, replace-b] nil)
                 (setNaggable [boolean canNag] nil))
        listener (reify org.bukkit.event.Listener)
        executor (reify org.bukkit.plugin.EventExecutor
                   (execute [this listener event]
                     (f event)))
        priority org.bukkit.event.EventPriority/NORMAL]
    (-> pm (.registerEvent event-type listener priority executor plugin))))

(defn register-all [pm]
  ; gather events, and register them
  (doseq [app (apps)
          :let [_ (require app)]
          [klass fs] (listeners app)
          f fs]
    (defevent pm klass f)))

(defn -main [& args]
  (future (Main/main (make-array String 0)))

  ; call `start` once server is ready.
  (loop [server nil]
    (Thread/sleep 100)
    (if server
      (let [pm (-> server .getPluginManager)]
        (register-all pm))
      (recur (try (Bukkit/getServer) (catch Exception e nil))))) )
