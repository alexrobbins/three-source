(ns three-source.server
  (:use [ring.middleware.reload-modified]
        [ring.adapter.jetty]
        [three-source.core :only [app]])
  (:gen-class))

(defn run-dev []
  (run-jetty (wrap-reload-modified #'app ["src"]) {:port 8080 :join? false}))

(defn -main []
  (run-jetty #'app {:port 8080}))
