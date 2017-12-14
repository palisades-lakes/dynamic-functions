(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.dynafun.core
  
  {:doc "Dynamic functions aka generic functions aka multimethods. 
         Less flexible than Clojure multimethods
         (no hierarchies, class-based only), but much faster. "
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-13"}
  
  (:refer-clojure :exclude [assoc dissoc merge
                            defmethod prefer-method])
  
  (:require [clojure.reflect :as r]
            [clojure.string :as s]
            [clojure.pprint :as pp])
  
  (:import [clojure.lang IFn IMeta]
           [palisades.lakes.dynafun.java 
            Classes DynaFun MetaFn Signature 
            Signature0 Signature2 Signature3 SignatureN ]))
;;----------------------------------------------------------------
;; more efficient meta data wrapper for functions
;; generalize so functions work with all IObj?
;;----------------------------------------------------------------
(defn with ^clojure.lang.IFn [^clojure.lang.IFn f m]
  (MetaFn/withMeta f m))

(defn merge ^clojure.lang.IFn [^clojure.lang.IFn f m]
  (MetaFn/withMeta f (clojure.core/merge (meta f) m)))

(defn assoc ^clojure.lang.IFn [^clojure.lang.IFn f & args]
  (MetaFn/withMeta f (clojure.core/assoc (meta f) args)))

(defn dissoc ^clojure.lang.IFn [^clojure.lang.IFn f & args]
  (MetaFn/withMeta f (clojure.core/dissoc (meta f) args)))
;;----------------------------------------------------------------
;; signatures
;;----------------------------------------------------------------
(defn to-signature 
  
  "Return an appropriate instance of 
   `Signature` for the `Class` valued arguments
    (in the arity 1 case, it just returns the `Class` itself).

   **Warning:** [[to-signature]] can only be used 
   to generate dispatch values for multimethods
   defined with [[palisades.lakes.multimethods.core/defmulti]]."
  
  {:added "faster-multimethods 0.0.9"}
  
  (^Signature0 [] Signature0/INSTANCE)
  (^Class [^Class c0] c0)
  (^Signature2 [^Class c0 ^Class c1] 
    (Signature2. c0 c1))
  (^Signature3 [^Class c0 ^Class c1 ^Class c2] 
    (Signature3. c0 c1 c2))
  (^SignatureN [^Class c0 ^Class c1 ^Class c2 & cs] 
    (SignatureN. c0 c1 c2 ^clojure.lang.ArraySeq cs)))


(defmacro signature 
  
  "Return an appropriate implementation of `Signature` for the
   arguments, by applying `getClass` as needed.

   `palisades.lakes.dynafun.core/signature` can only be used 
   as a dispatch function with dynafun
   defined with `palisades.lakes.dynafun.core/defmulti`."
  
  ([] Signature0/INSTANCE)
  ([x0] `(Classes/classOf ~(with-meta x0 {:tag 'Object})))
  ([x0 x1] 
    `(Signature2.
       (Classes/classOf ~(with-meta x0 {:tag 'Object}))
       (Classes/classOf ~(with-meta x1 {:tag 'Object}))))
  ([x0 x1 x2] 
    `(Signature3.
       (Classes/classOf ~(with-meta x0 {:tag 'Object}))
       (Classes/classOf ~(with-meta x1 {:tag 'Object}))
       (Classes/classOf ~(with-meta x2 {:tag 'Object}))))
  ([x0 x1 x2 & xs] 
    `(SignatureN/extract 
       ~x0 ~x1 ~x2 (with-meta ~xs {:tag 'clojure.lang.ArraySeq}))))

;;----------------------------------------------------------------
;; dynamic functions
;;----------------------------------------------------------------
(defmacro dynafun [mm-name options]
  `(def ~(with-meta mm-name options)
     (DynaFun/empty ~(name mm-name))))
;;----------------------------------------------------------------
;; methods
;;----------------------------------------------------------------
(defn- valid-signature? [sig]
  (or (nil? sig) ;; no arg case
      (class? sig) ;; 1 arg case
      (instance? Signature sig)))

(defn add-method ^DynaFun [^DynaFun f  
                           ^Object sig
                           ^IFn method]
  (assert (valid-signature? sig))
  ;; TODO: check arglist of f for consistency
  ;; TODO: (function-signature IFn) --- probably not possible
  ;; for vanilla IFn
  (.addMethod f sig method))

(defmacro defmethod [f args & body]
  (let [classes (mapv #(:tag (meta %) 'Object) args)
        names (mapv #(s/replace (str %) "." "") classes)
        m (symbol (str f "_" (s/join "_" names)))]
    `(alter-var-root 
       (var ~f) 
       add-method 
       (to-signature ~@classes)
       (fn ~m ~args ~@body))))
;;----------------------------------------------------------------
;; preferences
;;----------------------------------------------------------------
(defn- valid-preference? [signature0 signature1]
  (cond
    (= signature0 signature1) 
    false
    
    (and (class? signature0) 
         (class? signature1)
         (not (Classes/isAssignableFrom 
                ^Class signature0 ^Class signature1))
         (not (Classes/isAssignableFrom 
                ^Class signature1 ^Class signature0))) 
    true
    
    (and (instance? Signature signature0)
         (instance? Signature signature1)
         (not (.isAssignableFrom 
                ^Signature signature0 ^Signature signature1))
         (not (.isAssignableFrom 
                ^Signature signature1 ^Signature signature0)))
    true
    
    :else false))

(defn prefer-method ^DynaFun [^DynaFun f 
                              ^Object signature0 
                              ^Object signature1]
  ;; TODO: check arglist of f for consistency
  ;; TODO: (function-signature IFn) --- probably not possible
  ;; for vanilla IFn
  (assert (valid-preference? signature0 signature1)
          (pr-str "Can't prefer" signature0 
                  "to" signature1))
  (.preferMethod f signature0 signature1))

(defmacro defpreference [f signature0 signature1]
  `(alter-var-root 
     (var ~f) prefer-method ~signature0 ~signature1))
;----------------------------------------------------------------

