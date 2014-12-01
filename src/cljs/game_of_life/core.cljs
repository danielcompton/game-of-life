(ns game-of-life.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [game-of-life.game :as game]))

(enable-console-print!)

(def app-state (atom {:size          5
                      :board         [0 0 0 0 0
                                      0 0 0 0 0
                                      0 0 1 1 0
                                      0 0 0 0 0
                                      0 0 0 0 0]
                      :click-counter 5}))

(defn btn-view [cursor _]
  (reify om/IRender
    (render [_]
      (dom/div nil
               (dom/span nil (do (println (get cursor :click-counter))
                                 (println cursor)
                                 (str "Clicked " (get cursor :click-counter) " times")))
               (dom/button
                 #js {:onClick (fn [_]
                                 #_(om/transact! cursor :click-counter inc)
                                 (om/transact! cursor :board game/play))}
                 "Play")))))

(defn flex-item [cell]
  (om/component
    (dom/div #js {:className (if (zero? cell) "flex-item" "flex-item alive")}
             cell)))

(defn flex-row [cells]
  (om/component
    (apply dom/div #js {:className "row"}
           (om/build-all flex-item cells))))

(defn grid [app]
  (reify
    om/IRender
    (render [_]
      (apply dom/div #js {:className "flex-container"}
             (let [size (app :size)
                   start (app :board)]
               (om/build-all flex-row (partition size start)))))))

(defn app [app owner]
  (reify
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



;(def app-state
;  (atom
;    {:widgets
;     [{:my-number 16
;       :text "I'm divisible by 2!"
;       :grid [1 1 3 5]}
;      {:my-number 23
;       :text "I'm not divisible by 2!"}]}))
;
;(defmulti even-odd-widget (comp even? :my-number))
;
;(defmethod even-odd-widget true
;  [props owner]
;  (reify
;    om/IRender
;    (render [_]
;      (dom/div nil
;               (dom/h2 nil "Even Widget")
;               (dom/p nil (:text props))
;               (dom/p nil (pr-str  (:grid props)))))))
;
;(defmethod even-odd-widget false
;  [props owner]
;  (reify
;    om/IRender
;    (render [_]
;      (dom/div nil
;               (dom/h2 nil "Odd Widget")
;               (dom/p nil (:text props))))))
;
;(defn app [props owner]
;  (reify
;    om/IRender
;    (render [_]
;      (apply dom/div nil
;             (om/build-all even-odd-widget (:widgets props))))))



(defn main []
  (om/root
    app
    app-state
    {:target (. js/document (getElementById "app"))}))
