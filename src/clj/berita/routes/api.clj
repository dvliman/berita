(ns berita.routes.api
  (:require [struct.core :as st]
            [berita.db.news :as repo]))

(def news-schema
  [[:title st/required st/string]
   [:image_url st/required st/string]
   [:category st/required st/string]
   [:source_url st/required st/string]
   [:source_name st/required st/string]])

(defn validate-news [news]
  (first (st/validate news news-schema)))

(defn create-news [{:keys [params]}]
  (if-let [errors (validate-news params)]
    {:status  400
     :headers {}
     :body    {:errors errors}}
    {:status  200
     :headers {}
     :body    (merge {:id (first (repo/create-news! params))} params)}))

(defn api-routes []
  [""
   ["/news/create-news" {:post create-news}]
   ["/news/delete-news" {:post create-news}]])