package org.jboss.arquillian.container.test.impl;

import java.lang.reflect.InvocationTargetException;

public class CustomException extends Exception {

    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(InvocationTargetException cause) {
        super(cause.getTargetException());
    }

    public CustomException(String message, InvocationTargetException cause) {
        super(message, cause.getTargetException());
    }

    public CustomException(IllegalAccessException cause) {
        super(cause);
    }

    public CustomException(String message, IllegalAccessException cause) {
        super(message, cause);
    }
}
