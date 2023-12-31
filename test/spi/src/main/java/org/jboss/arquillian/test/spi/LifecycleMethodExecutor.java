/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010 Red Hat Inc. and/or its affiliates and other contributors
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
package org.jboss.arquillian.test.spi;

/**
 * Generic wrapper for invoking Lifecycle methods. <br/>
 * <br/>
 * Used to e.g. veto invocation of @Before/@After methods on the Client side.
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@SuppressWarnings("java:S112")
public interface LifecycleMethodExecutor {
    public static final LifecycleMethodExecutor NO_OP = new LifecycleMethodExecutor() {
        public void invoke() throws Throwable {
            // Questo metodo è vuoto di proposito in quanto rappresenta un'esecuzione "senza operazione".
        }
    };

    void invoke() throws Throwable;
}