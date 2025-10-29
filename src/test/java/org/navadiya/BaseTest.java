package org.navadiya;

import lombok.extern.slf4j.Slf4j;
import org.navadiya.driver.DriverManager;
import org.navadiya.listeners.AllureReportTimestampListener;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

/**
 * BaseTest initializes and tears down WebDriver per test method using DriverManager (ThreadLocal).
 */
@Slf4j
@Listeners({AllureReportTimestampListener.class})
public class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestContext context) throws Exception {
        // If TestNG provides a browser parameter at test level, ensure it's set as a System property
        try {
            if (context != null && context.getCurrentXmlTest() != null) {
                String browserParam = context.getCurrentXmlTest().getParameter("browser");
                String envParam = context.getCurrentXmlTest().getParameter("env");
                // create driver with the specific browser for this test (thread-local)
                if (browserParam != null && !browserParam.isEmpty()) {
                    DriverManager.createDriver(browserParam);
                } else {
                    DriverManager.createDriver("chrome");
                }
                if (envParam != null && !envParam.isEmpty()) {
                    System.setProperty("env", envParam);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        // if driver not created above (no currentXmlTest) ensure creation
        if (DriverManager.getDriver() == null) {
            DriverManager.createDriver("chrome");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
