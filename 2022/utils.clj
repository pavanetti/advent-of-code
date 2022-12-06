(import '(java.io BufferedReader FileReader))

(defn process-file
  [file-name func]
    (with-open [rdr (BufferedReader. (FileReader. file-name))]
      (doall
        (map func (line-seq rdr)))))

(defn int-from-string [str] (Integer/parseInt str))

(defn file-name-from-args
  [args & {:keys [default]}]
    (or (first args) default))

(defn str-transpose
  [xs]
  (apply vector
    (map
      (fn [cs] (string/trim (apply str cs)))
      (apply map vector xs))))
