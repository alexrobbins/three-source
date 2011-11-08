(defproject three-source "1.0.0-SNAPSHOT"
            :description "FIXME: write description"
            :dependencies [[cheshire "2.0.2"]
                           [clj-http "0.2.3"]
                           [org.clojure/clojure "1.2.1"]
                           [ring/ring-core "1.0.0-RC1"]
                           [ring/ring-jetty-adapter "1.0.0-RC1"]
                           [ring-reload-modified "0.1.1"]
                           [compojure "0.6.3"]
                           [hiccup "0.3.7"]]
            :dev-dependencies [[org.clojars.autre/lein-vimclojure "1.0.0"]
                               [vimclojure/server "2.2.0"]]
            :main three-source.server)
