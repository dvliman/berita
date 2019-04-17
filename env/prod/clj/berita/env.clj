(ns berita.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[berita started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[berita has shut down successfully]=-"))
   :middleware identity})
