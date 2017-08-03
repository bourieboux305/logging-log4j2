package org.apache.logging.log4j.core.util;

import java.util.concurrent.TimeUnit;

import jnr.posix.POSIXFactory;
import jnr.posix.Timeval;

public final class PosixClock implements Clock {
    private static volatile PosixClock instance ;
    private static final Object INSTANCE_LOCK = new Object();
    private static ThreadLocal<Timeval> tv = new ThreadLocal<Timeval>(){
	@Override protected Timeval initialValue(){
	    return POSIXFactory.getNativePOSIX().allocateTimeval();
	}
    };
    
    public static PosixClock instance(){
	PosixClock result=instance;
	if (result == null) {
            synchronized (INSTANCE_LOCK) {
                result = instance;
                if (result == null) {
                    instance = result = new PosixClock();
                }
            }
        }
        return result;
    }
    
    @Override
    public long nanoTime() {
	POSIXFactory.getNativePOSIX().gettimeofday(tv.get());
	return tv.get().sec() * TimeUnit.SECONDS.toNanos(1L) + tv.get().usec()*1000; 
    }

    @Override
    public long currentTimeMillis() {
	return System.currentTimeMillis();
    }

}
