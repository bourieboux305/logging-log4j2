/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.pattern;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PerformanceSensitive;


/**
 * Converts and formats the event's sub millisecond in a StringBuilder.
 */
@Plugin(name = "SubMillis", category = PatternConverter.CATEGORY)
@ConverterKeys({ "sm"})
@PerformanceSensitive("allocation")
public final class SubMillisPatternConverter extends LogEventPatternConverter {


    private int digitNumber=0;//number of digit to print in submilliseconde
    private static final int DEFAULT_DIGIT_NUMBER=6;
    private static String[] numberZero={"","0","00","000","0000","00000"};
    private static long[] integerMultipleOfTen={1L,10L,100L,1000L,10000L,100000L};

    /**
     * Private constructor.
     *
     * @param options
     *            options, may be null.
     */
    private SubMillisPatternConverter(final String[] options) {
	super("SubMillis", "submillis");
	if ((options.length <= 0) || !(options[0].matches("1|2|3|4|5|6")) || (options[0].length() != 1)){//test if parameters is an integer between 1 to 6
	    StatusLogger.getLogger().warn("impossible to parse parameters of 'sm'. number of digits apply by default is 6");
	    this.digitNumber=DEFAULT_DIGIT_NUMBER;
	}
	else{
	    this.digitNumber = Integer.parseInt(options[0]);
	}
    }

    /**
     * Obtains an instance of pattern converter.
     *
     * @param options
     *            options, may be null.
     * @return instance of pattern converter.
     */
    public static SubMillisPatternConverter newInstance(final String[] options) {
	return new SubMillisPatternConverter(options);
    }
    /** 6 is the number of digit maximum in sub millisecond . ex: 999 999**/
    int valueSize(Long paramLong){
	long l = 10L;
	for (int i = 1; i < 6; ++i)
	{
	    if (paramLong < l)
		return i;
	    l = 10L * l;
	}
	return 6;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
	long value=(event.getNanoTime() % 1000000L) / (integerMultipleOfTen[DEFAULT_DIGIT_NUMBER-this.digitNumber]);
	int valueSize=valueSize(value);
	if(this.digitNumber - valueSize>0){
	    output.append(numberZero[this.digitNumber - valueSize]);//add zero if needed
	}
	output.append(value);
    }
}
