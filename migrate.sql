-- Table set up
CREATE TABLE courses(
 id SERIAL PRIMARY KEY,
 course_name varchar(100) NOT NULL,
 duration_seconds integer NOT NULL
);

CREATE TABLE course_times(
    id SERIAL PRIMARY KEY,
    course_id integer references courses(id) ON DELETE CASCADE,
    starts_at timestamptz NOT NULL
);

-- Table Seed
INSERT INTO courses (name, duration_seconds) VALUES
('Canine Yoga', 3600), ('Financial Accounting I', 21600), ('Clojure Web Applications', 10800), ('Resume Writing Seminar', 10800);

INSERT INTO course_times (course_id, starts_at) VALUES
(1, '2021-01-18 11:00:00'),
(1, '2021-01-25 13:00:00'),
(2, '2021-01-25 10:00:00'),
(3, '2021-02-09 09:00:00'),
(3, '2021-02-11 09:00:00'),
(3, '2021-02-16 09:00:00'),
(3, '2021-02-18 09:00:00'),
(4, '2021-02-24 13:00:00');