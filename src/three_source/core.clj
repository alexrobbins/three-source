(ns three-source.core
  (:use compojure.core
        [hiccup.page-helpers :only [html5 link-to url]]
        [hiccup.core :only [html defelem]]
        [hiccup.form-helpers :only [form-to text-field submit-button]]
        [ring.util.response :only [redirect]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))


; Template helpers
(defn layout [title content]
  (html5
    [:head [:title title]]
    [:body
     (link-to
       "/"
       [:img {:src "/3sourceSmall.png"}])
     [:h1 title]
     content]))

(defn who-did-what
  ([] (who-did-what "" "" "" ""))
  ([e1 pred e2 prep]
   (html 
     [:div {:class "who-did-what"}
      (form-to [:get "/results"]
               [:table
                [:tr
                 (map #(vector :th %) ["who" "did" "what" "prep"])]
                [:tr
                 [:td (text-field "e1" e1)]
                 [:td (text-field "pred" pred)]
                 [:td (text-field "e2" e2)]
                 [:td (text-field "prep" prep)]
                 [:td (submit-button "Go")]]])]
     [:div {:class "samples"}
      (link-to (url "/results" {:e1 "zombies" :pred "eat"})
               "What do zombies eat?")])))


; View functions

(defn home []
  (layout "3Source"
          (who-did-what)))

(defn results [e1 pred e2 prep]
  (if (every? empty? [e1 pred e2 prep])
    (redirect "/")
    (layout "Results of your query"
            [:p 
             (interpose ", "
                        [e1 pred e2 prep])])))


; Routes

(defroutes main-routes
           (GET "/" [] (home))
           (GET "/results" [e1 pred e2 prep] (results e1 pred e2 prep))
           (route/resources "/")
           (route/not-found "Page not found"))

(def app
  (handler/site main-routes))
