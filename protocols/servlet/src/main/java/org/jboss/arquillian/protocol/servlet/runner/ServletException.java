package org.jboss.arquillian.protocol.servlet.runner;

public class ServletException extends RuntimeException {

    public ServletException(Throwable throwable) {
        super(throwable);
    }

    public ServletException(String string) {
        super(string);
    }

}
