package org.navadiya.tests;

import org.navadiya.BaseTest;
import org.navadiya.config.ApplicationConfig;
import org.navadiya.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LinkPresenceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(LinkPresenceTest.class);

    @Test
    public void hasAtLeastOneLink() {
        WebDriver driver = DriverManager.getDriver();
        String envUrl = ApplicationConfig.getEnvProperty("app.url");
        if (envUrl == null || envUrl.isEmpty()) envUrl = ApplicationConfig.getProperty("app.url");
        driver.get(envUrl);
        boolean hasLink = !driver.findElements(By.tagName("a")).isEmpty();
        log.info("LinkPresenceTest: links found={}", hasLink);
        Assert.assertTrue(hasLink, "Expected at least one link on the page");
    }
}
