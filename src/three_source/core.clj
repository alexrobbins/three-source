(ns three-source.core
  (:use compojure.core
        [cheshire.core :only [parse-string]]
        [hiccup.page-helpers :only [html5 link-to url include-css]]
        [hiccup.core :only [html defelem]]
        [hiccup.form-helpers :only [form-to text-field submit-button]]
        [ring.util.response :only [redirect]])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [clj-http.client :as client]))


; Template helpers
(defn base-template [title & content]
  (html5
    [:head
     [:title title]
     (include-css "/screen.css")]
    [:body content]))

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
                 [:td (submit-button "Go")]]])])))


; View functions

(defn home []
  (base-template "3Source"
                 [:div {:class "title"} [:img {:src "/3source.png"}]]
                 (who-did-what)
                 [:div {:class "samples"}
                  (link-to (url "/results" {:e1 "zombies" :pred "eat"})
                           "What do zombies eat?")]))

(defn results [e1 pred e2 prep]
  (letfn
    [(get-json [e1 pred e2 prep]
               (:body
                 (client/get
                   (str
                     "http://demo.languagecomputer.com/"
                     (url "3source/json"
                          {:e1 e1 :pred pred :e2 e2 :prep prep})))))
     (parse-json [json-str]
                 (parse-string json-str))
     (result-table-row [{s1 "a0" s2 "a1" pred "pred"} cls]
                       [:tr {:class cls}
                        [:td s1]
                        [:td pred]
                        [:td s2]])]
    (if (every? empty? [e1 pred e2 prep])
      (redirect "/")
      (base-template "Results"
                     (link-to "/" [:img {:src "/3sourceSmall.png"}])
                     (who-did-what e1 pred e2 prep)
                     (let [data (parse-json
                                  (get-json e1 pred e2 prep))
                           result-count (count data)]
                       [:div {:class "results"}
                        [:p (str result-count
                                 (if (= result-count 1)
                                   " result"
                                   " results"))]
                        (if (> result-count 0)
                          [:table
                           (map result-table-row data
                                (cycle ["odd" "even"]))]
                          [:p
                           "No results. Try searching with fewer terms."])])))))


; Routes

(defroutes main-routes
           (GET "/" [] (home))
           (GET "/results" [e1 pred e2 prep] (results e1 pred e2 prep))
           (route/resources "/")
           (route/not-found "Page not found"))

(def app
  (handler/site main-routes))
