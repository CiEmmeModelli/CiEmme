package org.jboss.arquillian.junit;

public class JunitRuntimeException extends RuntimeException{

    public JunitRuntimeException(String string, Throwable cause) {
        super(string, cause);
    }

}