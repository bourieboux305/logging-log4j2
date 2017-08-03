package org.apache.logging.log4j.core.util;

import java.util.concurrent.TimeUnit;

import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import jnr.posix.Timeval;

public final class PosixClock implements Clock {
    private static volatile PosixClock instance =new PosixClock();
    private final long UN_MILLIARD = TimeUnit.SECONDS.toNanos(1L);
    private static POSIX posix= POSIXFactory.getNativePOSIX();
    private static ThreadLocal<Timeval> tv = new ThreadLocal<Timeval>(){
	@Override protected Timeval initialValue(){
	    return posix.allocateTimeval();
	}
    };
    
    public static PosixClock instance(){
	return instance;
    }
    
    @Override
    public long nanoTime() {
	posix.gettimeofday(tv.get());
	return tv.get().sec() * UN_MILLIARD + tv.get().usec()*1000; 
    }

    @Override
    public long currentTimeMillis() {
	return System.currentTimeMillis();
    }

}
