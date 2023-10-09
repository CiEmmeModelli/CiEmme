package org.jboss.arquillian.container.test.impl.enricher.resource;

import java.net.URISyntaxException;

public class ConvertException extends RuntimeException {

    public ConvertException(String string, URISyntaxException e) {
        super(string,e);
    }

}
