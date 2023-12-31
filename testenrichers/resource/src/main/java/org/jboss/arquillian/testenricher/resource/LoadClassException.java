package org.jboss.arquillian.testenricher.resource;

public class LoadClassException extends RuntimeException{

    public LoadClassException(String string, ClassNotFoundException e2) {
        super(string,e2);
    }

    public LoadClassException(String string) {
        super(string);
    }

    public LoadClassException(String string, Exception e) {
        super(string,e);
    }

    public LoadClassException(String string, Throwable t) {
        super(string,t);
    }

}
