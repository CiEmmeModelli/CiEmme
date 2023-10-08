package org.jboss.arquillian.container.spi.client.protocol.metadata;

public class RunCustomExc extends RuntimeException {

    public RunCustomExc() {
        super();
    }

    public RunCustomExc(String message) {
        super(message);
    }

    public RunCustomExc(String message, Throwable cause) {
        super(message, cause);
    }

    public RunCustomExc(Throwable cause) {
        super(cause);
    }
}
