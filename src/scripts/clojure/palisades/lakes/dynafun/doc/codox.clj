(set! *warn-on-reflection* false)
(set! *unchecked-math* false)
;;----------------------------------------------------------------
(ns palisades.lakes.dynafun.codox
  
  {:doc "Generate codox for dynamic-functions."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-08-05"
   :version "2017-09-24"}
  
  (:require [clojure.java.io :as io]
            [codox.main :as codox]))
;;----------------------------------------------------------------
#_(set! *warn-on-reflection* true)
#_(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(let [version "0.0.7"
      project-name "dynamic-functions"
      description "Dynamic functions aka generic functions aka multimethods."
      options {:name project-name
               :version version 
               :description description
               :language :clojure
               :root-path (io/file "./")
               :output-path "docs/codox"
               :source-paths ["src/main/clojure"]
               :source-uri (str "https://github.com/palisades-lakes/"
                                project-name
                                "/tree/"
                                project-name
                                "-{version}/{filepath}#L{line}")
               :namespaces :all
               :doc-paths ["docs"]
               :doc-files ["README.md"]
               :html {:namespace-list :flat}
               ;;:exclude-vars #"^(map)?->\p{Upper}"
               :metadata {:doc "TODO: write docs"
                          :doc/format :markdown}
               :themes [:default]}]
  (codox/generate-docs options))
;;----------------------------------------------------------------

