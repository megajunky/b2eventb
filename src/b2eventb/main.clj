(ns b2eventb.main
  (:require [clojure.java.io :refer [make-parents]]
            [lisb.translation.util :refer [b->ir]]
            [lisb.translation.eventb.util :refer [ir->prob-model prob-model->rodin]]
            [lisb.translation.eventb.b2eventb :refer [extract-machine extract-context]])
  (:gen-class))

(defn show-usage []
  (println "Usage:\n\tb2eventb [B-FILE] [PROJECT-NAME] [OUTPUT-PATH]"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (if (not= 3 (count args))
    (show-usage)
    (let [[b-machine-file project-name output-path] args]
      (make-parents output-path)
      (as-> (b->ir (slurp b-machine-file)) x
        ((juxt extract-machine extract-context) x)
        (apply ir->prob-model x)
        (prob-model->rodin x project-name output-path)))
  ))
