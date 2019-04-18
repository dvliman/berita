(ns berita.routes.api
  (:require [struct.core :as st]
            [berita.db.news :as repo]
            [ring.util.http-response :as response]))

(def news-schema
  [[:title st/required st/string]
   [:image_url st/required st/string]
   [:category st/required st/string]
   [:source_url st/required st/string]
   [:source_name st/required st/string]
   [:published_at st/required st/string]])

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

(defn fetch-news [{:keys [params]}]
  (response/ok (repo/get-by-id (:id params))))

(defn query-news [{:keys [params]}]
  (response/ok (repo/get-by-id (:id params))))

(defn api-routes []
  [""
   ["/news/query-news" {:post query-news}]
   ["/news/fetch-news" {:post fetch-news}]
   ["/news/create-news" {:post create-news}]
   ["/news/delete-news" {:post create-news}]])