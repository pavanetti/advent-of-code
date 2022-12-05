(load-file "ch04_1.clj")

(defn overlaps?
  [[lower-a upper-a]
   [lower-b upper-b]]
  (and
    (<= lower-a upper-b)
    (<= lower-b upper-a)))


(defn find-answer
  [args]
  (count-from-file-by overlaps?
    (file-name-from-args args
      :default "ch04_1.txt")))

(defn -main
  []
  (println
    (find-answer *command-line-args*)))
