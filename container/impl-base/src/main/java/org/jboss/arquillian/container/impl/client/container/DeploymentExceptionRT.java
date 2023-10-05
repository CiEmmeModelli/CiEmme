package org.jboss.arquillian.container.impl.client.container;

public class DeploymentExceptionRT extends RuntimeException {
    public DeploymentExceptionRT(String message) {
        super(message);
    }

    public DeploymentExceptionRT(String message, Throwable cause) {
        super(message, cause);
    }
}
