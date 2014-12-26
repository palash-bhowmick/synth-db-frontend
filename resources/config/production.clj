{:logging {:loggers [{:type :stdout :level :warn}
                     ;; {:type :remote :host "beast.local" :level :debug}
                     ;; {:type :file :file "caribou-logging.out" :level :warn}
                     ]}
 :database {:classname    "org.postgresql.Driver"
            :subprotocol  "postgresql"
            :host         "localhost"
            :database     "synth_db_frontend_production"
            :user         "postgres"
            :password     "postgres"}
 :controller {:namespace "synth-db-frontend.controllers" :reload :never}
 :cache-templates :always}
