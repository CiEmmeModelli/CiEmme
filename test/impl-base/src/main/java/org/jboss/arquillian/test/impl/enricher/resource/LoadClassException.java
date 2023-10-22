package org.jboss.arquillian.test.impl.enricher.resource;

public class LoadClassException extends RuntimeException{

    public LoadClassException(String string, ClassNotFoundException e2) {
        super(string,e2);
    }

    public LoadClassException(String string) {
        super(string);
    }

}
