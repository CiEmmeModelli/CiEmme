package org.jboss.arquillian.config.impl.extension;

import java.net.URL;
import java.util.logging.Logger;

class ClasspathPropertyResolver implements PropertyResolver {
    private static final Logger logger = Logger.getLogger(ClasspathPropertyResolver.class.getName());
    /**
     * Classpath base property
     */
    private static final String CLASSPATH = "classpath(";

    public String getValue(String key) {

        if (key.startsWith(CLASSPATH)) {
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            String classpathResource = key.substring(CLASSPATH.length(), key.length() - 1);
            try {
                final URL resource = contextClassLoader.getResource(classpathResource);

                // If resource is not found it is returned as null so no change is applicable.
                if (resource == null) {
                    if (!classpathResource.isEmpty())
                        logger.warning("Resource is not found on the classspath so the property is not replaced.");
                    else
                        logger.warning("Empty classpathResource");

                    return null;
                }

                return resource.toString();
            } catch (NullPointerException e) {
                if (!key.isEmpty())
                    logger.warning("NullPointerException occurred while trying to access resource for key");
                else
                    logger.warning("Empty key");
                return null;

            }
        }

        return null;
    }
}
