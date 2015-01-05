(ns synth-db-frontend.controllers.tables
  (:use [schmetterling.core :only (debugger)])
  (:require [caribou.model :as model]
            [caribou.app.controller :as controller]
            [datomic.api :as d]))

(def datomic-uri "datomic:free://192.168.33.161:4334/test136")
(def conn (d/connect datomic-uri))
(def db (d/db conn))

(defn get-query-col [col-ident]
  ['?e (first col-ident) (symbol (str '? (name (first col-ident))))]
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

(defn get-datalog [table-name]
  (let [datalog (into [] (map #(rest %1) (get-col-attr-vector table-name)))]

    [':find (symbol (apply str (map #(get-find %1) datalog)))
     ':where
     (symbol (apply str (map #(get-query-col %1) datalog)))]
    ))

(defn get-table-data [table-name]
  (into [] (d/q (str (get-datalog table-name)) db))
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
      )
    )
  )
