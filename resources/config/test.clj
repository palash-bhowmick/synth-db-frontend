{:logging {:loggers [{:type :stdout :level :debug}
                     ;; {:type :remote :host "beast.local" :level :debug}
                     ;; {:type :file :file "caribou-logging.out" :level :debug}
                     ]}
 :app {:use-database true}
 :database {:classname    "org.h2.Driver"
            :subprotocol  "h2"
            :protocol     "file"
            :path         "./"
            :database     "synth_db_frontend_test"
            :host         "localhost"
            :subname      "file:synth_db_frontend_test"
            :user         "h2"
            :password     ""}
 :controller {:namespace "synth-db-frontend.controllers" :reload :always}
 :nrepl {:port 44444}
 :cache-templates :never}
