package org.jboss.arquillian.core.impl;

public class LoadException extends RuntimeException{

    public LoadException(String string) {
        super (string);
    }
    public LoadException(String string, ClassNotFoundException e2) {
        super(string,e2);
    }
    
}
