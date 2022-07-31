package com.example.vv.quicknote.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Vivek Verma
 * @since 28/3/21
 */
@RunWith(JUnit4.class)
public class DateUtilTest {

    @Test
    public void testGetCurrentTimeStamp() {
        String result = DateUtil.getCurrentTimeStamp();
        System.out.println(result);
        assertTrue(result.matches("[A-Z-][a-z-]{2} [0-9]{2}-[0-9]{2}-[0-9]{4}"));
    }
}