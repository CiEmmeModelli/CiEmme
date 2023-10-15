package org.jboss.arquillian.core.impl;

public class ProcessException extends RuntimeException{

    public ProcessException(String string, Exception e) {
        super(string,e);
    }

}
