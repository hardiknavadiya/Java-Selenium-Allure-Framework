package org.navadiya.driver;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.navadiya.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;

/**
 * Manages Appium capabilities for Android and iOS platforms.
 * Reads configuration from application.properties and builds appropriate options.
 */
public class AppiumCapabilitiesManager {

    private static final Logger log = LoggerFactory.getLogger(AppiumCapabilitiesManager.class);

    /**
     * Creates Android capabilities using UiAutomator2Options
     */
    public static UiAutomator2Options createAndroidCapabilities() {
        UiAutomator2Options options = new UiAutomator2Options();

        // Basic Android capabilities
        String platformName = ApplicationConfig.getProperty("appium.android.platformName");
        String deviceName = ApplicationConfig.getProperty("appium.android.deviceName");
        String browserName = ApplicationConfig.getProperty("appium.android.browserName");
        String appPath = ApplicationConfig.getProperty("appium.android.app");
        String appPackage = ApplicationConfig.getProperty("appium.android.appPackage");
        String appActivity = ApplicationConfig.getProperty("appium.android.appActivity");
        String automationName = ApplicationConfig.getProperty("appium.android.automationName");
        String udid = ApplicationConfig.getProperty("appium.android.udid");

        if (platformName != null && !platformName.isEmpty()) {
            options.setPlatformName(platformName);
        }

        if (deviceName != null && !deviceName.isEmpty()) {
            options.setDeviceName(deviceName);
        } else {
            options.setDeviceName("Android Emulator"); // Default
        }

        if (browserName != null && !browserName.isEmpty()) {
            options.setCapability("browserName",browserName);
        }

        options.setCapability("appium:autoAcceptAlerts",true);


        // Automation name (default: UiAutomator2)
        if (automationName != null && !automationName.isEmpty()) {
            options.setAutomationName(automationName);
        } else {
            options.setAutomationName("UiAutomator2");
        }

        // App installation
        if (appPath != null && !appPath.isEmpty()) {
            File appFile = new File(appPath);
            if (appFile.exists()) {
                options.setApp(appFile.getAbsolutePath());
                log.info("Android app set to: {}", appFile.getAbsolutePath());
            } else {
                log.warn("Android app file not found: {}", appPath);
            }
        }

        // App package and activity (for app already installed on device)
        if (appPackage != null && !appPackage.isEmpty()) {
            options.setAppPackage(appPackage);
        }
        if (appActivity != null && !appActivity.isEmpty()) {
            options.setAppActivity(appActivity);
        }

        // Device UDID for specific device targeting
        if (udid != null && !udid.isEmpty()) {
            options.setUdid(udid);
        }

        // Additional settings
        options.setNewCommandTimeout(Duration.ofSeconds(300));

        // Auto-grant permissions
        String autoGrantPermissions = ApplicationConfig.getProperty("appium.android.autoGrantPermissions");
        if ("true".equalsIgnoreCase(autoGrantPermissions)) {
            options.setAutoGrantPermissions(true);
        }

        // No reset (keep app state between sessions)
        String noReset = ApplicationConfig.getProperty("appium.android.noReset");
        if ("true".equalsIgnoreCase(noReset)) {
            options.setNoReset(true);
        }

        // Full reset (clear app data)
        String fullReset = ApplicationConfig.getProperty("appium.android.fullReset");
        if ("true".equalsIgnoreCase(fullReset)) {
            options.setFullReset(true);
        }

        log.info("Android capabilities created: {}", options.asMap());
        return options;
    }

    /**
     * Creates iOS capabilities using XCUITestOptions
     */
    public static XCUITestOptions createiOSCapabilities() {
        XCUITestOptions options = new XCUITestOptions();

        // Basic iOS capabilities
        String platformVersion = ApplicationConfig.getProperty("appium.ios.platformVersion");
        String deviceName = ApplicationConfig.getProperty("appium.ios.deviceName");
        String appPath = ApplicationConfig.getProperty("appium.ios.app");
        String bundleId = ApplicationConfig.getProperty("appium.ios.bundleId");
        String automationName = ApplicationConfig.getProperty("appium.ios.automationName");
        String udid = ApplicationConfig.getProperty("appium.ios.udid");

        if (platformVersion != null && !platformVersion.isEmpty()) {
            options.setPlatformVersion(platformVersion);
        }

        if (deviceName != null && !deviceName.isEmpty()) {
            options.setDeviceName(deviceName);
        } else {
            options.setDeviceName("iPhone Simulator"); // Default
        }

        // Automation name (default: XCUITest)
        if (automationName != null && !automationName.isEmpty()) {
            options.setAutomationName(automationName);
        } else {
            options.setAutomationName("XCUITest");
        }

        // App installation
        if (appPath != null && !appPath.isEmpty()) {
            File appFile = new File(appPath);
            if (appFile.exists()) {
                options.setApp(appFile.getAbsolutePath());
                log.info("iOS app set to: {}", appFile.getAbsolutePath());
            } else {
                log.warn("iOS app file not found: {}", appPath);
            }
        }

        // Bundle ID (for app already installed on device)
        if (bundleId != null && !bundleId.isEmpty()) {
            options.setBundleId(bundleId);
        }

        // Device UDID for specific device targeting
        if (udid != null && !udid.isEmpty()) {
            options.setUdid(udid);
        }

        // Additional settings
        options.setNewCommandTimeout(Duration.ofSeconds(300));

        // No reset (keep app state between sessions)
        String noReset = ApplicationConfig.getProperty("appium.ios.noReset");
        if ("true".equalsIgnoreCase(noReset)) {
            options.setNoReset(true);
        }

        // Full reset (clear app data)
        String fullReset = ApplicationConfig.getProperty("appium.ios.fullReset");
        if ("true".equalsIgnoreCase(fullReset)) {
            options.setFullReset(true);
        }

        // Auto accept alerts
        String autoAcceptAlerts = ApplicationConfig.getProperty("appium.ios.autoAcceptAlerts");
        if ("true".equalsIgnoreCase(autoAcceptAlerts)) {
            options.setAutoAcceptAlerts(true);
        }

        log.info("iOS capabilities created: {}", options.asMap());
        return options;
    }

    /**
     * Gets Appium server URL from configuration
     */
    public static String getAppiumServerUrl() {
        String url = ApplicationConfig.getProperty("appium.server.url");
        if (url == null || url.isEmpty()) {
            url = "http://127.0.0.1:4723"; // Default Appium server URL
        }
        log.info("Appium server URL: {}", url);
        return url;
    }
}

