package com.wq.exception;

/**
 * Mana异常
 *
 * @author wangqing
 * @since 1.0.0
 */
public class ManaException extends Exception {

    public ManaException(String msg) {
        super(msg);
    }

    public ManaException(String msg,Throwable e) {
        super(msg,e);
    }
}
