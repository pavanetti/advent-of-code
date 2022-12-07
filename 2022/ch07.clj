(ns ch07
  (:import [java.io BufferedReader FileReader])
  (:require [clojure.string :as s]
            [clojure.zip :as z]))

(defrecord File [name size])
(defrecord Dir [name nodes])

(defmulti fs-size class)
(defmethod fs-size File [this] (:size this))
(defmethod fs-size Dir
  [this]
  (if (nil? (:nodes this))
    0
    (reduce + (map fs-size (:nodes this)))))

(defmulti fs-represent
  (fn [obj level] (class obj)))

(defmethod fs-represent File [this level]
  (let [spacing (apply str (repeat (* 2 level) \space))]
    (format "%s- %s (file, size=%d)" spacing (:name this) (:size this))))

(defmethod fs-represent Dir [this level]
  (let [spacing (apply str (repeat (* 2 level) \space))]
    (s/join "\n"
      (cons
        (format "%s- %s (dir)" spacing (:name this))
        (for
        [node (:nodes this)]
          (fs-represent node (inc level)))))))

(defn is-dir? [node] (instance? Dir node))

(defn fs-zip
  [dir]
  (z/zipper
    is-dir?
    :nodes
    (fn [node children] (Dir. (:name node) children))
    dir))

(defn new-file-system [] (fs-zip (Dir. "/" nil)))

(defn change-dir
  [fs dir]
  (let [contains-dir?
          (some
            (fn [child] (= dir (:name child)))
            (z/children fs))]
    (when contains-dir?
      (loop [child (z/down fs)]
        (if (= dir (:name (z/node child)))
          child
          (recur (z/right child)))))))

(defn- parse-dir-output
  [line]
  (let [name (apply str (drop 4 line))]
    {:output :dir :name name}))

(defn- parse-file-output
  [line]
  (let [[size name] (s/split line #" ")]
    {:output :file
     :name name
     :size (Integer/parseInt size)}))

(defn- parse-change-dir
  [line]
  (let [arg (apply str (drop 5 line))
        target (case arg
                  "/" :root
                  ".." :up
                  arg)]
    {:command :cd :argument target}))

(defn parse-line
  [line]
  (cond
    (s/starts-with? line "$ cd ") (parse-change-dir line)
    (= line "$ ls") {:command :ls}
    (s/starts-with? line "dir ") (parse-dir-output line)
    :else (parse-file-output line)))

(defn- process-command-line
  [fs input]
  (case (:command input)
    :ls
      fs
    :cd (let [dir (:argument input)]
      (case dir
        :root (fs-zip (z/root fs))
        :up (z/up fs)
        (change-dir fs dir)))))

(defn- process-output-line
  [fs input]
  (case (:output input)
    :dir (z/append-child fs (Dir. (:name input) nil))
    :file (z/append-child fs (File. (:name input) (:size input)))))

(defn process-terminal-line
  [fs input]
  (cond
    (contains? input :command) (process-command-line fs input)
    (contains? input :output) (process-output-line fs input)
    :else fs))

(defn parse-input-file
  [file-name]
    (with-open [rdr (BufferedReader. (FileReader. file-name))]
      (->
        (for [line (line-seq rdr)]
          (parse-line line))
        doall)))

(defn process-input-file
  [file-name]
  (let [input (parse-input-file file-name)]
    (->
      (reduce
        process-terminal-line
        (new-file-system)
        input)
      z/root)))

(defn filter-dir-sizes
  [fs func]
  (loop [acc [] node (-> fs fs-zip)]
    (let [node-size (-> node z/node fs-size)]
      (if (z/end? node)
        acc
        (if (and (z/branch? node) (func node-size))
          (recur (cons node-size acc) (z/next node))
          (recur acc (z/next node)))))))

(defn smallest-dir-to-free-up-space
  [fs]
  (let [total-size 70000000
        update-size 30000000
        free-size (- total-size (fs-size fs))
        needed-space (- update-size free-size)]
    (apply min
      (filter-dir-sizes fs
        #(>= % needed-space)))))

(defn find-answer
  [part file-name]
  (let [fs (process-input-file file-name)]
    (case part
      1 (reduce + (filter-dir-sizes fs #(< % 100000)))
      2 (smallest-dir-to-free-up-space fs))))

(defn -main
  [& args]
  (let [part (-> args first (or "1") Integer/parseInt)]
    (println (find-answer part "input/ch07.txt"))))
;; (->
;;   (new-file-system)
;;   (z/append-child (Dir. "a" nil))
;;   (z/append-child (File. "b.txt" 14848514))
;;   (z/append-child (File. "c.dat" 8504156))
;;   (z/append-child (Dir. "d" nil))
;;   (change-dir "a")
;;   (z/append-child (Dir. "e" nil))
;;   (z/append-child (File. "f" 29116))
;;   (z/append-child (File. "g" 2557))
;;   (z/append-child (File. "h.lst" 62596))
;;   (change-dir "e")
;;   (z/append-child (File. "i" 584))
;;   z/up
;;   z/up
;;   (change-dir "d")
;;   (z/append-child (File. "j" 4060174))
;;   (z/append-child (File. "d.log" 8033020))
;;   (z/append-child (File. "d.ext" 5626152))
;;   (z/append-child (File. "k" 7214296))
;;   z/root
;;   (fs-represent 0)
;;   println)
