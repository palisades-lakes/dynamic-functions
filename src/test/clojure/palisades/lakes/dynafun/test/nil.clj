(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.dynafun.test.nil
  
  {:doc "Test nil dispatch value and in signatures."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-13"}
  (:require [clojure.test :as test]
            [palisades.lakes.dynafun.core :as d])
  (:import [java.util Collection]
           [clojure.lang IFn]))
;; mvn clojure:test -Dtest=palisades.lakes.multimethods.test.nil
;;----------------------------------------------------------------
(test/deftest class0
  (d/dynafun count0 {})
  (d/defmethod count0 [^{:tag nil} x] 0) 
  (d/defmethod count0 [^Collection x] (.size x))
  (test/is (== 0 (count0 nil)))
  (test/is (== 0 (count0 [])))
  (test/is (== 2 (count0 [:a :b]))))
;;----------------------------------------------------------------
(defn- square 
  (^long [^long x] (* x x))
  ([^long x ^long y] 
    [(* x x) (* y y)])
  ([^long x ^long y ^long z] 
    [(* x x) (* y y) (* z z)]))
;;----------------------------------------------------------------
(test/deftest signature0
  (d/dynafun map0 {})
  (d/defmethod map0 [^{:tag nil} f ^{:tag nil} x0] nil) 
  (d/defmethod map0 [^IFn _ ^{:tag nil} _] nil) 
  (d/defmethod map0 [^{:tag nil} _ ^Collection _] nil) 
  (d/defmethod map0 [^IFn f ^Collection x0] (mapv f x0))
  (d/defmethod map0 [^IFn f ^Collection x0 ^Collection x1] (mapv f x0 x1))
  (d/defmethod map0 [^IFn _ ^{:tag nil} _ ^Collection _] nil)
  (d/defmethod map0 [^IFn _ ^Collection _ ^{:tag nil} _] nil)
  (d/defmethod map0 [^{:tag nil} _ ^{:tag nil} _ ^Collection _] nil)
  (d/defmethod map0 [^IFn _ ^{:tag nil} _ ^{:tag nil} _] nil)
  (d/defmethod map0 [^{:tag nil} _ ^{:tag nil} _ ^{:tag nil} _] nil)
  ;; Doesn't support arity > 3 yet
  #_(d/defmethod map0 [^IFn f ^Collection x0 ^Collection x1 ^Collection x2]     (mapv f x0 x1 x2))
  #_(d/defmethod map0 [^IFn f ^{:tag nil} x0 ^Collection x1 ^Collection x2] nil)
  #_(d/defmethod map0 [^{:tag nil} f ^Collection x0 ^Collection x1 ^Collection x2]     nil)
  #_(d/defmethod map0 [^IFn f ^Collection x0 ^{:tag nil} x1 ^Collection x2] nil)
  #_(d/defmethod map0 [^IFn f ^{:tag nil} x0 ^{:tag nil} x1 ^Collection x2] nil)
  #_(d/defmethod map0 [^IFn f ^{:tag nil} x0 ^{:tag nil} x1 ^{:tag nil} x2] nil)
  #_(d/defmethod map0 [^{:tag nil} f ^{:tag nil} x0 ^{:tag nil} x1 ^{:tag nil} x2] nil)
  (test/is (nil? (map0 square nil)))
  (test/is (nil? (map0 nil nil)))
  (test/is (nil? (map0 square nil [2 4])))
  (test/is (nil? (map0 nil nil [3 5])))
  (test/is (nil? (map0 nil nil nil)))
  (test/is (nil? (map0 square nil nil)))
  (test/is (nil? (map0 square nil [2 4])))
  (test/is (= [[1 4][4 16]] (map0 square [1 2] [2 4])))
  #_(test/is (nil? (map0 nil [1 2] [2 4] [3 5])))
  #_(test/is (nil? (map0 nil nil nil nil)))
  #_(test/is (nil? (map0 square nil nil [3 5])))
  #_(test/is (= [[1 4 9][4 16 25]]
              (map0 square [1 2] [2 4] [3 5])))
  )
;;----------------------------------------------------------------
