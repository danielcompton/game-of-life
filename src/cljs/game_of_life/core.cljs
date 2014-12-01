(ns game-of-life.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [game-of-life.game :as game]
            [cljs.core.async :as async :refer [<! >! timeout chan]])
  (:require-macros [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)

(def app-state (atom {:size  10
                      :board (vec (repeatedly 100 #(rand-int 2)))

                      #_[0 0 0 1 1 1 0 0 0 0
                              0 0 0 0 0 0 0 0 0 0
                              0 0 0 0 0 0 0 0 0 0
                              0 0 0 0 1 0 0 0 0 0
                              0 0 0 1 1 1 0 0 0 0
                              0 0 0 0 0 0 1 0 0 0
                              0 0 0 0 0 0 0 0 0 0
                              0 0 0 0 1 1 0 0 0 0
                              0 0 0 0 1 0 0 0 0 0
                              0 0 0 0 0 0 0 0 0 0
                              0 0 0 0 0 0 0 0 0 0]
                      :playing true}))

(defn play-pause [app]
  (println "App:" app)
  (update-in app [:playing] false?))

(defn btn-view [cursor]
  (reify
    om/IDisplayName
    (display-name [_] "Button view")
    om/IWillMount
    (will-mount [_]
      (let [c (chan)]
        (go (while (:playing @cursor)
              (let [_ (<! (timeout 500))]
                (om/transact! cursor game/play))))))
    om/IRender
    (render [_]
      (dom/div nil
               (dom/button
                 #js {:onClick (fn [_] (om/transact! cursor game/play))}
                 "Forward")
               (dom/button
                 #js {:onClick (fn [_] (om/transact! cursor play-pause))}
                 "Play")))))

(defn cell [cell]
  (om/component
    (dom/div #js {:className (if (zero? cell) "flex-item" "flex-item alive")}
             nil)))

(defn row [cells]
  (om/component
    (apply dom/div #js {:className "row"}
           (om/build-all cell cells))))

(defn grid [app]
  (reify
    om/IDisplayName
    (display-name [_] "Game grid")
    om/IRender
    (render [_]
      (apply dom/div #js {:className "flex-container"}
             (let [size (app :size)
                   start (app :board)]
               (om/build-all row (partition size start)))))))

(defn app [app owner]
  (reify
    om/IDisplayName
    (display-name [_] "App")
    om/IRender
    (render [_]
      (dom/div nil
               (om/build grid app)
               (om/build btn-view app)))))

(defn main []
  (om/root
    app
    app-state
    {:target (. js/document (getElementById "app"))}))
