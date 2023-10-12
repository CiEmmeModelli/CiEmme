package org.jboss.arquillian.container.test.impl.enricher.resource;

public class CustomExceptionRunEnricher extends RuntimeException {

    public CustomExceptionRunEnricher() {
        super();
    }

    public CustomExceptionRunEnricher(String message) {
        super(message);
    }

    public CustomExceptionRunEnricher(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomExceptionRunEnricher(Throwable cause) {
        super(cause);
    }
}
