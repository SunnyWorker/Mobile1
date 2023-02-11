package com.example.labwork1;

import org.junit.Test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void checkShortNameFunctionWithMoreLetters() {
        Note note = new Note();
        note.setTitle("Very very very long title");
        assertEquals("Very very very lon...",note.getShortTitle());
    }

    @Test
    public void checkShortNameFunctionWithLessLetters() {
        Note note = new Note();
        note.setTitle("Lil title");
        assertEquals("Lil title",note.getShortTitle());
    }

    @Test
    public void checkPasswordHashFunctionWithEqualsPassword() {
        Note note1 = new Note();
        note1.setPasswordHash("dd");
        Note note2 = new Note();
        note2.setPasswordHash("dd");
        assertArrayEquals(note1.getPasswordHash(),note2.getPasswordHash());
    }

    @Test
    public void checkPasswordHashFunctionWithDifferentPassword() {
        Note note1 = new Note();
        note1.setPasswordHash("dd");
        Note note2 = new Note();
        note2.setPasswordHash("ddddd");
        if(Arrays.equals(note1.getPasswordHash(),note2.getPasswordHash()))
            fail();
    }
}