(ns berita.db.news
  (:require [berita.db.core :refer [db-conn]]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [honeysql.helpers :as helpers]))

(defn insert-news-sql [params]
  (-> (helpers/insert-into :news)
      (helpers/values [params])
      (sql/format)))

(defn create-news! [params]
  (jdbc/execute! db-conn (insert-news-sql params)))

(defn select-where [where-clause]
  (sql/format {:select [:id :title :image_url :category :source_url :source_name :published_at]
               :from   [:news]
               :where  where-clause}))

(defn select-news [& sqlmaps]
  (sql/format (merge
                {:select [:id :title :image_url :category
                          :source_url :source_name :published_at]
                 :from   [:news]}
                sqlmaps)))

(defn get-by-id [news-id]
  (jdbc/query db-conn (select-news {:where [:= :id news-id]})))

