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
package org.jboss.arquillian.test.spi;

import java.lang.reflect.Method;

import org.jboss.arquillian.core.spi.MyCustomException;
import org.jboss.arquillian.test.spi.event.suite.TestLifecycleEvent;

/**
 * TestRunnerAdaptor
 * <p>
 * Need to be Thread-safe
 *
 * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
 * @version $Revision: $
 */
public interface TestRunnerAdaptor {
    /**
     * Activate a new TestSuite.<br/>
     * This will trigger the BeforeSuite event.
     *
     * @throws CustomExc
     */
    void beforeSuite() throws CustomExc;

    /**
     * Deactivate the TestSuite.<br/>
     * This will trigger the AfterSuite event.
     *
     * @throws CustomExc
     */
    void afterSuite() throws CustomExc;

    /**
     * Activate a new TestClass.<br/>
     * This will trigger the BeforeClass event.
     *
     * @throws MyCustomException
     */
    void beforeClass(Class<?> testClass, LifecycleMethodExecutor executor) throws MyCustomException;

    /**
     * Deactivate the TestClass.<br/>
     * This will trigger the AfterClass event.
     *
     * @throws MyCustomException
     */
    void afterClass(Class<?> testClass, LifecycleMethodExecutor executor) throws MyCustomException;

    /**
     * Activate a new TestInstance.<br/>
     * This will trigger the Before event.
     *
     * @throws MyCustomException
     */
    void before(Object testInstance, Method testMethod, LifecycleMethodExecutor executor) throws MyCustomException;

    /**
     * Deactivate the TestInstance.<br/>
     * This will trigger the After event.
     *
     * @throws CustomExc
     */
    void after(Object testInstance, Method testMethod, LifecycleMethodExecutor executor) throws CustomExc;

    /**
     * Activate a TestMethod execution.<br/>
     * This will trigger the Test event.
     *
     * @throws CustomExc
     */
    TestResult test(TestMethodExecutor testMethodExecutor) throws CustomExc;

    /**
     * Fire any custom Test Lifecycle event.<br/>
     * <br/>
     * This can be used by a TestFramework to trigger e.g. additional Lifecycle
     * phases not described directly by the Test SPI.
     *
     * @param event
     *     Any event
     *
     * @throws CustomExc
     */
    <T extends TestLifecycleEvent> void fireCustomLifecycle(T event) throws CustomExc;

    /**
     * Shutdown Arquillian cleanly.
     */
    void shutdown();
}
