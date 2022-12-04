(use '[clojure.set :only (map-invert)])
(load-file "ch02_1.clj")

(def lost-to (map-invert wins-from))
(def your-play {"X" :lost, "Y" :draw, "Z" :won})

(defn round-choice
  [fst-choice result]
  (let [winning-choice (wins-from fst-choice)
        loosing-choice (lost-to fst-choice)]
    (cond
      (= result :draw) fst-choice
      (= result :won) winning-choice
      (= result :lost) loosing-choice)))

(defn game-from-line
  [line]
    (let [[fst snd] (split line #" ")
          opponent-choice (opponent-play fst)
          result (your-play snd)]
      [opponent-choice
       (round-choice opponent-choice result)] ))

(defn -main [] (exec-game *command-line-args*))
