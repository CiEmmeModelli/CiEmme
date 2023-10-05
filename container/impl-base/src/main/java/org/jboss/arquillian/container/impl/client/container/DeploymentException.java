package org.jboss.arquillian.container.impl.client.container;

public class DeploymentException extends IllegalStateException {
    public DeploymentException(String message) {
        super(message);
    }

    public DeploymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
