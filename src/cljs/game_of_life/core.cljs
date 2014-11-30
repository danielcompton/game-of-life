(ns game-of-life.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(def app-state (atom {:text "Hello dog Chestnut!"
                      :grid [[0 1 2 3 4 5] [6 7 8 9 10] [11 12 13 14 15 16]]
                      :columns 5
                      :rows 5}))

(defn flex-item [cell]
  (om/component
    (dom/div #js {:class "flex-item"}
             cell)))

(defn flex-column [rows]
  (om/component
    (dom/div #js {:class "column"}
             (om/build-all flex-item rows))))

(defn app [app owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:class "flex-container"}
               (om/build-all flex-column (:grid app))
               ))))

(defn main []
  (om/root
    app
    app-state
    {:target (. js/document (getElementById "app"))}))


