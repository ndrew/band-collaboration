(defproject server "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [io.pedestal/pedestal.service "0.2.0-SNAPSHOT"]
                 ; in order to deploy to war jetty — comment the like below, or use make-war.sh
                 [io.pedestal/pedestal.jetty "0.2.0-SNAPSHOT"]
                 ;; auto-reload changes
                 [ns-tracker "0.2.1"]

                 ;; Logging
                 [ch.qos.logback/logback-classic "1.0.7" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.2"]
                 [org.slf4j/jcl-over-slf4j "1.7.2"]
                 [org.slf4j/log4j-over-slf4j "1.7.2"]]
  :aliases {"run-dev" ["trampoline" "run" "-m" "dev"]}
  :jvm-opts ["-Xms256m"]
  :main ^{:skip-aot true} server.server
  :min-lein-version "2.0.0"
  :profiles {:dev {:source-paths ["dev"]}}
  :resource-paths ["config", "resources"]
)
