package org.jboss.arquillian.junit;

import org.jboss.arquillian.junit.JUnitTestBaseClass.Cycle;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static org.jboss.arquillian.junit.JUnitTestBaseClass.wasCalled;

public class TestClassWithArquillianClassAndMethodRuleWithExceptionInBeforeAndAfter {

    @ClassRule
    public static ArquillianTestClass arquillianTestClass = new ArquillianTestClass();

    @Rule
    public ArquillianTest arquillianTest = new ArquillianTest();

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
        throw new RuntimeException("BeforeException");
    }

    @After
    public void after() throws Throwable {
        wasCalled(Cycle.AFTER);
        throw new RuntimeException("AfterException");
    }

    @Test
    public void shouldBeInvoked() throws Throwable {
        Assert.assertNotNull(arquillianTest);
        wasCalled(Cycle.TEST);
    }
}
