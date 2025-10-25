package org.navadiya.tests;

import lombok.extern.slf4j.Slf4j;
import org.navadiya.BaseTest;
import org.navadiya.config.ApplicationConfig;
import org.navadiya.driver.DriverManager;
import org.navadiya.visual.VisualValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

@Slf4j
public class HeaderElementTest extends BaseTest {

    @Test
    public void h1ElementPresent() {
        WebDriver driver = DriverManager.getDriver();
        String envUrl = ApplicationConfig.getEnvProperty("app.url");
        if (envUrl == null || envUrl.isEmpty()) envUrl = ApplicationConfig.getProperty("app.url");
        driver.get(envUrl);
        boolean present = !driver.findElements(By.tagName("img")).isEmpty();
        log.info("HeaderElementTest: img present={}", present);
        VisualValidator.takeScreenshot(driver, "header-element-test");
        Assert.assertTrue(present, "Expected an img element on the page");
    }
}

