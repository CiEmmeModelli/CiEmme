package org.jboss.arquillian.core.impl.loadable;

public class MyCustomException extends RuntimeException {
    public MyCustomException(String message) {
        super(message);
    }

    public MyCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
