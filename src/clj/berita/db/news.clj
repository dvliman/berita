(ns berita.db.news
  (:require [berita.db.core :refer [db-conn]]
            [clojure.java.jdbc :as jdbc]
            [clj-time.format :as timef]
            [clj-time.coerce :as timec]
            [honeysql.core :as sql]
            [honeysql.helpers :as helpers]))

(defn parse-time [time]
  (->> time
       (timef/parse (timef/formatters :date-time-parser))
       (timec/to-timestamp)))

(defn update-time [params]
  (update params :published_at #(parse-time %)))

(defn insert-news-sql [params]
  (-> (helpers/insert-into :news)
      (helpers/values [params])
      (sql/format)))

(defn create-news! [params]
  (jdbc/execute! db-conn (insert-news-sql (update-time params))))

(defn select-news [sqlmap]
  (sql/format (merge
                {:select [:id :title :image_url :category
                          :source_url :source_name :published_at]
                 :from   [:news]
                 :order-by [[:published_at :desc]]}
                sqlmap)))

(defn news-by-id [news-id]
  (jdbc/query db-conn (select-news {:where [:= :id news-id]
                                    :limit 1})))

;; {:k :v} becomes [:= :k :v]
(defn query-news [query]
  )

