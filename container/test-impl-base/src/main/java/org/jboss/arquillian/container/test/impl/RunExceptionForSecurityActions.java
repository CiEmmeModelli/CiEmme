package org.jboss.arquillian.container.test.impl;

public class RunExceptionForSecurityActions extends RuntimeException {

    public RunExceptionForSecurityActions() {
        super();
    }

    public RunExceptionForSecurityActions(String message) {
        super(message);
    }

    public RunExceptionForSecurityActions(String message, Throwable cause) {
        super(message, cause);
    }

    public RunExceptionForSecurityActions(Throwable cause) {
        super(cause);
    }
}
