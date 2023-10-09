package org.jboss.arquillian.container.test.impl;

public class RunExceptionForSecurity extends RuntimeException {

    public RunExceptionForSecurity() {
        super();
    }

    public RunExceptionForSecurity(String message) {
        super(message);
    }

    public RunExceptionForSecurity(String message, Throwable cause) {
        super(message, cause);
    }

    public RunExceptionForSecurity(Throwable cause) {
        super(cause);
    }
}
