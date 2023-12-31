/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.testenricher.ejb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.NamingException;

import org.jboss.arquillian.container.spi.client.protocol.metadata.RunCustomExc;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.test.spi.TestEnricher;

/**
 * Enricher that provide EJB class and setter method injection.
 *
 * @author <a href="mailto:aknutsen@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class EJBInjectionEnricher implements TestEnricher {
    private static final String ANNOTATION_NAME = "javax.ejb.EJB";

    private static final Logger log = Logger.getLogger(EJBInjectionEnricher.class.getName());

    private static String globTest = "java:global/test/";
    private static String globInterface = "/no-interface";
    private static String testString = "test/";

    @Inject
    private Instance<Context> contextInst;

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.spi.TestEnricher#enrich(org.jboss.arquillian.spi .Context, java.lang.Object)
     */
    public void enrich(Object testCase) {
        if (SecurityActions.isClassPresent(ANNOTATION_NAME)) {
            try {
                if (createContext() != null) {
                    injectClass(testCase);
                }
            } catch (Exception e) {
                log.throwing(EJBInjectionEnricher.class.getName(), "enrich", e);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.spi.TestEnricher#resolve(org.jboss.arquillian.spi .Context, java.lang.reflect.Method)
     */
    public Object[] resolve(Method method) {
        return new Object[method.getParameterTypes().length];
    }

    /**
     * Obtains all field in the specified class which contain the specified annotation
     *
     * @throws IllegalArgumentException
     *     If either argument is not specified
     */
    protected List<Field> getFieldsWithAnnotation(final Class<?> clazz, final Class<? extends Annotation> annotation)
        throws IllegalArgumentException {
        // Precondition checks
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must be specified");
        }
        if (annotation == null) {
            throw new IllegalArgumentException("annotation must be specified");
        }

        // Delegate to the privileged operations
        return SecurityActions.getFieldsWithAnnotation(clazz, annotation);
    }

    protected void injectClass(Object testCase) {
        try {
            Class<? extends Annotation> ejbAnnotation = loadEJBAnnotationClass();
    
            injectFields(testCase, ejbAnnotation);
            injectMethods(testCase, ejbAnnotation);
        } catch (Exception e) {
            throw new RunCustomExc("Could not inject members", e);
        }
    }
    
    private Class<? extends Annotation> loadEJBAnnotationClass() throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<? extends Annotation> ejbAnnotation =
            (Class<? extends Annotation>) SecurityActions.getThreadContextClassLoader().loadClass(ANNOTATION_NAME);
        return ejbAnnotation;
    }
    
    private void injectFields(Object testCase, Class<? extends Annotation> ejbAnnotation) throws IllegalAccessException {
        List<Field> annotatedFields = SecurityActions.getFieldsWithAnnotation(
            testCase.getClass(),
            ejbAnnotation);
    
        for (Field field : annotatedFields) {
            if (field.get(testCase) == null) {
                injectField(testCase, field, ejbAnnotation);
            }
        }
    }
    
    private void injectField(Object testCase, Field field, Class<? extends Annotation> ejbAnnotation) {
        EJB fieldAnnotation = (EJB) field.getAnnotation(ejbAnnotation);
        try {
            String mappedName = fieldAnnotation.mappedName();
            String beanName = fieldAnnotation.beanName();
            String lookup = attemptToGet31LookupField(fieldAnnotation);
    
            String[] jndiNames = resolveJNDINames(field.getType(), mappedName, beanName, lookup);
            Object ejb = lookupEJB(jndiNames);
            field.set(testCase, ejb);
        } catch (Exception e) {
            log.fine("Could not lookup " + fieldAnnotation + ", other Enrichers might, move on. Exception: " + e.getMessage());
        }
    }
    
    private void injectMethods(Object testCase, Class<? extends Annotation> ejbAnnotation) throws IllegalAccessException {
        List<Method> methods = SecurityActions.getMethodsWithAnnotation(testCase.getClass(), ejbAnnotation);
    
        for (Method method : methods) {
            if (method.getParameterTypes().length != 1) {
                throw new RunCustomExc("@EJB only allowed on single argument methods");
            }
            if (!method.getName().startsWith("set")) {
                throw new RunCustomExc("@EJB only allowed on 'set' methods");
            }
            EJB parameterAnnotation = getParameterEJBAnnotation(method.getParameterAnnotations()[0]);
            String[] jndiNames = resolveJNDINamesForMethod(method, parameterAnnotation);
            Object ejb;
            try {
                ejb = lookupEJB(jndiNames);
                method.invoke(testCase, ejb);
            } catch (InvocationTargetException | NamingException e) {
                e.getMessage();
            }
            
        }
    }
    
    private EJB getParameterEJBAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (EJB.class.isAssignableFrom(annotation.annotationType())) {
                return (EJB) annotation;
            }
        }
        return null;
    }
    
    private String[] resolveJNDINamesForMethod(Method method, EJB parameterAnnotation) {
        String mappedName = null;
        String beanName = null;
        String lookup = null;
    
        if (parameterAnnotation != null) {
            mappedName = parameterAnnotation.mappedName();
            beanName = parameterAnnotation.beanName();
            try {
                lookup = attemptToGet31LookupField(parameterAnnotation);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.getMessage();
            
            }
        }
        
        return resolveJNDINames(method.getParameterTypes()[0], mappedName, beanName, lookup);
    }
    

    protected String attemptToGet31LookupField(EJB annotation) throws IllegalAccessException,
        InvocationTargetException {
        String lookup = null;
        try {
            Method m = EJB.class.getMethod("lookup");
            lookup = String.valueOf(m.invoke(annotation));
        } catch (NoSuchMethodException e) {
            // No op, running on < 3.1 EJB lib
        }
        return lookup;
    }

    /**
     * Resolves the JNDI name of the given field.
     * <p>
     * If <tt>mappedName</tt>, <tt>lookup</tt> or <tt>beanName</tt> are specified, they're used to resolve JNDI name.
     * Otherwise, default policy
     * applies.
     * <p>
     * If more than one of the <tt>mappedName</tt>, <tt>lookup</tt> and <tt>beanName</tt> {@link EJB} annotation
     * attributes is specified at the same time, an {@link IllegalStateException}
     * will be thrown.
     *
     * @param fieldType
     *     annotated field which JNDI name should be resolved.
     * @param mappedName
     *     Value of {@link EJB}'s <tt>mappedName</tt> attribute.
     * @param beanName
     *     Value of {@link EJB}'s <tt>beanName</tt> attribute.
     * @param lookup
     *     Value of {@link EJB}'s <tt>lookup</tt> attribute.
     *
     * @return possible JNDI names which should be looked up to access the proper object.
     */
    protected String[] resolveJNDINames(Class<?> fieldType, String mappedName, String beanName, String lookup) {

        MessageFormat msg = new MessageFormat(
            "Trying to resolve JNDI name for field \"{0}\" with mappedName=\"{1}\" and beanName=\"{2}\"");
        String logMess = msg.format(new Object[] {fieldType, mappedName, beanName});
        if (mappedName.length()>1) {
            log.finer(logMess);
        }
        
        Validate.notNull(fieldType, "EJB enriched field cannot to be null.");

        boolean isMappedNameSet = hasValue(mappedName);
        boolean isBeanNameSet = hasValue(beanName);
        boolean isLookupSet = hasValue(lookup);

        if (isMoreThanOneValueTrue(isMappedNameSet, isBeanNameSet, isLookupSet)) {
            throw new IllegalStateException(
                "Only one of the @EJB annotation attributes 'mappedName', 'lookup' and 'beanName' can be specified at the same time.");
        }

        String[] jndiNames;

        // If set, use only mapped name or bean name to lookup the EJB.
        if (isMappedNameSet) {
            jndiNames = new String[] {mappedName};
        } else if (isLookupSet) {
            jndiNames = new String[] {lookup};
        } else if (isBeanNameSet) {
            jndiNames = new String[] {"java:module/" + beanName + "!" + fieldType.getName()};
        } else {
            jndiNames = getJndiNamesForAnonymousEJB(fieldType);
        }
        return jndiNames;
    }

    protected String[] getJndiNamesForAnonymousEJB(Class<?> fieldType) {
        String[] jndiNames;
        jndiNames = new String[] {
            "java:global/test.ear/test/" + fieldType.getSimpleName() + "Bean",
            "java:global/test.ear/test/" + fieldType.getSimpleName(),
            globTest + fieldType.getSimpleName(),
            globTest + fieldType.getSimpleName() + "Bean",
            globTest + fieldType.getSimpleName() + globInterface,
            "java:module/" + fieldType.getSimpleName(),
            testString + fieldType.getSimpleName() + "Bean/local",
            testString + fieldType.getSimpleName() + "Bean/remote",
            testString + fieldType.getSimpleName() + globInterface,
            fieldType.getSimpleName() + "Bean/local",
            fieldType.getSimpleName() + "Bean/remote",
            fieldType.getSimpleName() + globInterface,
            // WebSphere Application Server Local EJB default binding
            "ejblocal:" + fieldType.getCanonicalName(),
            // WebSphere Application Server Remote EJB default binding
            fieldType.getCanonicalName()};
        return jndiNames;
    }

    protected Object lookupEJB(String[] jndiNames) throws NamingException {
        Context initcontext = createContext();

        for (String jndiName : jndiNames) {
            try {
                return initcontext.lookup(jndiName);
            } catch (NamingException e) {
                // no-op, try next
            }
        }
        throw new NamingException("No EJB found in JNDI, tried the following names: " + joinJndiNames(jndiNames));
    }

    @SuppressWarnings("java:S1130")
    protected Context createContext() throws NamingException {
        return contextInst.get();
    }

    // Simple helper for printing the jndi names
    private String joinJndiNames(String[] strings) {
        StringBuilder sb = new StringBuilder();

        for (String string : strings) {
            sb.append(string).append(", ");
        }
        return sb.toString();
    }

    /**
     * Helper method that checks if the given String has a non-empty value.
     *
     * @param string
     *     String to be checked.
     *
     * @return true if <tt>string</tt> is not null and has non-empty value; false otherwise.
     */
    private boolean hasValue(String string) {
        return string != null && !string.trim().isEmpty();
    }    

    private boolean isMoreThanOneValueTrue(boolean... values) {
        boolean trueFound = false;
        for (boolean value : values) {
            if (value) {
                if (trueFound) {
                    return true;
                }
                trueFound = true;
            }
        }
        return false;
    }
}
