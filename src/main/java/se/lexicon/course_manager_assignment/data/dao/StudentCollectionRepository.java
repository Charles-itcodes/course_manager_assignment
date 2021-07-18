package se.lexicon.course_manager_assignment.data.dao;



import se.lexicon.course_manager_assignment.data.sequencers.StudentSequencer;
import se.lexicon.course_manager_assignment.model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


public class StudentCollectionRepository implements StudentDao {

    private Collection<Student> students;
    private Student student;

    public StudentCollectionRepository(Collection<Student> students) {
        this.students = students;
    }

    @Override
    public Student createStudent(String name, String email, String address) {

        student = new Student(StudentSequencer.nextStudentId());
        student.setAddress(address);
        student.setEmail(email);
        student.setName(name);
        students.add(student);
        return student;
    }

    @Override
    public Student findByEmailIgnoreCase(String email) {

        Student _mstudent = new Student();
        for (Student mstudent : students) {
            if(mstudent.getEmail().equalsIgnoreCase(email)) {
                _mstudent=mstudent;
            }
        }
        return null;
    }

    @Override
    public Collection<Student> findByNameContains(String name) {

        Collection<Student> studentCollect = new ArrayList<Student>();
        for (Student student : students) {
            if(student.getName().equalsIgnoreCase(name)) {
                studentCollect.add(student);
            }}
        return studentCollect;
    }

    @Override
    public Student findById(int id) {

        Student myStudent =  new Student();
        for (Student _minstudent : students) {
            if(_minstudent.getId()==id) {
                myStudent=_minstudent;
            }}

        return myStudent;
    }

    @Override
    public Collection<Student> findAll() {
        return students;
    }

    @Override
    public boolean removeStudent(Student student) {
        return false;
    }

    @Override
    public void clear() {
        this.students = new HashSet<>();
    }
}
