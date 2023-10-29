package org.jboss.arquillian.testenricher.ejb;

public class NameException extends RuntimeException{

    public NameException(String string, ClassNotFoundException e2) {
        super(string,e2);
    }

    public NameException(String string) {
        super(string);
    }

    public NameException(String string, Exception e) {
        super(string,e);
    }

}
