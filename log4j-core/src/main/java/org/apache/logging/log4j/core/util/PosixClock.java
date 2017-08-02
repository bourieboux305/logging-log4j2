package org.apache.logging.log4j.core.util;

import jnr.posix.POSIXFactory;
import jnr.posix.Timeval;

public final class PosixClock implements Clock {
    private static volatile PosixClock instance;
    private static final Object INSTANCE_LOCK = new Object();
    private Timeval tv;
    public PosixClock() {
	tv=POSIXFactory.getNativePOSIX().allocateTimeval();
    }
    
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
	POSIXFactory.getNativePOSIX().gettimeofday(tv);
	return tv.usec()*1000;//not nano 
    }

    @Override
    public long currentTimeMillis() {
	POSIXFactory.getNativePOSIX().gettimeofday(tv);
	return tv.sec();
    }

}
