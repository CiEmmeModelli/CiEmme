package org.jboss.arquillian.container.spi;

public class NotNullExcp extends RuntimeException {
    public NotNullExcp(String message) {
        super(message);
    }

    public NotNullExcp(String message, Throwable cause) {
        super(message, cause);
    }
}
