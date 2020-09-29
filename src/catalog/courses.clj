(ns catalog.courses
  (:require [catalog.sql.courses :as sql]
            [catalog.config :refer [db]]
            [io.pedestal.http :as http]
            [net.cgrand.xforms :as x]
            [clojure.spec.alpha :as s]
  ))

(s/def ::course_name string?)
(s/def ::duration int?)

(s/def ::course (s/keys :req-un [::course_name ::duration]))

(s/def ::starts_at string?)
(s/def ::course_id int?)

(s/def ::session (s/keys :req-un [::course_id ::starts_at]))

(s/def ::course-update (s/keys :req-un [::course_id]))

; (defn try-this [item] 
;   (println "gabagool")
;   (println item)
; )

; Another option, but cannot readily determine how to extend this method to 
; (defn structure-into []
;   (into {} (x/by-key :name (comp (map :starts_at) (x/into []))) (sql/courses db))
; )

; (defn structure-courses []
  ; (def raw (sql/courses db))
  ; (doall (map inc raw))
  ; (println "error after doall map inc raw")
  ; (apply (map (try-this raw))))
  ; (let [nm (doall raw)]
  ;   (println "test")       
  ;   (println nm))
  ; (let [nm (get-in raw [ :name])]       
  ;   (println "test again")       
  ;   (println nm)))
  ; (get-in (first s) [:name])
  ; (apply (map println raw))
  ; (println (type raw))
  ; (raw)
;   ()
; )
  ; (println "hola weld")
  ; (sql/courses db))
  ; (sql/courses db))

(defn single-course [course_id]
  (println "single course")
  ; (println (type course_id))
  (http/json-response (sql/single-course db { :course_id (Integer/parseInt course_id) }))
)

(defn all-courses []
  (println "all courses")
  (http/json-response (sql/all-courses db))
)

(defn create-course [request]
  (println "create course")

  (let [new-course (select-keys (-> request :json-params)
                              [:course_name :duration])]
    (if (s/valid? ::course new-course)
      (let [[_ id] (sql/new-course db new-course)]
        (http/json-response {:msg "Course created successfully."
                             :id id}))
      (assoc (http/json-response {:msg "Please send a valid course."})
             :status 400))
  )
)

(defn not-nil [input]
  (not (nil? input))
)

(defn update-course [request]
  (println "update course")
  (def course_name (get-in request [:json-params :course_name]))
  (def duration (get-in request [:json-params :duration]))
  (def course_id (get-in request [:json-params :course_id]))
  (if (not-nil course_id)
    ; I imagine there is a somewhat smoother way to do this
    (cond
      ;A bit complex; checking for the presence of a name and a valid duration to perform an update 
      (and (not-nil course_name) (not-nil duration)) (zero? (sql/update-all-course-fields db { :course_id course_id :course_name course_name :duration duration}))
      (not-nil course_name) (zero? (sql/update-course-name db { :course_id course_id :course_name course_name}))
      (not-nil duration) (zero? (sql/update-course-duration db { :course_id course_id :duration duration}))
      :else "invalid request for update."
    )
    "No course specified for update."
  )
)

(defn create-course-session [request]
  (println "create course session")
  (let [new-session (select-keys (-> request :json-params)
                              [:course_id :starts_at])]
    (if (s/valid? ::session new-session)
      (let [[_ id] (sql/new-session db new-session)]
        ; (println "recovered vars")
        ; (println starts_at)
        (http/json-response {:msg "Course session created successfully."
                             :id id 
                             :timeframe (sql/course-end-time db { :id id })
                            }
        )
      )
      (assoc (http/json-response {:msg "Please send a valid session."})
             :status 400))
  )
)

(defn delete-session [session_id]
  (println "delete session")
  ; (println (sql/delete-session db {:id session_id}))
  (zero? (sql/delete-session db {:id (Integer/parseInt session_id)}))
)

(defn delete-course [course_id]
  (println "delete course")
  ; (println (sql/delete-session db {:id session_id}))
  (zero? (sql/delete-course db {:id (Integer/parseInt course_id)}))
)



(comment 
  (do 
    (require '[catalog.config :refer [db]])
    (require '[catalog.sql.courses :as sd] :reload)
    (require '[catalog.courses :as d] :reload)))