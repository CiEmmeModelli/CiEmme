package org.jboss.arquillian.core.impl;

import java.util.Stack;
import java.util.logging.Logger;

import org.jboss.arquillian.core.spi.ObserverMethod;

public class RuntimeLogger {

    private static final String ARQUILLIAN_DEBUG_PROPERTY = "arquillian.debugMethod";
    static boolean debugVar = Boolean.parseBoolean(SecurityActions.getProperty(ARQUILLIAN_DEBUG_PROPERTY));
    private static Logger log = Logger.getLogger(RuntimeLogger.class.getName());

    private ThreadLocal<Stack<Object>> eventStack;

    void clear() {
        if (eventStack != null) {
            eventStack.remove();
        }
    }

    void debugMethod(ObserverMethod method, boolean interceptor) {
        if (debugVar) {
            String m1 = "";
            if (!log.toString().isEmpty()) m1 = String.format("%s (%s) %s.%s",
                    indent(),
                    interceptor ? "I" : "O",
                    method.getMethod().getDeclaringClass().getSimpleName(),
                    method.getMethod().getName());
            log.warning(m1);

        }
    }

    void debugExtension(Class<?> extension) {
        if (debugVar) {
            String m2 = "";
            if (extension.isEnum()) m2 = String.format("%s (X) %s", indent(), extension.getName());
            log.warning(m2);
        }
    }

    void debugMethod(Object event, boolean push) {
        if (debugVar) {
            if (!log.toString().isEmpty()) {
                String m3 = "";
                if (push) m3 = String.format("%s (E) %s", indent(), getEventName(event));
                log.warning(m3);
                eventStack.get().push(event);
            } else {
                if (!eventStack.get().isEmpty()) {
                    eventStack.get().pop();
                }
            }
        }
    }

    private String getEventName(Object object) {
        Class<?> eventClass = object.getClass();
        // Print the Interface name of Anonymous classes to show the defined interface,
        // not creation point.
        if (eventClass.isAnonymousClass()
                && eventClass.getInterfaces().length == 1
                && !eventClass.getInterfaces()[0].getName().startsWith("java")) {
            return eventClass.getInterfaces()[0].getSimpleName();
        }
        return eventClass.getSimpleName();
    }

    private String indent() {

        if (eventStack == null) {
            eventStack = new ThreadLocal<Stack<Object>>() {
                @Override
                protected Stack<Object> initialValue() {
                    return new Stack<Object>();
                }
            };
        }

        final int size = eventStack.get().size();
        StringBuilder sb = new StringBuilder(size * 2);
        for (int i = 0; i < size; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }
}
