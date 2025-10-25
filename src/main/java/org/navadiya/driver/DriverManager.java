package org.navadiya.driver;

import org.navadiya.config.ApplicationConfig;
import org.openqa.selenium.WebDriver;

/**
 * Thread-safe WebDriver holder using ThreadLocal.
 */
public class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    // keep track of which browser instance is associated with this thread's driver
    private static final ThreadLocal<String> BROWSER = new ThreadLocal<>();

    private DriverManager() {}

    /** Create a WebDriver instance for current thread if not present. */
    public static void createDriver() throws Exception {
        createDriver(null);
    }

    /**
     * Create a WebDriver instance for the current thread using the provided browser value.
     * If browser is null, fall back to System property or application config defaults.
     */
    public static void createDriver(String browser) throws Exception {
        String desired = browser;
            if (desired == null || desired.isEmpty()) desired = System.getProperty("browser");
        if (desired == null || desired.isEmpty()) {
            String[] browsers = ApplicationConfig.getBrowsers();
            desired = (browsers != null && browsers.length > 0) ? browsers[0] : "chrome";
        }
        desired = desired.trim();

        WebDriver existing = DRIVER.get();
        String existingBrowser = BROWSER.get();

        // if an existing driver is present but for a different browser, quit and recreate
        if (existing != null && existingBrowser != null && !existingBrowser.equalsIgnoreCase(desired)) {
            try {
                existing.quit();
            } catch (Exception ignore) {}
            DRIVER.remove();
            BROWSER.remove();
            existing = null;
        }

        if (existing == null) {
            WebDriver wd = WebDriverFactory.createInstance(desired);
            DRIVER.set(wd);
            BROWSER.set(desired);
        }
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver wd = DRIVER.get();
        if (wd != null) {
            try {
                wd.quit();
            } catch (Exception ignore) {
            }
            DRIVER.remove();
        }
    }
}
