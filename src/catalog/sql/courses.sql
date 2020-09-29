-- :name all-courses :? :*
-- :doc Get all courses and their ids to hydrate top level list
SELECT
    courses.name,
    courses.id,
    (courses.duration_seconds / 60) AS duration_minutes
FROM courses
ORDER BY courses.id ASC;

-- :name single-course :? :*
-- :doc Get the session times (cast into human readable format) for a single course
SELECT
    timeslots.id,
    TO_CHAR(timeslots.starts_at, 'MM-DD-YYYY HH:MI') AS starts_at,
    TO_CHAR(
        timeslots.starts_at + (courses.duration_seconds||' seconds')::interval,
        'MM-DD-YYYY HH:MI'
    ) AS ends_at
FROM courses
JOIN course_times AS timeslots 
    ON timeslots.course_id = courses.id
WHERE timeslots.course_id = :course_id;

-- :name course-end-time :? :*
-- :doc compute the end time of a course given a time slot ID
SELECT
    TO_CHAR(timeslots.starts_at, 'MM-DD-YYYY HH:MI') AS starts_at,
    TO_CHAR(
        timeslots.starts_at + (courses.duration_seconds||' seconds')::interval,
        'MM-DD-YYYY HH:MI'
    ) AS ends_at
FROM courses
JOIN course_times AS timeslots 
    ON timeslots.course_id = courses.id
WHERE timeslots.id = :id;

-- :name new-course :insert :1
-- :doc Creates a new course
INSERT INTO courses
(name, duration_seconds)
VALUES (:course_name, :duration * 60)
RETURNING id;

-- :name new-session :insert :1
-- :doc adds a new session for the course
INSERT INTO course_times
(course_id, starts_at) 
VALUES (
    :course_id,
    :starts_at::timestamptz
)
RETURNING id;

-- :name delete-session :! :n
-- :doc deletes the session
DELETE FROM course_times WHERE id = :id;

-- :name delete-course :! :n
-- :doc deletes the course (and thus all of its sessions)
DELETE FROM course_times WHERE id = :id;

-- :name update-all-course-fields :! :n
-- :doc updates both the name and duration of the course
UPDATE courses
SET 
    name = :course_name,
    duration_seconds = :duration * 60
WHERE id = :course_id

-- :name update-course-name :! :n
-- :doc updates the name of the course
UPDATE courses
SET name = :course_name
WHERE id = :course_id

-- :name update-course-duration :! :n
-- :doc updates the name of the course
UPDATE courses
SET duration_seconds = :duration * 60
WHERE id = :course_id