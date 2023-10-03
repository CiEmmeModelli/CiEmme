package org.jboss.arquillian.config.impl.extension;

public class ArquillianConfigurationException extends RuntimeException {
    public ArquillianConfigurationException(String message) {
        super(message);
    }

    public ArquillianConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
