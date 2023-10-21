package org.jboss.arquillian.junit;

public class CustomExceptionRun extends RuntimeException {

    public CustomExceptionRun() {
        super();
    }

    public CustomExceptionRun(String message) {
        super(message);
    }

    public CustomExceptionRun(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomExceptionRun(Throwable cause) {
        super(cause);
    }
}
