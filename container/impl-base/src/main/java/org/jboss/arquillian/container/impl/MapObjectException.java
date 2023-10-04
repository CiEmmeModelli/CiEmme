package org.jboss.arquillian.container.impl;

public class MapObjectException extends RuntimeException {
    
    public MapObjectException() {
        super();
    }
    public MapObjectException(String message) {
        super(message);
    }

    public MapObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}

