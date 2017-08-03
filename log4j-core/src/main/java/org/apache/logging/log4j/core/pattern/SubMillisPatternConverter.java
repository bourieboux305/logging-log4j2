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
 * Converts and formats the event's nanoTime in a StringBuilder.
 */
@Plugin(name = "SubMillis", category = PatternConverter.CATEGORY)
@ConverterKeys({ "sm"})
@PerformanceSensitive("allocation")
public final class SubMillisPatternConverter extends LogEventPatternConverter {
    
    
    private int digitNumber=0;//number of digit to print in submilliseconde
    private static final int DEFAULT_DIGIT_NUMBER=6;
    
    /** zero to add if number don't reach the number of digit asked **/
    private static String[][] numberZero={
	    	{""},//number of digit = 1
	    	{"0","0"},//number of digit = 2
	    	{"00","00","0"},//number of digit = 3
	    	{"000","000","00","0"},//number of digit = 4
	    	{"000","0000","000","00","0"},//number of digit = 5
	    	{"00000","00000","0000","000","00","0"},//number of digit = 6
	    };
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
	for(int i=0;i<this.digitNumber;i++){//test value if we should put a zero to complete the number digits asked
	    if(((event.getNanoTime() % 1000000L)/ (integerMultipleOfTen[DEFAULT_DIGIT_NUMBER-this.digitNumber]) )<integerMultipleOfTen[i]){
		 output.append(numberZero[this.digitNumber-1][i]);//put the zero before the value to match the number digit asked
		 output.append((event.getNanoTime() % 1000000L)/ (integerMultipleOfTen[DEFAULT_DIGIT_NUMBER-this.digitNumber]));
		 return;
	    }
	}
	output.append((event.getNanoTime() % 1000000L) / (integerMultipleOfTen[DEFAULT_DIGIT_NUMBER-this.digitNumber]));//append nanotime if it didn't need zero before the value
    }
}
