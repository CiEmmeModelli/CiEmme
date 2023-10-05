package org.jboss.arquillian.container.impl.client.container;

import java.io.IOException;

public class ExceptionDeploy extends IOException {
    
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
