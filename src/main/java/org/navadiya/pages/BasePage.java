package org.navadiya.pages;

import org.navadiya.config.ApplicationConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BasePage provides common functionality for all page objects.
 * Uses AjaxElementLocatorFactory to handle dynamic element loading with intelligent waits.
 */
public abstract class BasePage {
    private static final Logger log = LoggerFactory.getLogger(BasePage.class);

    protected WebDriver driver;

    /**
     * Default timeout for AjaxElementLocatorFactory (in seconds)
     */
    private static final int DEFAULT_AJAX_TIMEOUT = 15;

    /**
     * Initialize page with AjaxElementLocatorFactory
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        int timeout = getAjaxTimeout();
        log.info("========================================");
        log.info("Timeout configured: {} seconds", timeout);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, timeout), this);
        log.info("✅ AjaxElementLocatorFactory initialization completed for {}", this.getClass().getSimpleName());
    }

    /**
     * Get the AJAX timeout from application config or use default
     * @return timeout in seconds
     */
    private int getAjaxTimeout() {
        try {
            String timeoutStr = ApplicationConfig.getProperty("app.timeout.seconds");
            if (timeoutStr != null && !timeoutStr.isEmpty()) {
                return Integer.parseInt(timeoutStr);
            }
        } catch (Exception e) {
            log.warn("Could not parse app.timeout.seconds, using default: {}", DEFAULT_AJAX_TIMEOUT);
        }
        return DEFAULT_AJAX_TIMEOUT;
    }
}

