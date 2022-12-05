(require '[clojure.string :as string])

(load-file "ch05-input.clj")

(defn apply-step
  [stacks {move :move from :from to :to}]
  (let [src-stack (nth stacks (dec from))
        dst-stack (nth stacks (dec to))
        [moved-lst remaining-lst] (split-at move src-stack)
        moved (apply str (reverse moved-lst))
        remaining (apply str remaining-lst)]
    (assoc
      (assoc stacks (dec from) remaining)
      (dec to)
      (str moved dst-stack))))

(defn execute-procedure
  [initial-config steps]
  (reduce apply-step initial-config steps))

(defn top-crates
  [configuration]
  (map first configuration))

(defn find-answer
  []
  (apply str
    (top-crates
      (execute-procedure initial-stacks procedure))))
