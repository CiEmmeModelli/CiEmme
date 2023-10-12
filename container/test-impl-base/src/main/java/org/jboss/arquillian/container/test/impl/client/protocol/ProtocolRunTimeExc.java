package org.jboss.arquillian.container.test.impl.client.protocol;

public class ProtocolRunTimeExc extends RuntimeException {

    public ProtocolRunTimeExc() {
        super();
    }

    public ProtocolRunTimeExc(String message) {
        super(message);
    }

    public ProtocolRunTimeExc(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolRunTimeExc(Throwable cause) {
        super(cause);
    }
}
