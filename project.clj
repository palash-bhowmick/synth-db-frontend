(defproject synth-db-frontend "0.14.0"
  :description "The page routing ring handler for caribou"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [http-kit "2.1.12"]
                 [caribou/caribou-admin "0.14.0"]
                 [caribou/caribou-api "0.14.0"]
                 [schmetterling "0.0.8"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [com.datomic/datomic-free "0.9.5078"]]
  :plugins [[lein-ring "0.8.6"]
            [caribou/lein-caribou "2.14.0"]
            [lein-cljsbuild "1.0.2"
             :exclusions [fs]]]
  :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n"
             "-Dclojure.compiler.disable-locals-clearing=true"
             "-Xmx512m"
             "-XX:MaxPermSize=128m"
             "-XX:MaxInlineSize=0"]
  :source-paths ["src"]
  :resource-paths ["resources/"]
  :min-lein-version "2.0.0"
  :migration-namespace synth-db-frontend.migrations
  :main synth-db-frontend.core
  :uberjar-name "synth-db-frontend-standalone.jar"
  :profiles {:uberjar {:aot :all}}
  :ring {:handler synth-db-frontend.core/handler
         :init synth-db-frontend.core/init
         :port 33333
         :auto-reload? false
         :servlet-name "synth-db-frontend-frontend"}
  :immutant {:context-path "/"}
  :cljsbuild {:repl-listen-port 44994
              :builds [{:source-paths ["resources/cljs"]
                        :compiler {:output-to "resources/public/js/app/synth-db-frontend.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]})
