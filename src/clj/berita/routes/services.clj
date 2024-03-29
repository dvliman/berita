(ns berita.routes.services
  (:require
    [reitit.swagger :as swagger]
    [reitit.swagger-ui :as swagger-ui]
    [reitit.ring.coercion :as coercion]
    [reitit.coercion.spec :as spec-coercion]
    [reitit.ring.middleware.muuntaja :as muuntaja]
    [reitit.ring.middleware.multipart :as multipart]
    [reitit.ring.middleware.parameters :as parameters]
    [berita.middleware.formats :as formats]
    [berita.middleware.exception :as exception]
    [ring.util.http-response :refer :all]))

(defn service-routes []
  ["/api"
   {:coercion spec-coercion/coercion
    :muuntaja formats/instance
   :swagger {:id ::api}
   :middleware [parameters/parameters-middleware
                muuntaja/format-negotiate-middleware
                muuntaja/format-response-middleware
                exception/exception-middleware
                muuntaja/format-request-middleware
                coercion/coerce-response-middleware
                coercion/coerce-request-middleware
                multipart/multipart-middleware]}

   ;; swagger documentation
   ["" {:no-doc false
        :swagger {:info {:title "berita (berita lokal API)"
                         :description "https://github.com/dvliman/berita"}}}

    ["/swagger.json"
     {:get (swagger/create-swagger-handler)}]

    ["/api-docs/*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url "/api/swagger.json"
              :config {:validator-url nil}})}]]

   ;["/news" {:swagger {:tags ["news"]}}
   ;
   ; ["/fetch-news/:category"
   ;  {:get {:summary "fetch news by category"}}]]

    ;; ["/math"
    ;; {:swagger {:tags ["math"]}}

    ;; ["/plus"
    ;;  {:get {:summary "plus with spec query parameters"
    ;;         :parameters {:query {:x int?, :y int?}}
    ;;         :responses {200 {:body {:total pos-int?}}}
    ;;         :handler (fn [{{{:keys [x y]} :query} :parameters}]
    ;;                    {:status 200
    ;;                     :body {:total (+ x y)}})}
    ;;   :post {:summary "plus with spec body parameters"
    ;;          :parameters {:body {:x int?, :y int?}}
    ;;          :responses {200 {:body {:total pos-int?}}}
    ;;          :handler (fn [{{{:keys [x y]} :body} :parameters}]
    ;;                     {:status 200
    ;;                      :body {:total (+ x y)}})}}]]

   ;; ["/files"
   ;;  {:swagger {:tags ["files"]}}

   ;;  ["/upload"
   ;;   {:post {:summary "upload a file"
   ;;           :parameters {:multipart {:file multipart/temp-file-part}}
   ;;           :responses {200 {:body {:name string?, :size int?}}}
   ;;           :handler (fn [{{{:keys [file]} :multipart} :parameters}]
   ;;                      {:status 200
   ;;                       :body {:name (:filename file)
   ;;                              :size (:size file)}})}}]

   ;;  ["/download"
   ;;   {:get {:summary "downloads a file"
   ;;          :swagger {:produces ["image/png"]}
   ;;          :handler (fn [_]
   ;;                     {:status 200
   ;;                      :headers {"Content-Type" "image/png"}
   ;;                      :body (-> "public/img/warning_clojure.png"
   ;;                                (io/resource)
   ;;                                (io/input-stream))})}}]]


   ])
