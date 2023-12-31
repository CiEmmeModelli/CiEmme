package org.jboss.arquillian.junit;

import org.jboss.arquillian.junit.JUnitTestBaseClass.Cycle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import static org.jboss.arquillian.junit.JUnitTestBaseClass.wasCalled;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("java:S3577")
public class ClassWithArquillianClassAndMethodRuleWithExceptionInAfterRule {

    @ClassRule
    public static ArquillianTestClass arquillianTestClass = new ArquillianTestClass();

    @Rule
    public ArquillianTest arquillianTest = new ArquillianTest();

    @Rule
    public MethodRule rule = new MethodRule() {
        @Override
        public Statement apply(final Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    base.evaluate();
                    throw new RuntimeException("AfterRuleException");
                }
            };
        }
    };

    @BeforeClass
    public static void beforeClass() throws Throwable {
        wasCalled(Cycle.BEFORE_CLASS);
    }

    @AfterClass
    public static void afterClass() throws Throwable {
        wasCalled(Cycle.AFTER_CLASS);
    }

    @Before
    public void before() throws Throwable {
        wasCalled(Cycle.BEFORE);
    }

    @After
    public void after() throws Throwable {
        wasCalled(Cycle.AFTER);
    }

     
    @Test
    public void shouldBeInvoked() throws Throwable {
        assertNotNull(arquillianTest);
        wasCalled(Cycle.TEST);
    }
    
}
