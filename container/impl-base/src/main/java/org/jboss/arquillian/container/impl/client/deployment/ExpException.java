package org.jboss.arquillian.container.impl.client.deployment;


public class ExpException extends RuntimeException {
    public ExpException(String message) {
        super(message);
    }

    public ExpException(String message, Throwable cause) {
        super(message, cause);
    }
}

