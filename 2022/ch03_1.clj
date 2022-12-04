(require '[clojure.set :as set])

(load-file "utils.clj")

(defn repeated-items
  [fst-seq snd-seq]
  (apply list
    (set/intersection
      (set fst-seq)
      (set snd-seq))))

(defn wrong-item
  [seq]
  (let [size (count seq)
        middle (/ size 2)]
    (first
      (apply repeated-items
        (split-at middle seq)))))

(defn item-priority
  [item-as-char]
  (let [num-value (int item-as-char)
        lower-a (int \a)
        upper-a (int \A)]
    (cond
      (>= num-value lower-a)
        (inc (- num-value lower-a))
      :else
        (+ 27 (- num-value upper-a)))))

(defn priority-sum-of-wrong-items
  [rucksacks]
  (apply +
    (map
      (fn [sack] (item-priority (wrong-item sack)))
      rucksacks)))

(defn rucksacks-from-file
  [file-name]
  (process-file file-name identity))

(defn find-answer
  [args]
  (priority-sum-of-wrong-items
    (rucksacks-from-file
      (file-name-from-args args
        :default "ch03_1.txt"))))

(defn -main
  []
  (println (find-answer *command-line-args*)))
