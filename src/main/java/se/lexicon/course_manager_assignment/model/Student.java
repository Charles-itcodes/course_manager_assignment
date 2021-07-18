package se.lexicon.course_manager_assignment.model;

import java.util.Objects;

public class Student {
    private int id;
    private String name;
    private String email;
    private String address;

    public Student(int id) {
        this.id = id;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Student s = (Student) o;
        return getId() == s.getId() && Objects.equals(getName(), s.getName())
                && Objects.equals(getEmail(), s.getEmail()) && Objects.equals(getAddress(), s.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), getAddress());
    }

}


