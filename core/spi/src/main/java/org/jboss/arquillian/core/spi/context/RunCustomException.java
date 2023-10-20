package org.jboss.arquillian.core.spi.context;

public class RunCustomException extends RuntimeException {
    public RunCustomException(String message) {
        super(message);
    }

    public RunCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
