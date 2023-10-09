package org.jboss.arquillian.container.test.impl.enricher.resource;

import java.net.URISyntaxException;

public class UriException extends RuntimeException{

    public UriException(String string, URISyntaxException e) {
        super(string,e);
    }

}
