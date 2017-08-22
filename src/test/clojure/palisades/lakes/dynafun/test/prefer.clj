(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.dynafun.test.prefer
  {:doc "Check DynaFun.prefers(x,y), prefer-method, etc."
   :author "palisades dot lakes at gmail dot com"
   :since "2017-08-12"
   :version "2017-08-18"}
  (:require [clojure.test :as test]
            [palisades.lakes.dynafun.core :as df]))
;; mvn clojure:test -Dtest=palisades.lakes.dynafun.test.prefer
;;----------------------------------------------------------------
;; TODO: replace hierarchy based tests with class-based.
;; prefer-method transitivity bug
;;----------------------------------------------------------------
#_(test/deftest transitivity
  
   (derive ::transitive-d ::transitive-a)
   (derive ::transitive-d ::transitive-c)
  
   (defmulti transitive identity)
   (defmethod transitive ::transitive-a [x] [::transitive-a x]) 
   (defmethod transitive ::transitive-c [x] [::transitive-c x]) 
   (prefer-method transitive ::transitive-a ::transitive-b)
   (prefer-method transitive ::transitive-b ::transitive-c)
   ;; this should not throw an exception
   (test/is 
     #_(= [::transitive-a ::transitive-d] 
          (transitive ::transitive-d))
     (thrown-with-msg? 
       IllegalArgumentException 
       #"Multiple methods in multimethod"
       (= [::transitive-a ::transitive-d] 
          (transitive ::transitive-d))))
  
   (df/defmulti df-transitive identity)
   (df/defmethod df-transitive ::transitive-a [x] [::transitive-a x]) 
   (df/defmethod df-transitive ::transitive-c [x] [::transitive-c x]) 
   (df/prefer-method df-transitive ::transitive-a ::transitive-b)
   (df/prefer-method df-transitive ::transitive-b ::transitive-c)
   (test/is (= [::transitive-a ::transitive-d] 
               (df-transitive ::transitive-d))))
;;----------------------------------------------------------------
;; (prefers x ancestor-of-y) wrongly implies (prefers x y) in 
;; Clojure 1.8.0
;;----------------------------------------------------------------
#_(test/deftest inheritance
   (derive ::pi-b ::pi-a)
   (derive ::pi-e ::pi-d)
   (derive ::pi-c ::pi-b)
   (derive ::pi-c ::pi-e)
  
   (defmulti cpi identity)
   (defmethod cpi ::pi-b [x] [::pi-b x]) 
   (defmethod cpi ::pi-e [x] [::pi-e x]) 
   (prefer-method cpi ::pi-b ::pi-d)
   ;; This should throw the exception
   (test/is 
     (= [::pi-b ::pi-c] (cpi ::pi-c))
     #_(thrown-with-msg? 
         IllegalArgumentException 
         #"Multiple methods in multimethod"
         (= [::pi-b ::pi-c] (fpi ::pi-c))))
  
   (df/defmulti fpi identity)
   (df/defmethod fpi ::pi-b [x] [::pi-b x]) 
   (df/defmethod fpi ::pi-e [x] [::pi-e x]) 
   (df/prefer-method fpi ::pi-b ::pi-d)
   ;; This should throw the exception
   (test/is 
     #_(= [::pi-b ::pi-c] (fpi ::pi-c))
     (thrown-with-msg? 
       IllegalArgumentException 
       #"Multiple methods in multimethod"
       (= [::pi-b ::pi-c] (fpi ::pi-c)))))
;;----------------------------------------------------------------
