(require '[clojure.string :as string])

(load-file "utils.clj")

(def opponent-play {"A" :rock, "B" :paper, "C" :scissors})
(def your-play     {"X" :rock, "Y" :paper, "Z" :scissors})
(def choice-points {:rock 1, :paper 2, :scissors 3})
(def result-points {:lost 0, :draw 3, :won 6})
(def wins-from {:rock :paper, :paper :scissors, :scissors :rock})

(defn round-result
  [fst snd]
  (let [winning-choice (wins-from fst)]
    (cond
      (= fst snd) :draw
      (= snd winning-choice) :won
      :else :lost)))

(defn round-score
  [fst snd]
  (let [result (round-result fst snd)
        points-from-result (result-points result)
        points-from-choice (choice-points snd)]
    (+ points-from-choice points-from-result)))

(defn game-score
  [rounds]
  (apply +
    (map (partial apply round-score) rounds)))

(defn game-from-line
  [line]
  (let [[fst snd] (string/split line #" ")]
    [(opponent-play fst) (your-play snd)]))

(defn game-from-file
  [file-name]
  (process-file file-name game-from-line))

(defn exec-game
  [args]
  (game-score
    (game-from-file
      (file-name-from-args args
        :default "ch02_1.txt"))))

(defn -main
  []
  (println (exec-game *command-line-args*)))
