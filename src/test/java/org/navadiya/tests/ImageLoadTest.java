package org.navadiya.tests;

import lombok.extern.slf4j.Slf4j;
import org.navadiya.BaseTest;
import org.navadiya.config.ApplicationConfig;
import org.navadiya.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

@Slf4j
public class ImageLoadTest extends BaseTest {

    @Test
    public void imagesHaveSrc() {
        WebDriver driver = DriverManager.getDriver();
        String envUrl = ApplicationConfig.getEnvProperty("app.url");
        if (envUrl == null || envUrl.isEmpty()) envUrl = ApplicationConfig.getProperty("app.url");
        driver.get(envUrl);
        List<WebElement> imgs = driver.findElements(By.xpath("//img[@id='vow-login-logo']"));
        log.info("ImageLoadTest: found {} images", imgs.size());
        for (WebElement img : imgs) {
            String src = img.getAttribute("src");
            // Allow images without src in some cases, but ensure no NullPointerException
            Assert.assertNotNull(src, "Image src should not be null");
        }
    }
}
