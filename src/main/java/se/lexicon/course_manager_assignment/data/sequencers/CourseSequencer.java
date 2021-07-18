package se.lexicon.course_manager_assignment.data.sequencers;

public class CourseSequencer {
    private static int courseSequencer;

    public static int nextCourseId(){
        return ++courseSequencer;
    }

    public static int getCourseSequencer() {
        return courseSequencer;
    }

    public static void setCourseSequencer(int courseSequencer) {
        CourseSequencer.courseSequencer = courseSequencer;
    }
}
