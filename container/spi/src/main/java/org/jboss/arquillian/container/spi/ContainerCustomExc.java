package org.jboss.arquillian.container.spi.client;

public class ContainerCustomExc extends Exception {
    public ContainerCustomExc(String message) {
        super(message);
    }
}
