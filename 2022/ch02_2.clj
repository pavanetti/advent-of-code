(ns ch02_2)
(require '[clojure.set :as set])
(require '[clojure.string :as string])

(load-file "ch02_1.clj")
(use '[ch02_1 :only (wins-from opponent-play exec-game)])

(def lost-to (set/map-invert wins-from))
(def your-play {"X" :lost, "Y" :draw, "Z" :won})

(defn round-choice
  [fst-choice result]
  (let [winning-choice (wins-from fst-choice)
        loosing-choice (lost-to fst-choice)]
    (cond
      (= result :draw) fst-choice
      (= result :won) winning-choice
      (= result :lost) loosing-choice)))

(defn game-from-line-part2
  [line]
    (let [[fst snd] (string/split line #" ")
          opponent-choice (opponent-play fst)
          result (your-play snd)]
      [opponent-choice
       (round-choice opponent-choice result)] ))

(defn -main
  [& args]
  (println (exec-game game-from-line-part2 args)))
