(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.dynafun.test.transitivity
  
  {:doc "dummy classes for prefer-method tests."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-09-14"
   :version "2017-09-14"}
  (:require [clojure.test :as test]
            [palisades.lakes.dynafun.core :as d]
            [palisades.lakes.dynafun.test.classes])
  (:import [palisades.lakes.dynafun.test.classes 
            A B C D A0 B0 C0 D0 A1 B1 C1 D1 A2 B2 C2 D2 E2]))
;; mvn clojure:test -Dtest=palisades.lakes.dynafun.test.transitivity
;;----------------------------------------------------------------
;; prefer-method transitivity bugs
;;----------------------------------------------------------------
(test/deftest transitive1
  (d/dynafun transitive1 {})
  (d/defmethod transitive1 [^A x] [A (class x)]) 
  (d/defmethod transitive1 [^C x] [C (class x)]) 
  (d/defpreference transitive1 A B)
  (d/defpreference transitive1 B C)
  (test/is (= [A D] (transitive1 (D.)))))
;;----------------------------------------------------------------
(test/deftest transitive2
  (d/dynafun transitive2 {})
  (d/defmethod transitive2 [^B0 x0 ^B1 x1] 
    [(d/signature B0 B1) (d/extract-signature x0 x1)])
  (d/defmethod transitive2 [^C0 x0 ^C1 x1] 
    [(d/signature C0 C1) (d/extract-signature x0 x1)])
  (d/defpreference transitive2 
                   (d/signature A0 A1) 
                   (d/signature B0 B1))
  (test/is (= [(d/signature C0 C1) (d/signature D0 D1)] 
              (transitive2 (D0.) (D1.)))))
;;----------------------------------------------------------------
;; (prefers x ancestor-of-y) wrongly implies (prefers x y)
;; in Clojure 1.8.0
;;----------------------------------------------------------------
(test/deftest sins-of-the-parents
  (d/dynafun sins-of-the-parents {})
  (d/defmethod sins-of-the-parents [^B2 x] [B2 (class x)]) 
  (d/defmethod sins-of-the-parents [^E2 x] [E2 (class x)]) 
  (d/defpreference sins-of-the-parents B2 D2)
  ;; This should throw the exception, but doesn't in Clojure 1.8.0
  (test/is 
    (thrown-with-msg? 
      IllegalArgumentException 
      #"Multiple methods in multimethod"
      (= [B2 C2] (sins-of-the-parents (C2.))))))
;;----------------------------------------------------------------
