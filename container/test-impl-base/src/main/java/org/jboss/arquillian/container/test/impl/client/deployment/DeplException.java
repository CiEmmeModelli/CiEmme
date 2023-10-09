package org.jboss.arquillian.container.test.impl.client.deployment;

public class DeplException extends RuntimeException{

    public DeplException(String string, Exception e)   {
        super(string,e);
    }
     

}
