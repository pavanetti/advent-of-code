(ns ch03_2)
(require '[clojure.set :as set])
(load-file "ch03_1.clj")
(use '[ch03_1 :exclude (find-answer -main)])

(defn group-badge
  [group-sacks]
  (first
    (apply set/intersection
      (map set group-sacks))))

(defn groups-from-rucksacks
  [rucksacks]
  (partition 3 rucksacks))

(defn priority-sum-of-groups-badges
  [rucksacks]
  (let [groups (groups-from-rucksacks rucksacks)
        badges (map group-badge groups)
        priorities (map item-priority badges)]
    (apply + priorities)))

(defn find-answer
  [args]
  (priority-sum-of-groups-badges
    (rucksacks-from-file
      (file-name-from-args args
        :default "input/ch03_1.txt"))))

(defn -main
  [& args]
  (println (find-answer args)))
