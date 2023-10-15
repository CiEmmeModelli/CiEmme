package org.jboss.arquillian.core.impl;

public class ServiceException extends RuntimeException{
    public ServiceException(){}
    public ServiceException(String message){
        super(message);
    }

}
