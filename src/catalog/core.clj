(ns catalog.core
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :refer [body-params]]
            [clojure.tools.namespace.repl :refer [refresh]]
            [catalog.courses]))

(defn respond-hello [request]
  {:status 200
   :body "Hola Mundo"})

(defn ok [body]
  {:status 200 :body body})

(defn message-response [status message]
  {:status status :body message})

(defn get-course-payload [course_id]
  (if (empty? course_id)
    (catalog.courses/all-courses)
    (catalog.courses/single-course course_id)
  )
)

(defn delete-session-payload [session_id]
  (if (empty? session_id)
    (message-response 400 "Unable to determine which session should be deleted")
    (if (catalog.courses/delete-session session_id)
      (message-response 500 "No records deleted (there might not be a session with this id)")
      (message-response 200 "Successful deletion of session")
    )
  )
)

(defn delete-course-payload [course_id]
  (if (empty? course_id)
    (message-response 400 "Unable to determine which course should be deleted")
    (if (catalog.courses/delete-course course_id)
      (message-response 500 "No records deleted (there might not be a course with this id)")
      (message-response 200 "Successful deletion of course")
    )
  )
)

(defn handle-get-course-response [request]
  (get-course-payload (get-in request [:query-params :course_id]))
)

(defn handle-delete-course-response [request]
  (delete-course-payload (get-in request [:query-params :course_id]))
)

(defn handle-delete-session-response [request]
  (delete-session-payload (get-in request [:query-params :session_id]))
)

(defn handle-course-update-response [request]
  (def res (catalog.courses/update-course request))
  ; (println res)
  (if (string? res)
    (message-response 400 res)
    (if res
      (message-response 500 "update failed")
      (message-response 200 "successful update")
    )
  )
)

(def routes
  #{
    ["/hello" :get `respond-hello]
    ["/courses" :get handle-get-course-response :route-name :get-course]
    ["/courses" :delete handle-delete-course-response :route-name :delete-course]
    ["/courses" :post [(body-params) catalog.courses/create-course] :route-name :post-course]
    ["/courses" :patch [(body-params) handle-course-update-response] :route-name :patch-course]
    ["/course-session" :post [(body-params) catalog.courses/create-course-session] :route-name :post-course-session]
    ["/course-session" :delete handle-delete-session-response :route-name :delete-course-session]
  }
)

(def service-map
  {::http/routes routes
   ::http/type   :jetty
   ::http/port   8890
   ::http/allowed-origins {:creds true :allowed-origins (constantly true)}})

;; For interactive development
(defonce server (atom nil))

(defn go []
  (reset! server
          (http/start (http/create-server
                       (assoc service-map
                              ::http/join? false))))
  (prn "Server started on localhost:8890")
  :started)

(defn halt []
  (http/stop @server))

(defn reset []
  (halt)
  (refresh :after 'catalog.core/go))

(defn -main [& args] (go))