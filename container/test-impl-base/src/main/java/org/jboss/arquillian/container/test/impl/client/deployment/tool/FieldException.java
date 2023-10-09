package org.jboss.arquillian.container.test.impl.client.deployment.tool;

public class FieldException extends RuntimeException{

    public FieldException(String string, Exception e) {
        super(string,e);
    }

}
