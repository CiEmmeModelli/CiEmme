package org.jboss.arquillian.test.spi;

public class NotLoadException extends RuntimeException{

    public NotLoadException(String string, ClassNotFoundException e2) {
        super(string,e2);
    }

    public NotLoadException(String string) {
        super(string);
    }

    public NotLoadException(String string, Exception e) {
        super(string,e);
    }

    public NotLoadException(String string, Throwable t) {
        super(string,t);
    }

}
