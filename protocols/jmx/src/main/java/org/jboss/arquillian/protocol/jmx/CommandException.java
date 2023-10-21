package org.jboss.arquillian.protocol.jmx;

public class CommandException extends RuntimeException{

    public CommandException(Throwable throwable) {
        super(throwable);
    }

    public CommandException(String string) {
        super(string);
    }

}
