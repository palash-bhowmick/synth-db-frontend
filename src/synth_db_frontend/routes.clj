(ns synth-db-frontend.routes
  (:require [caribou.app.pages :as pages]))

(def routes
  [["/" :home {:GET {:controller 'home :action 'home :template "home.html"}}]
   ["/table" :tables {:GET {:controller 'tables :action 'tables :template "tables.html"}}]
   ["/table/:name" :table-details {:GET {:controller 'tables :action 'table-details :template "table_details.html"}}]
   ])

(defn build-routes
  [routes namespace]
  (pages/bind-actions routes namespace))

(defn gather-pages
  []
  (try 
    (pages/all-pages)
    (catch Exception e nil)))
