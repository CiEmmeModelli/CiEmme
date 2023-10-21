package org.jboss.arquillian.protocol.servlet.runner;

public class HttpException extends RuntimeException{

    public HttpException(String string, Exception e2) {
        super(string,e2);
    }

}
