(ns ch04_2)
(load-file "ch04_1.clj")
(use '[ch04_1 :exclude (-main)])

(defn overlaps?
  [[lower-a upper-a]
   [lower-b upper-b]]
  (and
    (<= lower-a upper-b)
    (<= lower-b upper-a)))

(defn -main
  [& args]
  (println
    (find-answer overlaps? args)))
