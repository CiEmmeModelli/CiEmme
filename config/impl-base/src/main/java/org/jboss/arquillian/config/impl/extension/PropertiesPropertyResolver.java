package org.jboss.arquillian.config.impl.extension;

import java.io.File;
import java.util.Properties;

class PropertiesPropertyResolver implements PropertyResolver {
    /**
     * File separator value
     */
    private static final String FILE_SEPARATOR = File.separator;

    /**
     * Path separator value
     */
    private static final String PATH_SEPARATOR = File.pathSeparator;

    /**
     * File separator alias
     */
    private static final String FILE_SEPARATOR_ALIAS = "/";

    /**
     * Path separator alias
     */
    private static final String PATH_SEPARATOR_ALIAS = ":";


    private final Properties props;

    PropertiesPropertyResolver(Properties props) {
        this.props = props;
    }

    public String getValue(String key) {
        String value;
    
        if (FILE_SEPARATOR_ALIAS.equals(key)) {
            value = FILE_SEPARATOR;
        } else if (PATH_SEPARATOR_ALIAS.equals(key)) {
            value = PATH_SEPARATOR;
        } else {
            value = getValueFromProperties(key);
            if (value == null) {
                value = resolveDefaultValue(key);
            }
        }
    
        return value;
    }

    private String getValueFromProperties(String key) {
        if (props != null) {
            return props.getProperty(key);
        } else {
            return System.getProperty(key);
        }
    }
    private String resolveDefaultValue(String key) {
        int colon = key.indexOf(':');
        if (colon > 0) {
            String realKey = key.substring(0, colon);
            String defaultValue = key.substring(colon + 1);
            String value = getValueFromProperties(realKey);
            if (value == null) {
                value = resolveCompositeKey(realKey,props);
            }
            return (value != null) ? value : defaultValue;
        } else {
            return resolveCompositeKey(key,props);
        }
    }

    /**
     * Try to resolve a "key" from the provided properties by
     * checking if it is actually a "key1,key2", in which case
     * try first "key1", then "key2". If all fails, return null.
     * <p>
     * It also accepts "key1," and ",key2".
     *
     * @param key
     *     the key to resolve
     * @param props
     *     the properties to use
     *
     * @return the resolved key or null
     */
    private String resolveCompositeKey(String key, Properties props) {
        String value = null;

        // Look for the comma
        int comma = key.indexOf(',');
        if (comma > -1) {
            // If we have a first part, try resolve it
            if (comma > 0) {
                // Check the first part
                String key1 = key.substring(0, comma);
                if (props != null) {
                    value = props.getProperty(key1);
                } else {
                    value = System.getProperty(key1);
                }
            }
            // Check the second part, if there is one and first lookup failed
            if (value == null && comma < key.length() - 1) {
                String key2 = key.substring(comma + 1);
                if (props != null) {
                    value = props.getProperty(key2);
                } else {
                    value = System.getProperty(key2);
                }
            }
        }
        // Return whatever we've found or null
        return value;
    }
}
