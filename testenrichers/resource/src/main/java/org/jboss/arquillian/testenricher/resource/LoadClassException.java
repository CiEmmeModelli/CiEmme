package org.jboss.arquillian.testenricher.resource;

public class LoadClassException extends RuntimeException{

    public LoadClassException(String string, ClassNotFoundException e2) {
        super(string,e2);
    }

}
