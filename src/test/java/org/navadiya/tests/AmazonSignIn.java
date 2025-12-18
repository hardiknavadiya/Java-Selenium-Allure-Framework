package org.navadiya.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.navadiya.pages.LoginPage;
import org.navadiya.util.ScreenshotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Login test using Page Object Model with AjaxElementLocatorFactory.
 */
public class amazonSignIn extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(amazonSignIn.class);

    @Test
    @Step("Amazon Sign-In Flow")
    @Description("Test Amazon sign-in process using POM with automatic element waiting")
    public void loginWithPageObjectModel() {
        navigateToApp();
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.clickConsent();
        ScreenshotUtils.takeScreenshot(getDriver(), "Home Page");

        loginPage.clickMyAccount();
        ScreenshotUtils.takeScreenshot(getDriver(), "Sign In Prompt");

        loginPage.clickSignIn();
        loginPage.clickConsent();

        loginPage.enterEmail("yihop65927@wivstore.com")
                .clickContinue()
                .enterPin("456654")
                .clickContinue()
                .clickSendVerificationCode();

        String actualTitle = loginPage.getPageTitle();
        Assert.assertEquals(actualTitle, "Choose Verification Type",
                "Expected page title to be 'Choose Verification Type'");
        log.info("✅ Test passed. Page title: {}", actualTitle);
    }
}

