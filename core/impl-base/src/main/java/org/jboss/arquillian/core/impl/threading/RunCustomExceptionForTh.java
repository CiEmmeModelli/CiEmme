package org.jboss.arquillian.core.impl.threading;

public class RunCustomExceptionForTh extends RuntimeException {
    public RunCustomExceptionForTh(String message) {
        super(message);
    }

    public RunCustomExceptionForTh(Exception e) {
        super(e);
    }

    public RunCustomExceptionForTh(String message, Throwable cause) {
        super(message, cause);
    }
}
