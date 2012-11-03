package org.mat.nounou;

import org.junit.Test;
import org.mat.nounou.model.Time;

import java.util.Calendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimeTest {

    @Test
    public void testDefaultTimezone() throws Exception {
        final Time time = new Time();
        assertEquals(TimeZone.getDefault().getDisplayName(), time.getTimezone());
    }

    @Test
    public void testSpecifiedTimezone() throws Exception {
        final TimeZone est = TimeZone.getTimeZone("EST");
        final Time time = new Time(est);
        assertEquals(est.getDisplayName(), time.getTimezone());
    }

    @Test
    public void testIfItsTheMorning() {
        System.out.println(">>" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        assertTrue(Calendar.HOUR_OF_DAY < 12);


    }
}