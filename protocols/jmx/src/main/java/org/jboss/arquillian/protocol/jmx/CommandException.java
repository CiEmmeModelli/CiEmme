package org.jboss.arquillian.protocol.jmx;

public class CommandException extends RuntimeException{

    public CommandException(Throwable throwable) {
        super(throwable);
    }

}
