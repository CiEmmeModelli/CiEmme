package org.jboss.arquillian.protocol.jmx;

public class SerializeException extends RuntimeException{

    public SerializeException(String string, Exception e) {
        super(string,e);
    }

}
