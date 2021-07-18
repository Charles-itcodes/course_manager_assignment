package se.lexicon.course_manager_assignment.data.sequencers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentSequencerTest {

    @BeforeEach
    void setUp() {
        StudentSequencer.setStudentSequencer(0);
    }

    @Test
    @DisplayName("getStudentSequencer() should return 0")
    void getStudentSequencer_return_0() {
        assertEquals(0, StudentSequencer.getStudentSequencer());
    }

    @Test
    @DisplayName("nextStudentId() should return 1")
    void nextStudentId_return_1() {
        assertEquals(1, StudentSequencer.nextStudentId());
    }
}
