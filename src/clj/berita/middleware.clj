(ns berita.middleware
  (:require
    [berita.env :refer [defaults]]
    [berita.config :refer [env]]
    [ring-ttl-session.core :refer [ttl-memory-store]]
    [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
    [ring.middleware.keyword-params :refer [wrap-keyword-params]]
    [ring.middleware.json :refer [wrap-json-params wrap-json-body wrap-json-response]]
    [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
    [buddy.auth.accessrules :refer [restrict]]
    [buddy.auth :refer [authenticated?]]
    [buddy.auth.backends.session :refer [session-backend]]))

(defn on-error [request response]
  {:status  403
   :headers {}
   :body    (str "Access to " (:uri request) " is not authorized")})

(defn wrap-restricted [handler]
  (restrict handler {:handler  authenticated?
                     :on-error on-error}))

(defn wrap-auth [handler]
  (let [backend (session-backend)]
    (-> handler
        (wrap-authentication backend)
        (wrap-authorization backend))))

(defn wrap-base [handler]
  (-> ((:middleware defaults) handler)
      wrap-auth
      wrap-keyword-params
      wrap-json-params
      wrap-json-response
      (wrap-defaults
        (-> site-defaults
            (assoc-in [:security :anti-forgery] false)
            (assoc-in [:session :store] (ttl-memory-store (* 60 30)))))))
