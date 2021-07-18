package se.lexicon.course_manager_assignment.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

public class Course {

    private int id;
    private String courseName;
    private LocalDate startDate;
    private int weekDuration;
    private Collection<Student> students;

    public Course(int id) {
        this.id = id;
    }

    public Course() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getWeekDuration() {
        return weekDuration;
    }

    public void setWeekDuration(int weekDuration) {
        this.weekDuration = weekDuration;
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    public boolean enrollStudent(Student student) {
        if (students.contains(student) && student.equals(null)) {
            return false;
        } else {
            students.add(student);
            return true;
        }
    }

    public boolean unEnrollStudent(Student student) {

        if (students.contains(student)) {
            students.remove(student);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Course c = (Course) o;
        return getId() == c.getId() && Objects.equals(getCourseName(), c.getCourseName())
                && Objects.equals(getStartDate(), c.getStartDate()) && Objects.equals(getWeekDuration(), c.getWeekDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCourseName(), getStartDate(), getStudents(), getWeekDuration());
    }
}

