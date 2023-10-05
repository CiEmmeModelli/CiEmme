package org.jboss.arquillian.container.impl.client.container;

public class ExceptionSetContainer extends Exception {
    public ExceptionSetContainer (String message){
        super(message);
    }


    public ExceptionSetContainer(String message, Throwable cause) {
        super(message, cause);
    }
}
