package org.jboss.arquillian.junit;

public class TestClassException extends RuntimeException{

    public TestClassException(String format) {
        super(format);
    }

    public TestClassException(String string, Exception e) {
        super(string,e);
    }

}
