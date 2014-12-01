(ns game-of-life.game)

#+cljs (enable-console-print!)

(defn- harvest [cell neighbor-count]
  (if (zero? cell)
    (if (= 3 neighbor-count) 1 0)
    (case neighbor-count
      2 1
      3 1
      0)))

(defn- neighbors
  "Returns the indexes of a cells neighbors in a square array of size `size`"
  [size idx]
  (let [keyseq [(inc 0) (dec size) size (inc size)]
        neighbors (concat (map - keyseq) keyseq)]
    (map (partial + idx) neighbors)))

(defn play
  ([{:keys [board size] :as app}]
    (conj app
          [:board (vec (map-indexed (fn [idx cell] (->> (neighbors size idx)
                                                        (select-keys board)
                                                        (vals)
                                                        (apply +)
                                                        (harvest cell)))
                                    board))])))
