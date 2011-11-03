(ns three-source.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

(defroutes main-routes
  (GET "/" [] "<h1>Hello World Wide Web!")
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site main-routes))
