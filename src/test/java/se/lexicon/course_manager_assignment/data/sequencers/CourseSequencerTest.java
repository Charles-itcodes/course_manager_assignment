package se.lexicon.course_manager_assignment.data.sequencers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseSequencerTest {

    @BeforeEach
    void setUp() {
        CourseSequencer.setCourseSequencer(0);
    }

    @Test
    @DisplayName("getCourseSequencer should return 0")
    void getCourseSequencer_should_return_0() {
        assertEquals(0, CourseSequencer.getCourseSequencer());
    }

    @Test
    @DisplayName("nextCourseId() should return 1")
    void nextCourseId_should_return_1() {
        assertEquals(1, CourseSequencer.nextCourseId());
    }
}
