(ns three-source.server
  (:use [ring.middleware.reload-modified]
        [ring.adapter.jetty]
        [three-source.core :only [app]])
  (:gen-class))

(defn -main []
  (run-jetty (wrap-reload-modified #'app ["src"]) {:port 8080}))
