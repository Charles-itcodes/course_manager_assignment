package se.lexicon.course_manager_assignment.data.sequencers;

public class StudentSequencer {
    private static int studentSequencer;

    public static int nextStudentId(){
        return ++studentSequencer;
    }

    public static int getStudentSequencer() {
        return studentSequencer;
    }

    public static void setStudentSequencer(int studentSequencer) {
        StudentSequencer.studentSequencer = studentSequencer;
    }
}
