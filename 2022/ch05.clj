(require '[clojure.string :as string])

(load-file "ch05-input.clj")

(def crate-mover-ordering
  {:crate-mover-9000 reverse
   :crate-mover-9001 identity})

(defn apply-step
  [machine-version stacks {move :move from :from to :to}]
  (let [ordering (crate-mover-ordering machine-version)
        src-stack (nth stacks (dec from))
        dst-stack (nth stacks (dec to))
        [moved-lst remaining-lst] (split-at move src-stack)
        moved (apply str (ordering moved-lst))
        remaining (apply str remaining-lst)]
    (assoc
      (assoc stacks (dec from) remaining)
      (dec to)
      (str moved dst-stack))))

(defn execute-procedure
  [machine-version initial-config steps]
  (reduce (partial apply-step machine-version) initial-config steps))

(defn top-crates
  [configuration]
  (map first configuration))

(def machine-version-from-part
  {1 :crate-mover-9000
   2 :crate-mover-9001})

(defn find-answer
  [challenge-part]
  (apply str
    (top-crates
      (execute-procedure
        (machine-version-from-part challenge-part)
        initial-stacks
        procedure))))
