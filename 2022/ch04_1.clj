(require '[clojure.string :as string])

(load-file "utils.clj")

(defn range-contains?
  [[lower-a upper-a]
   [lower-b upper-b]]
  (and
    (<= lower-a lower-b)
    (>= upper-a upper-b)))

(defn one-contains-other?
  [range-a range-b]
  (or
    (range-contains? range-a range-b)
    (range-contains? range-b range-a)))

(defn contains-pairs-count
  [range-pairs]
  (count
    (filter
      (partial apply one-contains-other?)
      range-pairs)))

(defn ranges-from-line
  [line]
  (let [[a b] (string/split line #",")
        [lower-a upper-a] (string/split a #"-")
        [lower-b upper-b] (string/split b #"-")]
    [(map int-from-string [lower-a upper-a])
     (map int-from-string [lower-b upper-b])]))

(defn ranges-from-file
  [file-name]
  (process-file file-name ranges-from-line))

(defn find-answer
  [args]
  (contains-pairs-count
    (ranges-from-file
      (file-name-from-args args
        :default "ch04_1.txt"))))

(defn -main
  []
  (println
    (find-answer *command-line-args*)))
