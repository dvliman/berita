(ns user
  (:require
    [berita.config :refer [env]]
    [clojure.spec.alpha :as s]
    [expound.alpha :as expound]
    [mount.core :as mount]
    [berita.core :refer [start-app]]
    [berita.db.core]
    [conman.core :as conman]
    [luminus-migrations.core :as migrations]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(defn start []
  (mount/start-without #'berita.core/repl-server))

(defn stop []
  (mount/stop-except #'berita.core/repl-server))

(defn restart []
  (stop)
  (start))

(defn restart-db []
  (mount/stop #'berita.db.core/*db*)
  (mount/start #'berita.db.core/*db*)
  (binding [*ns* 'berita.db.core]
    (conman/bind-connection berita.db.core/*db* "sql/queries.sql")))

(defn reset-db []
  (migrations/migrate ["reset"] (select-keys env [:database-url])))

(defn migrate []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration [name]
  (migrations/create name (select-keys env [:database-url])))


