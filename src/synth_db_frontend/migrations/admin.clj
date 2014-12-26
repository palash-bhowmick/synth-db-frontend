(ns synth-db-frontend.migrations.admin
  (:require [caribou.admin.migrations
             [roles-permissions :as admin]]))

(def migrate admin/migrate)

(def rollback admin/rollback)
