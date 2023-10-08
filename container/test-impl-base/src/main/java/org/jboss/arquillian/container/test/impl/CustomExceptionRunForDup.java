package org.jboss.arquillian.container.test.impl;

public class CustomExceptionRunForDup extends RuntimeException {

    public CustomExceptionRunForDup() {
        super();
    }

    public CustomExceptionRunForDup(String message) {
        super(message);
    }

    public CustomExceptionRunForDup(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomExceptionRunForDup(Throwable cause) {
        super(cause);
    }
}
