package org.navadiya;

import org.testng.annotations.Test;

/**
 * TestNG wrapper to run SuiteRunner via maven-surefire-plugin.
 * This allows Jenkins to use 'mvn test' instead of 'exec:java'.
 */
public class SuiteRunnerTest {

    @Test
    public void runTestSuite() {
        // Invoke SuiteRunner's main method to execute the dynamic test suite
        SuiteRunner.main(new String[]{});
    }
}

