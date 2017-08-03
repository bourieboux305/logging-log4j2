package org.apache.logging.log4j.core.pattern;

import static org.junit.Assert.assertEquals;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.junit.Test;

public class SubMillisPatternConverterTest {

    @Test
    public void testConverterAppendsLogEventSubMillisNoAddZeroToStringBuilder() {
        final LogEvent event = Log4jLogEvent.newBuilder() //
                .setNanoTime(1234567).build();
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final StringBuilder sb3 = new StringBuilder();
        final StringBuilder sb4 = new StringBuilder();
        final StringBuilder sb5 = new StringBuilder();
        final StringBuilder sb6 = new StringBuilder();
        
        final String[] options1={"1"};
        final String[] options2={"2"};
        final String[] options3={"3"};
        final String[] options4={"4"};
        final String[] options5={"5"};
        final String[] options6={"6"};
        final SubMillisPatternConverter converter1 = SubMillisPatternConverter.newInstance(options1);
        final SubMillisPatternConverter converter2 = SubMillisPatternConverter.newInstance(options2);
        final SubMillisPatternConverter converter3 = SubMillisPatternConverter.newInstance(options3);
        final SubMillisPatternConverter converter4 = SubMillisPatternConverter.newInstance(options4);
        final SubMillisPatternConverter converter5 = SubMillisPatternConverter.newInstance(options5);
        final SubMillisPatternConverter converter6 = SubMillisPatternConverter.newInstance(options6);
        converter1.format(event, sb1);
        assertEquals("2", sb1.toString());
        converter2.format(event, sb2);
        assertEquals("23", sb2.toString());
        converter3.format(event, sb3);
        assertEquals("234", sb3.toString());
        converter4.format(event, sb4);
        assertEquals("2345", sb4.toString());
        converter5.format(event, sb5);
        assertEquals("23456", sb5.toString());
        converter6.format(event, sb6);
        assertEquals("234567", sb6.toString());
    }
    
    @Test
    public void testConverterAppendsLogEventSubMillisWithAddZeroToStringBuilder() {
        final LogEvent event = Log4jLogEvent.newBuilder() //
                .setNanoTime(123).build();
        final StringBuilder sb1 = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final StringBuilder sb3 = new StringBuilder();
        final StringBuilder sb4 = new StringBuilder();
        final StringBuilder sb5 = new StringBuilder();
        final StringBuilder sb6 = new StringBuilder();
        
        final String[] options1={"1"};
        final String[] options2={"2"};
        final String[] options3={"3"};
        final String[] options4={"4"};
        final String[] options5={"5"};
        final String[] options6={"6"};
        final SubMillisPatternConverter converter1 = SubMillisPatternConverter.newInstance(options1);
        final SubMillisPatternConverter converter2 = SubMillisPatternConverter.newInstance(options2);
        final SubMillisPatternConverter converter3 = SubMillisPatternConverter.newInstance(options3);
        final SubMillisPatternConverter converter4 = SubMillisPatternConverter.newInstance(options4);
        final SubMillisPatternConverter converter5 = SubMillisPatternConverter.newInstance(options5);
        final SubMillisPatternConverter converter6 = SubMillisPatternConverter.newInstance(options6);
        converter1.format(event, sb1);
        assertEquals("0", sb1.toString());
        converter2.format(event, sb2);
        assertEquals("00", sb2.toString());
        converter3.format(event, sb3);
        assertEquals("000", sb3.toString());
        converter4.format(event, sb4);
        assertEquals("0001", sb4.toString());
        converter5.format(event, sb5);
        assertEquals("00012", sb5.toString());
        converter6.format(event, sb6);
        assertEquals("000123", sb6.toString());
    }
    
}
