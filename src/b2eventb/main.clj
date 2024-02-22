(ns b2eventb.main
  (:require [clojure.java.io :refer [make-parents file]]
            [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as string]
            [b2eventb.b2eventb :refer [translate-single-file]])
  (:gen-class))

(def cli-options
  [["-o" "--output OUTPUT"]
   ["-v" "--verbose" :default false]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["Usage: b2eventb [options] files ..."
        ""
        "Options:"
        options-summary]
       (string/join \newline)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{:keys [options arguments errors summary] :as parsed} (parse-opts args cli-options)]
    (cond
      ;; Help Message
      (:help options)
      (exit 0 (usage summary))

      ;; Translate Single File
      (= (count arguments) 1)
      (translate-single-file (file (first arguments)) (:output options) (:verbose options))

      ;; Translate Multiple Files
      (> (count arguments) 1)
      (doseq [f arguments]
        (try
          (translate-single-file (file f) (:output options) (:verbose options))
          (catch Exception e (println (str "Error with in " f ":") (.getMessage e)))))

      ;; Error
      :else
      (exit 1 (usage summary)))))


