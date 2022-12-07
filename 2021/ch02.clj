(ns ch02
  (:import [java.io BufferedReader FileReader])
  (:require [clojure.string :as string]))

(def my-ns *ns*)

(def delta {:forward {:x 1 :y  0}
            :up      {:x 0 :y -1}
            :down    {:x 0 :y  1}})

(defn next-position
  [position command size]
  (let [dx ((delta command) :x)
        dy ((delta command) :y)
        next-x (+ (position :x) (* dx size))
        next-y (+ (position :y) (* dy size))]
    (assoc position :x next-x :y next-y)))

(defn part2+up
  [setup units]
  (assoc setup :aim (- (setup :aim) units)))
(defn part2+down
  [setup units]
  (assoc setup :aim (+ (setup :aim) units)))
(defn part2+forward
  [setup units]
  (assoc setup
    :x (+ units (setup :x))
    :y (+ (setup :y) (* units (setup :aim)))))

(defn part2+next-setup
  [setup command units]
  (let [effect-name (str "part2+" (name command))
        effect (ns-resolve my-ns (symbol effect-name))]
    (effect setup units)))

(defn setup-after-plan
  [step-func setup plan]
  (let [split-plan (partial apply vector)
        apply-plan (partial apply step-func)]
    (reduce
      (comp apply-plan split-plan)
      setup plan)))

(defn plan-from-line
  [line]
  (let [[command size] (string/split line #"\s")]
    (list
      (keyword command)
      (Integer/parseInt size))))

(defn plan-from-file
  [file-name]
  (with-open [rdr (BufferedReader. (FileReader. file-name))]
    (doall
      (map plan-from-line (line-seq rdr)))))

(defn find-answer
  [part file-name]
  (let [initial-position {:x 0 :y 0 :aim 0}
        plan (plan-from-file file-name)
        step-func (if (= 1 part) next-position part2+next-setup)
        final-position (setup-after-plan step-func initial-position plan)]
    (* (final-position :x) (final-position :y))))

(defn -main
  [& args]
  (let [part (Integer/parseInt (or (first args) "1"))
        file-name (or (second args) "ch02.txt")]
    (println
      (find-answer part file-name))))
