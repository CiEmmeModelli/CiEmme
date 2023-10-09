package org.jboss.arquillian.container.test.impl.client.deployment.command;

import java.io.IOException;

public class ObtException extends RuntimeException{

    public ObtException(String string, IOException ioe) {
        super(string,ioe);
    }

}
