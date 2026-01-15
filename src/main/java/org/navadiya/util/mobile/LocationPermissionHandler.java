package org.navadiya.util.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Simple utility to handle location permission prompts for Android and iOS.
 * Specifically designed for the location permission dialog shown in the image.
 */
public class LocationPermissionHandler {

    private static final Logger log = LoggerFactory.getLogger(LocationPermissionHandler.class);
    private static final int DEFAULT_TIMEOUT = 5;

    /**
     * Handles location permission by clicking "Allow while visiting the site" or "Allow while using".
     *
     * @param driver The WebDriver instance
     * @return true if permission was handled, false otherwise
     */
    public static boolean allowWhileUsing(WebDriver driver) {
        return handleLocationPermission(driver, "Allow while visiting the site", "Allow While Using App");
    }

    /**
     * Handles location permission by clicking "Allow this time" or "Allow Once".
     *
     * @param driver The WebDriver instance
     * @return true if permission was handled, false otherwise
     */
    public static boolean allowThisTime(WebDriver driver) {
        return handleLocationPermission(driver, "Allow this time", "Allow Once");
    }

    /**
     * Handles location permission by clicking "Never allow" or "Don't Allow".
     *
     * @param driver The WebDriver instance
     * @return true if permission was handled, false otherwise
     */
    public static boolean neverAllow(WebDriver driver) {
        return handleLocationPermission(driver, "Never allow", "Don't Allow");
    }

    /**
     * Main method to handle location permission with specific button text.
     *
     * @param driver The WebDriver instance
     * @param androidText The text on Android button (e.g., "Allow while visiting the site")
     * @param iosText The text on iOS button (e.g., "Allow While Using App")
     * @return true if permission was handled, false otherwise
     */
    private static boolean handleLocationPermission(WebDriver driver, String androidText, String iosText) {
        try {
            AppiumDriver appiumDriver = (AppiumDriver) driver;
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));

            boolean isAndroid = isAndroidDevice(appiumDriver);
            String buttonText = isAndroid ? androidText : iosText;

            log.info("Attempting to handle location permission on {}: '{}'",
                    isAndroid ? "Android" : "iOS", buttonText);

            if (isAndroid) {
                return handleAndroid(wait, androidText);
            } else {
                return handleIOS(wait, iosText);
            }
        } catch (Exception e) {
            log.debug("No location permission prompt found: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Handle Android location permission.
     */
    private static boolean handleAndroid(WebDriverWait wait, String buttonText) {
        try {
            // Try multiple Android locator strategies
            By[] locators = {
                    // Web-based (for Chrome/WebView) - using standard Selenium locators
                    By.xpath(String.format("//button[normalize-space()='%s']", buttonText)),
                    By.xpath(String.format("//div[normalize-space()='%s']", buttonText)),
                    By.xpath(String.format("//*[normalize-space()='%s']", buttonText)),
                    By.xpath(String.format("//*[contains(text(), '%s')]", buttonText)),
                    // Native app locators (for actual Android apps)
                    AppiumBy.xpath(String.format("//*[@text='%s']", buttonText)),
                    // Resource IDs for standard Android permission buttons
                    AppiumBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"),
                    AppiumBy.id("com.android.permissioncontroller:id/permission_allow_one_time_button"),
                    AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button")
            };

            for (By locator : locators) {
                try {
                    WebElement button = wait.until(ExpectedConditions.elementToBeClickable(locator));
                    button.click();
                    log.info("✅ Android location permission handled: '{}'", buttonText);
                    return true;
                } catch (Exception e) {
                    log.trace("Locator failed: {}", locator);
                }
            }
            return false;
        } catch (Exception e) {
            log.debug("Could not handle Android permission: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Handle iOS location permission.
     */
    private static boolean handleIOS(WebDriverWait wait, String buttonText) {
        try {
            // Try multiple iOS locator strategies
            By[] locators = {
                    // Accessibility ID (most reliable for iOS)
                    AppiumBy.accessibilityId(buttonText),
                    // XCUITest predicates
                    AppiumBy.iOSNsPredicateString(String.format("name == '%s'", buttonText)),
                    AppiumBy.iOSNsPredicateString(String.format("label == '%s'", buttonText)),
                    // Button with name
                    AppiumBy.xpath(String.format("//XCUIElementTypeButton[@name='%s']", buttonText)),
                    AppiumBy.xpath(String.format("//XCUIElementTypeButton[@label='%s']", buttonText))
            };

            for (By locator : locators) {
                try {
                    WebElement button = wait.until(ExpectedConditions.elementToBeClickable(locator));
                    button.click();
                    log.info("✅ iOS location permission handled: '{}'", buttonText);
                    return true;
                } catch (Exception e) {
                    log.trace("Locator failed: {}", locator);
                }
            }
            return false;
        } catch (Exception e) {
            log.debug("Could not handle iOS permission: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if device is Android.
     */
    private static boolean isAndroidDevice(AppiumDriver driver) {
        try {
            String platformName = driver.getCapabilities().getPlatformName().toString();
            return platformName != null && platformName.equalsIgnoreCase("android");
        } catch (Exception e) {
            return driver.getClass().getSimpleName().toLowerCase().contains("android");
        }
    }
}

