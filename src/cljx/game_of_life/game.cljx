(ns game-of-life.game)

(defn foo-cljx [x]
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(def start [0 0 0 0 0
            0 0 0 0 0
            0 1 1 1 0
            0 0 0 0 0
            0 0 0 0 0])

(defn live-or-die [neighbor-count]
  (case neighbor-count
    2 1
    3 1
    0))

(defn neighbors
  "Returns the indexes of a cells neighbors in a square array of size `size`"
  [size idx]
  (let [keyseq [(inc 0) (dec size) size (inc size)]
        neighbors (concat (map - keyseq) keyseq)]
    (map (partial + idx) neighbors)))

(defn play
  ([board]
    (play 5 board))
  ([size board]
    (vec (map-indexed (fn [idx _] (->> (neighbors size idx)
                                       (select-keys board)
                                       (vals)
                                       (apply +)
                                       (live-or-die))) board))))
