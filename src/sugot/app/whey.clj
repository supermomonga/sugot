(ns sugot.app.whey
  (:require [sugot.lib :as l])
  (:import [org.bukkit.inventory ItemStack]
           [org.bukkit Material Sound]
           [org.bukkit.enchantments Enchantment]
           [org.bukkit.inventory FurnaceRecipe ShapelessRecipe]))

(defn recipes []
  (let [item-stack (doto (ItemStack. Material/MILK_BUCKET 1 (short 0) (byte 1))
                     (.addUnsafeEnchantment Enchantment/DURABILITY 1)
                     (l/set-display-name "Whey"))
        milk->whey (doto (ShapelessRecipe. item-stack)
                     (.addIngredient Material/MILK_BUCKET))
        item-stack (doto (ItemStack. Material/SUGAR 1)
                     (.addUnsafeEnchantment Enchantment/DURABILITY 1)
                     (l/set-display-name "Ricotta"))
        whey->ricotta (FurnaceRecipe. item-stack
                                      Material/MILK_BUCKET
                                      (byte 1))]
    [milk->whey whey->ricotta]))

(defn PlayerBucketEmptyEvent [event]
  (let [player (.getPlayer event)
        item-stack (.getItemStack event)]
    (when (= "Whey" (some-> item-stack .getItemMeta .getDisplayName))
      (.setCancelled event true))))
