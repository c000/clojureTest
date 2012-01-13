(ns helloworld
  (:use [clojure.contrib.duck-streams :only [reader read-lines]]
        [clojure.xml])
  (:require [com.twinql.clojure.http :as http])
  (:import (java.net HttpURLConnection URL)))

(defn -main [& args]
  (let [^HttpURLConnection conn (.openConnection (URL. "http://ext.nicovideo.jp/api/getthumbinfo/sm13657968"))
        xml (xml-seq (parse (.getInputStream conn)))]
    (.disconnect conn)
    {:title (get-single-element xml :title)
     :size_high (Integer/parseInt (get-single-element xml :size_high))
     :size_low  (Integer/parseInt (get-single-element xml :size_low))
     }))

(defn get-single-element [xml target]
  (first
    (for [x xml :when (= target (:tag x))]
      (-> x :content first))))

(defmacro infixr
  ([y] y)
  ([x f & y]
   `(~f ~x (infixr ~@y))))

(defmacro infixl
  ([y] y)
  ([x f y & z]
   `(infixl (~f ~x ~y) ~@z)))

(macroexpand '(infixl 1 + 1 + 1 * 3))
