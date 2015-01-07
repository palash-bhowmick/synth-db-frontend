(ns synth-db-frontend.controllers.tables
  (:use [schmetterling.core :only (debugger)])
  (:require [caribou.model :as model]
            [caribou.app.controller :as controller]
            [datomic.api :as d]))

(def datomic-uri "datomic:free://192.168.33.161:4334/test136")
(def conn (d/connect datomic-uri))
(def db (d/db conn))

(defn get-query-col [col-ident unique-attribute]
  (if (= (first col-ident) (first unique-attribute))
    nil
    [(symbol (str "(get-else $ ?e " (first col-ident) " " \" \" ")")) (symbol (str '? (name (first col-ident))))]
    )
  )

(defn get-find [col-ident]
  (symbol (str " " '? (name (first col-ident))))
  )

(defn get-col-attr-vector [table-name]
  (into [] (d/q (str "[:find ?col-name ?ident
                                    :where [?e :db/ident ?ident]
                                    [(namespace ?ident) ?ns]
                                    [(.startsWith ?ns \"table.\")]
                                    [(name ?ident) ?col-name]
                                    [(.substring ?ns 6) ?table-name]
                                    [(= ?table-name \"" table-name "\" )]
                                    ]") db)
    )
  )

(defn get-columns [table-name]
  (into [] (map #(first %1) (get-col-attr-vector table-name))
    )
  )

(defn get-unique-attrib [table-name]
  (d/q (str "[:find ?doc1
        :where [?e :db/unique ?doc]
        [?e :db/ident ?doc1]
        [(namespace ?doc1) ?doc4]
        [(= ?doc4 \"" table-name "\") ?doc3]
        [(= ?doc3 true)]
        ]") db))

(defn get-datalog [table-name]
  (let [datalog (into [] (map #(rest %1) (get-col-attr-vector table-name)))
        unique-attribute (get-unique-attrib table-name)]

    [':find (symbol (apply str (map #(get-find %1) datalog)))
     ':where
     (if (= (count unique-attribute) 0)
       (symbol (apply str "[?e " (first (first datalog)) " " (symbol (str '? (name (first (first datalog))))) "]"))
       (symbol (apply str "[?e " (first unique-attribute) " " (symbol (str '? (name (first unique-attribute)))) "]")))
     (symbol (apply str (map #(get-query-col %1 unique-attribute) (rest datalog))))]
    ))

(defn get-table-data [table-name]
  (let [da-query (str (get-datalog table-name))]
    (into [] (d/q da-query db))
    )
  )

(defn get-table-names []
  (into [] (map #(first %1) (into [] (d/q (str "[:find ?table-name
                       :where
                       [?e :db/ident ?ident]
                       [(namespace ?ident) ?ns]
                       [(.startsWith ?ns \"table.\")]
                       [(name ?ident) ?col-name]
                       [(.substring ?ns 6) ?table-name]
                      ]") db)
                              )))
  )

(defn get-formatted-uri [uri]
  (if (.endsWith (.trim uri) "/")
    (.substring uri 0 (- (.length uri) 1))
    uri))

(defn table-details
  [request]
  (controller/render
    (assoc request
      :table-name (:name (:params request))
      :table-data (get-table-data (:name (:params request)))
      :table-head (get-columns (:name (:params request)))
      )
    )
  )

(defn tables
  [request]
  (controller/render
    (assoc request
      :tables (get-table-names)
      :uri (get-formatted-uri (:uri request))
      )
    )
  )
