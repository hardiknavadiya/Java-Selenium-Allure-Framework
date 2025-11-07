package org.navadiya.tests;

import org.navadiya.BaseTest;
import org.navadiya.util.WaitUtils;
import org.navadiya.locators.AppLocators;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.time.Duration;

public class AmazonSignIn extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(AmazonSignIn.class);

    @Test
    public void openExampleAndAssertTitle() {
        navigateToApp();
        WebDriver driver = getDriver();
        Duration timeout = defaultTimeout();
        try {
            // Use central locators
            try { WaitUtils.clickWhenClickable(driver, AppLocators.CONSENT_BUTTON, timeout); } catch (Exception ignored) {}
            WaitUtils.clickWhenClickable(driver, AppLocators.MY_ACCOUNT, timeout);
            WaitUtils.waitForVisible(driver, AppLocators.SIGN_IN, timeout).click();
            try { WaitUtils.clickWhenClickable(driver, AppLocators.CONSENT_BUTTON, timeout); } catch (Exception ignored) {}
            WaitUtils.typeWhenInteractable(driver, AppLocators.EMAIL_INPUT, timeout, "yihop65927@wivstore.com", true);
            WaitUtils.clickWhenClickable(driver, AppLocators.CONTINUE_BUTTON, timeout);
            WaitUtils.typeWhenInteractable(driver, AppLocators.PIN_INPUT, timeout, "456654", true);
            WaitUtils.clickWhenClickable(driver, AppLocators.CONTINUE_BUTTON, timeout);
            WaitUtils.clickWhenClickable(driver, AppLocators.SEND_VERIFICATION_CODE_BUTTON, timeout);
            log.info("Test Title is {}", driver.getTitle());
        } catch (Exception e) {
            log.error("Failure in SampleTest interactions: {}", e.getMessage(), e);
        }
    }
}