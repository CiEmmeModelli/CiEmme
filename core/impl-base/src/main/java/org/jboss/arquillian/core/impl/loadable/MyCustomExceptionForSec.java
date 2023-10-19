package org.jboss.arquillian.core.impl.loadable;

public class MyCustomExceptionForSec extends RuntimeException {
    public MyCustomExceptionForSec(String message) {
        super(message);
    }

    public MyCustomExceptionForSec(String message, Throwable cause) {
        super(message, cause);
    }
}
