(ns b2eventb.b2eventb
  (:require [clojure.java.io :refer [make-parents]]
            [clojure.string :as string]
            [lisb.translation.util :refer [b->ir]]
            [lisb.translation.irtools :refer [replace-all-definitions]]
            [lisb.translation.eventb.util :refer [ir->prob-model prob-model->rodin]]
            [lisb.translation.eventb.b2eventb :refer [extract-machine extract-context]]))

(defn b-ir->eventb-ir [ir]
  [(extract-machine ir) (extract-context ir)])

(defn eventb-ir->rodin [irs project-name output-path]
  (assert (not (seq? irs)) "Expects a list of machines and contexts")
  (prob-model->rodin (apply ir->prob-model irs) project-name output-path))

(defn file-with-extension? [^java.io.File file extensions]
  (and (.isFile file)
       (some #(.endsWith (.getName file) %) extensions)))

(defn file-basename [^java.io.File file] (first (string/split (.getName file) #"\." 2)))

(defn normalize [project-name] (apply str (re-seq #"[a-zA-Z_]" project-name)))

(defn log [value verbose msg]
  (when verbose (println msg))
  value)

(defn translate-single-file [^java.io.File file output-path verbose?]
    (let [project-name (normalize (file-basename file))]
      (assert (file-with-extension? file [".mch"]) (str ".mch extension required. Given: " (.getName file)))
      (make-parents output-path)
      (log nil verbose? (str "Reading " (.getName file)))
      (-> file
          slurp
          b->ir
          replace-all-definitions
          (log verbose? "Converting B IR to Event-B IR ...")
          b-ir->eventb-ir
          (log verbose? (str "Creating Rodin Project at: " output-path "/" project-name))
          (eventb-ir->rodin project-name output-path))))


