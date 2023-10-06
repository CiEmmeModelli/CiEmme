package org.jboss.arquillian.container.impl.client.container;

public class DeploymentExceptionRTSec extends RuntimeException {
    public DeploymentExceptionRTSec(String message) {
        super(message);
    }

    public DeploymentExceptionRTSec(String message, Throwable cause) {
        super(message, cause);
    }
}
