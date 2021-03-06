(ns playphraseme.api.routes.playlists
  (:require [playphraseme.api.middleware.cors :refer [cors-mw]]
            [playphraseme.api.queries.playlists :refer :all]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(s/defschema Playlist
  {:title                 s/Str
   :uuid                  s/Str
   (s/optional-key :info) s/Str
   :phrases               [{:text                  s/Str
                            (s/optional-key :info) s/Str}]})

(defn add-new-playlist [playlist-data]
  (println "add-new-playlist:" playlist-data)
  (let [exists      (find-one-playlist playlist-data)
        playlist-id (if exists
                      (:id exists)
                      (:id (insert-playlist! playlist-data)))]
    playlist-id))

(def playlists-routes
  "Specify routes for Mobile Playlists"
  (context "/api/v1/playlists" []
    :tags ["Playlists"]

    (POST "/"             {:as request}
          :return        s/Str
          :middleware    [cors-mw]
          :body-params   [playlist :- Playlist]
          :summary       "Add "
          (ok (add-new-playlist playlist)))

    (GET "/:id"         [id :as request]
         :tags          ["Playlists"]
         :return        s/Any
         :middleware    [cors-mw]
         :summary       "Return playlist by id"
         (ok
          (get-playlist-by-id id)))))


(comment

  (add-new-playlist {:title "hello"
                     :uuid "any id"
                     :phrases [{:text "aaaasss" :info "nothing"}]})


  )
