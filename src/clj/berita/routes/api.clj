(ns berita.routes.api
  (:require [struct.core :as st]
            [ring.util.http-response :refer [ok] :as response]))

(defn receive-webhook [req]
  (clojure.pprint/pprint req)
  (response/ok {:hello "changed"}))

(defn api-routes []
  [""
   {:middleware {}}
   ["/news/webhook" {:post receive-webhook}]])