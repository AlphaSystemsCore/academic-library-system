package com.sys.utilities;

public class Table {
    public static String tableCreation() {
        String table_sql = """
              
                CREATE TABLE IF NOT EXISTS departments (
                    department_id INT PRIMARY KEY AUTO_INCREMENT,
                    name          VARCHAR(100),
                    description   VARCHAR(255)
                );
               
        
                """;
                //  CREATE TABLE IF NOT EXISTS roles (
                //     role_id  INT PRIMARY KEY AUTO_INCREMENT,
                //     name     VARCHAR(50) UNIQUE
                // );
              
                // CREATE TABLE IF NOT EXISTS programmes (
                //     programme_id   INT PRIMARY KEY AUTO_INCREMENT,
                //     name           VARCHAR(100),
                //     code           VARCHAR(10),
                //     level          VARCHAR(50),
                //     duration_years INT,
                //     department_id  INT,
                //     FOREIGN KEY (department_id) REFERENCES departments(department_id)
                // );
                

                // CREATE TABLE IF NOT EXISTS semesters (
                //     semester_id     INT PRIMARY KEY AUTO_INCREMENT,
                //     academic_year   YEAR,
                //     semester_number INT,
                //     start_date      DATE,
                //     end_date        DATE,
                //     is_current      BOOLEAN DEFAULT FALSE
                // );

                // CREATE TABLE IF NOT EXISTS courses (
                //     course_id    INT PRIMARY KEY AUTO_INCREMENT,
                //     course_code  VARCHAR(20) UNIQUE,
                //     title        VARCHAR(150),
                //     credit_hours INT,
                //     programme_id INT,
                //     FOREIGN KEY (programme_id) REFERENCES programmes(programme_id)
                // );

                // CREATE TABLE IF NOT EXISTS admins (
                //     admin_id   INT PRIMARY KEY AUTO_INCREMENT,
                //     first_name VARCHAR(100),
                //     last_name  VARCHAR(100),
                //     email      VARCHAR(100) UNIQUE,
                //     identification_number VARCHAR(10) UNIQUE,
                //     phone_number VARCHAR(14) UNIQUE,
                //     role_id    INT,
                //     password   VARCHAR(255),
                //     adminNumber VARCHAR(12) UNIQUE
                    
                //     FOREIGN KEY (role_id) REFERENCES roles(role_id)
                // );

                // CREATE TABLE IF NOT EXISTS lecturers (
                //     lecturer_id  INT PRIMARY KEY AUTO_INCREMENT,
                //     first_name   VARCHAR(100),
                //     last_name    VARCHAR(100),
                //     email        VARCHAR(100) UNIQUE,
                //     title        VARCHAR(50),
                //     ID_NO        VARCHAR(20) UNIQUE,
                //     phone_number VARCHAR(20) UNIQUE,
                //     staff_number VARCHAR(20) UNIQUE,
                //     department_id INT,
                //     role_id      INT,
                //     password     VARCHAR(255),
                //     FOREIGN KEY (department_id) REFERENCES departments(department_id),
                //     FOREIGN KEY (role_id)       REFERENCES roles(role_id)
                // );

                // CREATE TABLE IF NOT EXISTS course_offerings (
                //     offering_id  INT PRIMARY KEY AUTO_INCREMENT,
                //     course_id    INT,
                //     lecturer_id  INT,
                //     semester_id  INT,
                //     programme_id INT,
                //     FOREIGN KEY (course_id)    REFERENCES courses(course_id),
                //     FOREIGN KEY (lecturer_id)  REFERENCES lecturers(lecturer_id),
                //     FOREIGN KEY (semester_id)  REFERENCES semesters(semester_id),
                //     FOREIGN KEY (programme_id) REFERENCES programmes(programme_id)
                // );

                // CREATE TABLE IF NOT EXISTS students (
                //     student_id          INT PRIMARY KEY AUTO_INCREMENT,
                //     first_name          VARCHAR(100),
                //     last_name           VARCHAR(100),
                //     email               VARCHAR(100) UNIQUE,
                //     phone_number        VARCHAR(20),
                //     ID_NO               VARCHAR(20) UNIQUE,
                //     password            VARCHAR(255),
                //     programme_id        INT,
                //     admission_number    VARCHAR(30) UNIQUE,
                //     role_id             INT,
                //     registered_date     DATE,
                //     FOREIGN KEY (programme_id) REFERENCES programmes(programme_id),
                //     FOREIGN KEY (role_id)      REFERENCES roles(role_id)
                // );

                // CREATE TABLE IF NOT EXISTS enrollments (
                //     enrollment_id     INT PRIMARY KEY AUTO_INCREMENT,
                //     student_id        INT,
                //     offering_id       INT,
                //     attempt_number    INT DEFAULT 1,
                //     status            VARCHAR(20),
                //     is_counted_in_gpa BOOLEAN DEFAULT TRUE,
                //     is_active         BOOLEAN DEFAULT TRUE,
                //     enrolled_date     DATE,
                //     FOREIGN KEY (student_id)  REFERENCES students(student_id),
                //     FOREIGN KEY (offering_id) REFERENCES course_offerings(offering_id)
                // );

                // CREATE TABLE IF NOT EXISTS scores (
                //     score_id      INT PRIMARY KEY AUTO_INCREMENT,
                //     enrollment_id INT,
                //     cat_score     FLOAT,
                //     exam_score    FLOAT,
                //     total_score   FLOAT,
                //     letter_grade  VARCHAR(5),
                //     grade_point   FLOAT,
                //     academic_year YEAR,
                //     submitted_by  INT,
                //     submitted_date Date,
                //     FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id),
                //     FOREIGN KEY (submitted_by)  REFERENCES lecturers(lecturer_id)
                // );

                // CREATE TABLE IF NOT EXISTS books (
                //     book_id          INT PRIMARY KEY AUTO_INCREMENT,
                //     isbn             VARCHAR(20) UNIQUE,
                //     title            VARCHAR(200),
                //     author           VARCHAR(150),
                //     publisher        VARCHAR(150),
                //     published_year   YEAR,
                //     category         VARCHAR(100),
                //     total_copies     INT DEFAULT 1,
                //     available_copies INT DEFAULT 1
                // );

                // CREATE TABLE IF NOT EXISTS librarians (
                //     librarian_id INT PRIMARY KEY AUTO_INCREMENT,
                //     first_name   VARCHAR(100),
                //     last_name    VARCHAR(100),
                //     email        VARCHAR(100) UNIQUE,
                //     password     VARCHAR(255),
                //     staff_number VARCHAR(20) UNIQUE,
                //     role_id      INT,
                //     identification_number VARCHAR(19) UNIQUE,
                //     phone_number VARCHAR(19) UNIQUE
                //     FOREIGN KEY (role_id) REFERENCES roles(role_id)
                // );

                // CREATE TABLE IF NOT EXISTS borrowings (
                //     borrowing_id INT PRIMARY KEY AUTO_INCREMENT,
                //     student_id   INT,
                //     book_id      INT,
                //     librarian_id INT,
                //     borrow_date  DATE,
                //     due_date     DATE,
                //     return_date  DATE,
                //     status       VARCHAR(20) DEFAULT 'borrowed',
                //     FOREIGN KEY (student_id)   REFERENCES students(student_id),
                //     FOREIGN KEY (book_id)      REFERENCES books(book_id),
                //     FOREIGN KEY (librarian_id) REFERENCES librarians(librarian_id)
                // );

                // CREATE TABLE IF NOT EXISTS fines (
                //     fine_id      INT PRIMARY KEY AUTO_INCREMENT,
                //     borrowing_id INT,
                //     student_id   INT,
                //     amount       DECIMAL(10,2),
                //     is_paid      BOOLEAN DEFAULT FALSE,
                //     created_date DATE,
                //     paid_date    DATE,
                //     FOREIGN KEY (borrowing_id) REFERENCES borrowings(borrowing_id),
                //     FOREIGN KEY (student_id)   REFERENCES students(student_id)
                // );
        return table_sql;
    }
}