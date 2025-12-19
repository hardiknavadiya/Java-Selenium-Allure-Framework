package org.navadiya.util;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

public class ScreenshotUtils {
    /**
     * Take screenshot and attach to Allure report
     */
    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);

    public static void takeScreenshot(WebDriver driver, String name) {
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            log.error("Failed to take screenshot: {}", e.getMessage());
        }
    }
}
