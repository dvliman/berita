(ns berita.test.db.core
  (:require
    [berita.db.core :refer [db-conn] :as db]
    [luminus-migrations.core :as migrations]
    [clojure.test :refer :all]
    [clojure.java.jdbc :as jdbc]
    [berita.config :refer [env]]
    [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
      #'berita.config/env
      #'berita.db.core/db-conn)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn db-conn]
    (jdbc/db-set-rollback-only! t-conn)
    (is (= 1 (db/create-user!
               t-conn
               {:id         "1"
                :first_name "Sam"
                :last_name  "Smith"
                :email      "sam.smith@example.com"
                :pass       "pass"})))
    (is (= {:id         "1"
            :first_name "Sam"
            :last_name  "Smith"
            :email      "sam.smith@example.com"
            :pass       "pass"
            :admin      nil
            :last_login nil
            :is_active  nil}
           (db/get-user t-conn {:id "1"})))))
