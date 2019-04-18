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

(defn select-news [& sqlmaps]
  (sql/format (merge
                {:select [:id :title :image_url :category
                          :source_url :source_name :published_at]
                 :from   [:news]
                 :order-by [[:published_at :desc]]}
                sqlmaps)))

(defn news-by-id [news-id]
  (jdbc/query db-conn (select-news {:where [:= :id news-id]
                                    :limit 1})))

;; {:k :v} becomes [:= :k :v]
(defn query-news [query]
  )

