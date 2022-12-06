(ns ch05)
(require '[clojure.string :as string])

(load-file "utils.clj")

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

(defn process-config-line
  [line]
  (letfn [(mark-spaces [line]
            (string/replace line #"(^|\s)\s{3}" "$1[.]"))
          (remove-brackets [line]
            (string/replace line #"\[|\]" ""))
          (remove-gaps [line]
            (string/replace line #"\s" ""))
          (remove-dot-marks [line]
            (string/replace line #"\." " "))]
    (remove-dot-marks
      (remove-gaps
        (remove-brackets
          (mark-spaces line))))))

(defn process-procedure-line
  [line]
  (let [proc-regex #"move (?<move>[0-9]+) from (?<from>[0-9]+) to (?<to>[0-9]+)"
        matcher (re-matcher proc-regex line)]
    (if (.matches matcher)
      {:move (Integer/parseInt (.group matcher "move"))
      :from (Integer/parseInt (.group matcher "from"))
      :to (Integer/parseInt (.group matcher "to"))})))

(defn split-by-config-and-procedure
  [lines]
  (letfn [(still-config? [s] (not (string/starts-with? s " 1")))]
    (let [[config remaining] (split-with still-config? lines)
          procedure (drop 2 remaining)]
      [config procedure])))

(defn parse-input
  [lines]
  (let [[config-lines procedure-lines] (split-by-config-and-procedure lines)]
    [
      (str-transpose (map process-config-line config-lines))
      (map process-procedure-line procedure-lines)]))

(defn config-and-procedure-from-file
  [file-name]
  (parse-input (process-file file-name identity)))

(defn top-crates-from-execution
  [stacks-config procedure challenge-part]
  (apply str
    (top-crates
      (execute-procedure
        (machine-version-from-part challenge-part)
        stacks-config
        procedure))))

(defn find-answer
  [challenge-part file-name]
  (let [[stacks-config procedure]
          (config-and-procedure-from-file file-name)]
    (top-crates-from-execution
      stacks-config procedure challenge-part)))

(defn -main [& args]
  (let [part-arg (or (first args) "1")
        part (Integer/parseInt part-arg)
        file-name (file-name-from-args
                    (drop 1 args)
                    :default "input/ch05.txt")]
    (println (find-answer part file-name))))
