(ns b2eventb.main
  (:require [clojure.java.io :refer [make-parents file]]
            [clojure.string :as string]
            [lisb.translation.util :refer [b->ir]]
            [lisb.translation.eventb.util :refer [ir->prob-model prob-model->rodin]]
            [lisb.translation.eventb.b2eventb :refer [extract-machine extract-context]])
  (:gen-class))

(defn show-usage []
  (println "Usage: b2eventb B-FILE OUTPUT-PATH"))


(defn b-ir->eventb-ir [ir]
  (println "Converting B IR to Event-B IR ...")
  [(extract-machine ir) (extract-context ir)])

(defn eventb-ir->rodin [irs project-name output-path]
  (println "Creating Rodin Project at:" (str output-path "/" project-name))
  (assert (not (seq? irs)) "Expects a list of machines and contexts")
  (prob-model->rodin (apply ir->prob-model irs) project-name output-path))

(defn file-with-extension? [^java.io.File file extensions]
  (and (.isFile file)
       (some #(.endsWith (.getName file) %) extensions)))

(defn file-basename [^java.io.File file] (first (string/split (.getName file) #"\." 2)))

(defn normalize [project-name] (apply str (re-seq #"[a-zA-Z_]" project-name)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (if (= 2 (count args))
    (let [b-file (file (first args))
          project-name (normalize (file-basename b-file))
          output-path (second args)]
      (assert (file-with-extension? b-file [".mch"]) ".mch extension required")
      (make-parents output-path)
      (println "Reading" (.getName b-file))
      (-> b-file
          slurp
          b->ir
          b-ir->eventb-ir
          (eventb-ir->rodin project-name output-path)))

    (show-usage)))

