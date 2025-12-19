package org.navadiya.pages.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.navadiya.config.ApplicationConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base page for mobile apps (Android and iOS).
 * Provides common functionality for mobile page objects.
 */
public abstract class BaseMobilePage {

    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final AppiumDriver driver;
    protected final WebDriverWait wait;

    public BaseMobilePage(WebDriver driver) {
        this.driver = (AppiumDriver) driver;

        // Get timeout from configuration
        int timeoutSeconds = 15; // default
        try {
            String timeout = ApplicationConfig.getProperty("app.timeout.seconds");
            if (timeout != null && !timeout.isEmpty()) {
                timeoutSeconds = Integer.parseInt(timeout);
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid timeout configuration, using default: {}", timeoutSeconds);
        }

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        // Initialize page elements with AppiumFieldDecorator
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(timeoutSeconds)), this);
    }

    /**
     * Hides the keyboard if it's showing
     */
    protected void hideKeyboard() {
        try {
            if (isAndroid()) {
                ((AndroidDriver) driver).hideKeyboard();
            } else if (isIOS()) {
                ((IOSDriver) driver).hideKeyboard();
            }
            log.info("Keyboard hidden");
        } catch (Exception e) {
            log.debug("Keyboard was not shown or couldn't be hidden: {}", e.getMessage());
        }
    }

    /**
     * Scrolls down on the screen
     */
    protected void scrollDown() {
        try {
            // Implementation depends on platform
            log.info("Scrolling down");
        } catch (Exception e) {
            log.error("Failed to scroll down", e);
        }
    }

    /**
     * Checks if the device is Android
     */
    protected boolean isAndroid() {
        try {
            String platformName = driver.getCapabilities().getPlatformName().toString();
            return platformName != null && platformName.equalsIgnoreCase("android");
        } catch (Exception e) {
            return driver instanceof AndroidDriver;
        }
    }

    /**
     * Checks if the device is iOS
     */
    protected boolean isIOS() {
        try {
            String platformName = driver.getCapabilities().getPlatformName().toString();
            return platformName != null && platformName.equalsIgnoreCase("ios");
        } catch (Exception e) {
            return driver instanceof IOSDriver;
        }
    }

    /**
     * Puts the app in background for specified seconds
     */
    protected void backgroundApp(int seconds) {
        try {
            if (isAndroid()) {
                ((AndroidDriver) driver).runAppInBackground(Duration.ofSeconds(seconds));
            } else if (isIOS()) {
                ((IOSDriver) driver).runAppInBackground(Duration.ofSeconds(seconds));
            }
            log.info("App backgrounded for {} seconds", seconds);
        } catch (Exception e) {
            log.error("Failed to background app", e);
        }
    }

    /**
     * Closes the app
     */
    protected void closeApp() {
        try {
            if (isAndroid()) {
                ((AndroidDriver) driver).terminateApp(driver.getCapabilities().getCapability("appPackage").toString());
            } else if (isIOS()) {
                ((IOSDriver) driver).terminateApp(driver.getCapabilities().getCapability("bundleId").toString());
            }
            log.info("App closed");
        } catch (Exception e) {
            log.error("Failed to close app", e);
        }
    }

    /**
     * Launches the app
     */
    protected void launchApp() {
        try {
            if (isAndroid()) {
                String appPackage = (String) driver.getCapabilities().getCapability("appPackage");
                if (appPackage != null) {
                    ((AndroidDriver) driver).activateApp(appPackage);
                }
            } else if (isIOS()) {
                String bundleId = (String) driver.getCapabilities().getCapability("bundleId");
                if (bundleId != null) {
                    ((IOSDriver) driver).activateApp(bundleId);
                }
            }
            log.info("App launched");
        } catch (Exception e) {
            log.error("Failed to launch app", e);
        }
    }

    /**
     * Gets current activity (Android only)
     */
    protected String getCurrentActivity() {
        if (isAndroid()) {
            try {
                return ((AndroidDriver) driver).currentActivity();
            } catch (Exception e) {
                log.error("Failed to get current activity", e);
            }
        }
        return null;
    }
}


