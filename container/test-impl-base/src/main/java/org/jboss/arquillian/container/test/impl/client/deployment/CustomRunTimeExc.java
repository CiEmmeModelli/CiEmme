package org.jboss.arquillian.container.test.impl.client.deployment;

public class CustomRunTimeExc extends RuntimeException {
    public CustomRunTimeExc() {
        super();
    }

    public CustomRunTimeExc(String message) {
        super(message);
    }

    public CustomRunTimeExc(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomRunTimeExc(Throwable cause) {
        super(cause);
    }
}
