package org.navadiya.tests;

import io.qameta.allure.Step;
import org.navadiya.util.WaitUtils;
import org.navadiya.locators.AppLocators;
import org.navadiya.visual.VisualValidator;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;


public class amazonSignIn extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(amazonSignIn.class);

    @Test
    @Step("Open example application and assert title")
    public void openExampleAndAssertTitle() {
        navigateToApp();
        WebDriver driver = getDriver();
        Duration timeout = defaultTimeout();
        try {
            // Use central locators
            WaitUtils.clickWhenClickable(driver, AppLocators.CONSENT_BUTTON, timeout);
            VisualValidator.takeScreenshot(driver, "Home Page");
            WaitUtils.clickWhenClickable(driver, AppLocators.MY_ACCOUNT, timeout);
            VisualValidator.takeScreenshot(driver, "Sign In Prompt");
            WaitUtils.waitForVisible(driver, AppLocators.SIGN_IN, timeout).click();
            System.out.println("test");
            WaitUtils.clickWhenClickable(driver, AppLocators.CONSENT_BUTTON, timeout);
            WaitUtils.typeWhenInteractable(driver, AppLocators.EMAIL_INPUT, timeout, "yihop65927@wivstore.com", true);
            WaitUtils.clickWhenClickable(driver, AppLocators.CONTINUE_BUTTON, timeout);
            WaitUtils.typeWhenInteractable(driver, AppLocators.PIN_INPUT, timeout, "456654", true);
            WaitUtils.clickWhenClickable(driver, AppLocators.CONTINUE_BUTTON, timeout);
            WaitUtils.clickWhenClickable(driver, AppLocators.SEND_VERIFICATION_CODE_BUTTON, timeout);
            Assert.assertEquals(driver.getTitle(), "Choose Verification Type");
            log.info("Test Title is {}", driver.getTitle());
        } catch (Exception e) {
            log.error("Failure in SampleTest interactions: {}", e.getMessage(), e);
        }
    }
}