(ns berita.routes.api
  (:require [struct.core :as st]
            [berita.db.news :as repo]
            [ring.util.http-response :refer [ok]]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.multipart :as multipart]
            [berita.middleware.formats :as formats]
            [reitit.coercion.spec :as spec-coercion]))

(defn bad-request [body]
  {:status 400
   :headers {}
   :body body})

(def news-schema
  [[:title st/required st/string]
   [:image_url st/required st/string]
   [:category st/required st/string]
   [:source_url st/required st/string]
   [:source_name st/required st/string]
   [:published_at st/required st/string]])

(defn validate-news [news]
  (first (st/validate news news-schema)))

;; curl -v -XPOST http://localhost:3000/news/create-news -H "Content-Type: application/json" -d '{"title": "json", "image_url": "url", "category": "news", "source_url": "source-url", "source_name": "source-name", "published_at": "2019-04-18T23:20:08Z"}'
(defn create-news [{:keys [params]}]
  (if-let [errors (validate-news params)]
    (bad-request {:error errors})
    (ok (merge {:id (first (repo/create-news! params))} params)))) ;; postgres returning id

;; curl -v -XPOST http://localhost:3000/news/fetch-news -H "Content-Type: application/json" -d '{"id": 4}'
(defn fetch-news [{:keys [params]}]
  (let [item (repo/news-by-id (:id params))]
    (if (empty? item)
      (bad-request {:error :not-found})
      (ok item))))

(defn query-news [{:keys [params]}]
  (ok (repo/query-news params)))

(defn api-routes []
  ["/news"
   {:coercion   spec-coercion/coercion
    :muuntaja   formats/instance
    :swagger    {:id ::api}
    :middleware [parameters/parameters-middleware
                 muuntaja/format-negotiate-middleware
                 muuntaja/format-response-middleware
                 exception/exception-middleware
                 muuntaja/format-request-middleware
                 coercion/coerce-response-middleware
                 coercion/coerce-request-middleware
                 multipart/multipart-middleware]}
   ["/query-news" {:post query-news}]
   ["/fetch-news" {:post fetch-news}]
   ["/create-news" {:post create-news}]
   ["/delete-news" {:post create-news}]])