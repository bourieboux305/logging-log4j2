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
import org.apache.logging.log4j.util.PerformanceSensitive;

/**
 * Converts and formats the event's nanoTime in a StringBuilder.
 */
@Plugin(name = "SubMillis", category = PatternConverter.CATEGORY)
@ConverterKeys({ "sm"})
@PerformanceSensitive("allocation")
public final class SubMillisPatternConverter extends LogEventPatternConverter {
    
    
    private int format=0;
    private final int DEFAULT_FORMAT=6;
    String[][] numberZero={
	    	{""},
	    	{"","0"},
	    	{"","00","0"},
	    	{"","000","00","0"},
	    	{"","0000","000","00","0"},
	    	{"","00000","0000","000","00","0"},
	    };
    long[] integerMultipleOfTen={0,10,100,1000,10000,100000};
    
    /**
     * Private constructor.
     *
     * @param options
     *            options, may be null.
     */
    private SubMillisPatternConverter(final String[] options) {
        super("SubMillis", "submillis");
        if ((options.length <= 0) || !(options[0].matches("1|2|3|4|5|6")) || (options[0].length() != 1)){
		this.format=DEFAULT_FORMAT;
        }
        else
            this.format = Integer.parseInt(options[0]);
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
    
    public String getSubMillisFormatedWithZero(long[] valueInFormat){
	String value=String.valueOf(valueInFormat[this.format-1]);
	for(int i=0;i<this.format;i++){
	    if(valueInFormat[this.format-1]<this.integerMultipleOfTen[i])
		return numberZero[this.format-1][i]+value;
	}
	return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void format(final LogEvent event, final StringBuilder output) {
	long subMillis = event.getNanoTime() % 1000000L;
	long[] longInFormat={subMillis / 100000,subMillis / 10000, subMillis / 1000, subMillis / 100,subMillis / 10,subMillis};
	output.append(getSubMillisFormatedWithZero(longInFormat));
    }
}
