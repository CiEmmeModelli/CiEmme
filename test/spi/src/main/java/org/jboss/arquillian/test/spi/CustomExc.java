package org.jboss.arquillian.test.spi;

public class CustomExc extends Exception{

    public CustomExc(String string, Exception e) {
        super(string,e);
    }

    public CustomExc(String string, Throwable t) {
        super(string,t);
    }

    public CustomExc(String exceptionMessage) {
        super (exceptionMessage);
    }

}