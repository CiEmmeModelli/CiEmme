package org.jboss.arquillian.core.spi;

public class LoadExcp extends RuntimeException{

    public LoadExcp(String string, ClassNotFoundException e2) {
        super(string,e2);
    }

    public LoadExcp(String string) {
        super(string);
    }

}
