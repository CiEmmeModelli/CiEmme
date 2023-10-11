package org.jboss.arquillian.container.test.impl.client.deployment.tool;

public class ToolRTException extends RuntimeException {

    public ToolRTException() {
        super();
    }

    public ToolRTException(String message) {
        super(message);
    }

    public ToolRTException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolRTException(Throwable cause) {
        super(cause);
    }
}
