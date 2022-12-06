(ns ch06)
(load-file "utils.clj")

(defn some-repeated?
  [window]
  (some
    (fn [[ch fq]] (> fq 1))
    (frequencies window)))

(defn first-all-distincts-window
  [seq window-size]
  (some
    (fn [[window, idx]] (if-not (some-repeated? window) idx))
    (map list
      (partition window-size 1 seq)
      (range))))

(defn characters-before-marker
  [seq window-size]
  (+ window-size
    (first-all-distincts-window seq window-size)))

(defn signal-from-file
  [file-name]
  (first (process-file file-name identity)))

(defn find-answer
  [part file-name]
  (let [window-size (if (= 2 part) 14 4)]
    (characters-before-marker
      (signal-from-file file-name)
      window-size)))

(defn -main [& args]
  (let [part-arg (or (first args) "1")
        part (Integer/parseInt part-arg)
        file-name (file-name-from-args
                    (drop 1 args)
                    :default "input/ch06.txt")]
    (println (find-answer part file-name))))
