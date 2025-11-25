package org.navadiya.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.navadiya.config.ApplicationConfig;
import org.navadiya.driver.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.time.Duration;
import org.navadiya.util.WaitUtils;

/**
 * BaseTest initializes and tears down WebDriver per test method using DriverManager (ThreadLocal).
 */
public class BaseTest {
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);


    @BeforeMethod(alwaysRun = true)
    public void setUp(ITestContext context) throws Exception {
        // If TestNG provides a browser parameter at test level, ensure it's set as a System property
        try {
            if (context != null && context.getCurrentXmlTest() != null) {
                String browserParam = context.getCurrentXmlTest().getParameter("browser");
                // create driver with the specific browser for this test (thread-local)
                if (browserParam != null && !browserParam.isEmpty()) {
                    DriverManager.createDriver(browserParam);
                } else {
                    DriverManager.createDriver("chrome");
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

    /**
     * Get the WebDriver instance for the current thread.
     * @return WebDriver instance
     */
    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }

    /**
     * Navigate to the environment-specific URL configured in environments.properties,
     * then wait for the page to fully load.
     */
    protected void navigateToApp() {
        WebDriver driver = DriverManager.getDriver();
        String envUrl = ApplicationConfig.getEnvProperty("app.url");
        log.info("Navigating to URL: {} (env={})", envUrl, ApplicationConfig.getEnv());
        driver.get(envUrl);
        WaitUtils.waitForPageLoad(driver, defaultTimeout());
    }

    /**
     * Default explicit wait timeout derived from application.properties (app.timeout.seconds)
     */
    protected Duration defaultTimeout() {
        String t = ApplicationConfig.getProperty("app.timeout.seconds");
        int seconds = 15;
        try { if (t != null) seconds = Integer.parseInt(t); } catch (Exception ignored) {}
        return Duration.ofSeconds(seconds);
    }
}
