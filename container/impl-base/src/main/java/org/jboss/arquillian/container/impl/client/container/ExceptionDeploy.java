package org.jboss.arquillian.container.impl.client.container;

public class ExceptionDeploy extends RuntimeException {
    
    public ExceptionDeploy() {
        super();
    }
    public ExceptionDeploy(String message) {
        super(message);
    }

    public ExceptionDeploy(String message, Throwable cause) {
        super(message, cause);
    }
}
