package org.jboss.arquillian.junit;

import org.jboss.arquillian.core.spi.MyCustomException;
import org.jboss.arquillian.test.spi.LifecycleMethodExecutor;

abstract class ArquillianTestClassLifecycleManager extends AdaptorManager {

    protected void handleSuiteLevelFailure(Throwable initializationException) {
        throw new JunitRuntimeException(
            "Arquillian initialization has already been attempted, but failed. See previous exceptions for cause",
            initializationException);
    }

    protected void handleBeforeSuiteFailure(Exception e) throws Exception {
        State.runnerFinished();
        if (State.isLastRunner()) {
            State.clean();
        }
        throw e;
    }


    void beforeTestClassPhase(Class<?> testClass) throws Exception {
        State.runnerStarted();
        initializeAdaptor();

        // initialization ok, run children
        if (State.hasTestAdaptor()) {
            setAdaptor(State.getTestAdaptor());
        }

        getAdaptor().beforeClass(testClass, LifecycleMethodExecutor.NO_OP);
    }

    void afterTestClassPhase(Class<?> testClass) throws MyCustomException {
        getAdaptor().afterClass(testClass, LifecycleMethodExecutor.NO_OP);
        shutdown(getAdaptor());
    }
}
