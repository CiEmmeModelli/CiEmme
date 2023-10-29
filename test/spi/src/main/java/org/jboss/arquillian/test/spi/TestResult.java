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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A test result which may be serialized for communicate between client and
 * server
 *
 * @author Pete Muir
 * @author <a href="mailto:aknutsen@redhat.com">Aslak Knutsen</a>
 */
@SuppressWarnings("java:S1133")
public final class TestResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private Status status;
    transient private Throwable throwable;
    private String description = "";
    private ExceptionProxy exceptionProxy;
    private long start;
    private long end;

    /**
     * Constructs a new TestResult with the given status.
     *
     * @param status The status of the test result.
     * @deprecated Use the constructor TestResult(Status) instead.
     */
    @Deprecated
    public TestResult(Status status, String description) {
        this(status);
        this.description = description;
    }

    /**
     * Create a empty result.<br/>
     * <br/>
     * Start time is set to Current Milliseconds.
     */
    /**
     * Constructs a new TestResult with the given status.
     *
     * @deprecated Use the constructor TestResult(Status) instead.
     */
    @Deprecated
    public TestResult() {
        this(null);
    }

    /**
     * Create a new TestResult.<br/>
     * <br/>
     * Start time is set to Current Milliseconds.
     *
     * @param status
     *     The result status.
     *      @deprecated Use the constructor TestResult(Status) instead.
     */
    @Deprecated
    public TestResult(Status status) {
        this(status, (Throwable) null);
    }

    /**
     * Create a new TestResult.<br/>
     * <br/>
     * Start time is set to Current Milliseconds.
     *
     * @param status
     *     The result status.
     * @param throwable
     *     thrown exception if any
     *  @deprecated Use the constructor TestResult(Status) instead.
     */
    @Deprecated
    public TestResult(Status status, Throwable throwable) {
        this.status = status;
        setThrowable(throwable);

        this.start = System.currentTimeMillis();
    }

    public static TestResult passed() {
        return new TestResult(Status.PASSED);
    }

    public static TestResult passed(String description) {
        return new TestResult(Status.PASSED, description);
    }

    public static TestResult skipped(Throwable cause) {
        return new TestResult(Status.SKIPPED, cause);
    }

    public static TestResult skipped(String description) {
        return new TestResult(Status.SKIPPED, description);
    }

    public static TestResult skipped() {
        return new TestResult(Status.SKIPPED);
    }

    public static TestResult failed(Throwable cause) {
        return new TestResult(Status.FAILED, cause);
    }

    public static TestResult flatten(Collection<TestResult> results) {
        final TestResult combinedResult = new TestResult(Status.PASSED);
        final Map<Status, TestResult> resultsPerStatus = new EnumMap<>(Status.class);
        final List<Throwable> allExceptions = new ArrayList<Throwable>();

        for (TestResult result : results) {
            if (result == null) {
                continue;  // For some strange reason sometimes results contain null objects
            }
            resultsPerStatus.put(result.getStatus(), result);
            if (result.getThrowable() != null) {
                allExceptions.add(result.getThrowable());
            }
            combinedResult.addDescription(
                String.format("%s: '%s'%n", result.getStatus().name(), result.getDescription()));
        }

        propagateTestResultStatus(combinedResult, resultsPerStatus);
        propagateExceptions(combinedResult, allExceptions);

        return combinedResult;
    }

    private static void propagateExceptions(TestResult combinedResult, List<Throwable> allExceptions) {
        if (!allExceptions.isEmpty()) {
            if (allExceptions.size() == 1) {
                combinedResult.setThrowable(allExceptions.get(0));
            } else {
                combinedResult.setThrowable(new CombinedException("Combined test result exceptions", allExceptions));
            }
        }        
    }

    private static void propagateTestResultStatus(TestResult combinedResult, Map<Status, TestResult> resultsPerStatus) {
        if (resultsPerStatus.containsKey(Status.FAILED)) {
            combinedResult.setStatus(Status.FAILED);
        } else if (resultsPerStatus.containsKey(Status.PASSED)) {
            combinedResult.setStatus(Status.PASSED);
        } else if (resultsPerStatus.containsKey(Status.SKIPPED)) {
            combinedResult.setStatus(Status.SKIPPED);
        }
    }

    /**
     * Get the status of this test
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Constructs a new TestResult with the given status.
     *
     * @param status The status of the test result.
     * @deprecated Use the constructor TestResult(Status) instead.
     */
    @Deprecated
    public TestResult setStatus(Status status) {
        this.status = status;
        return this;
    }

    public String getDescription() {
        return description;
    }


    /**
     * Constructs a new TestResult with the given status.
     *
     * @param description The description of the test result.
     * @deprecated Use the constructor TestResult(Status) instead.
     */
    @Deprecated
    public void setDescription(String description) {
        this.description = description;
    }

    public void addDescription(String description) {
        this.description += description;
    }

    /**
     * If the test failed, the exception that was thrown. It does not need to be
     * the root cause.
     */
    public Throwable getThrowable() {
        if (throwable == null && exceptionProxy != null) {
                throwable = exceptionProxy.createException();
        }
        return throwable;
    }

    public TestResult setThrowable(Throwable throwable) {
        this.throwable = throwable;
        this.exceptionProxy = ExceptionProxy.createForException(throwable);
        return this;
    }

    /**
     * Get the start time.
     *
     * @return Start time in milliseconds
     */
    public long getStart() {
        return start;
    }

    /**
     * Set the start time of the test.
     *
     * @param start
     *     Start time in milliseconds
     */
    public TestResult setStart(long start) {
        this.start = start;
        return this;
    }

    /**
     * Get the end time.
     *
     * @return End time in milliseconds
     */
    public long getEnd() {
        return end;
    }

    /**
     * Set the end time of the test.
     *
     * @param end
     *     time in milliseconds
     */
    public TestResult setEnd(long end) {
        this.end = end;
        return this;
    }

    public ExceptionProxy getExceptionProxy() {

        return exceptionProxy;
    }

    @Override
    public String toString() {
        long time = (end > 0 ? end - start : System.currentTimeMillis() - start);
        return "TestResult[status=" + status + ",time=" + time + "ms]";
    }

    /**
     * The test status
     *
     * @author Pete Muir
     */
    public enum Status {
        /**
         * The test passed
         */
        PASSED,
        /**
         * The test failed
         */
        FAILED,
        /**
         * The test was skipped due to some deployment problem
         */
        SKIPPED;
    }
}
