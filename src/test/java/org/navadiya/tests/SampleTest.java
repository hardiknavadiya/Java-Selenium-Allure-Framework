package org.navadiya.tests;

import org.navadiya.BaseTest;
import org.navadiya.config.ApplicationConfig;
import org.navadiya.driver.DriverManager;
import org.navadiya.visual.VisualValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(SampleTest.class);

    @Test
    public void openExampleAndAssertTitle() {
        WebDriver driver = DriverManager.getDriver();

        // prefer environment-specific URL from environments.properties, fallback to application.properties
        String envUrl = ApplicationConfig.getEnvProperty("app.url");
        if (envUrl == null || envUrl.isEmpty()) envUrl = ApplicationConfig.getProperty("app.url");

        log.info("Navigating to URL: {} (env={})", envUrl, ApplicationConfig.getEnv());
        driver.get(envUrl);

        // title should be non-null and non-empty
        Assert.assertTrue(driver.getTitle() != null && !driver.getTitle().isEmpty(), "Page title should be present");

        // take screenshot for reference (Allure attachment handled inside VisualValidator if configured)
        VisualValidator.takeScreenshot(driver, "sample-test");

        // simple element check if present
        try {
            driver.findElement(By.tagName("h1"));
        } catch (Exception ignored) {}
    }
}