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

(defn debug [params]
  (if-let [response (merge {:id (first (repo/create-news! params))} params)]
    (do (println "got to here")
        (println params)
        (println response))
    {:ok "fine"}))

(defn create-news [{:keys [params]}]
  (if-let [errors (validate-news params)]
    {:status  400
     :headers {}
     :body    {:errors errors}}
    {:status  200
     :headers {}
     :body    (debug params)})

  (defn delete-news [news-id]))

(defn api-routes []
  [""
   {:middleware {}}
   ["/news/create-news" {:post create-news}]
   ["/news/delete-news" {:post delete-news}]])