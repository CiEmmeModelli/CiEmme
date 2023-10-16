package org.jboss.arquillian.junit;

public class RunException extends RuntimeException{

    public RunException(String string, Exception e) {
        super(string,e);
    }

}
