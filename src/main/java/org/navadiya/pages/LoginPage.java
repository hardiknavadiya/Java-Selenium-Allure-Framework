package org.navadiya.pages;

import io.qameta.allure.Step;
import org.navadiya.util.WaitUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;


/**
 * Example Login Page using Page Object Model with AjaxElementLocatorFactory.
 * Elements are automatically waited for using the AJAX locator factory.
 */
public class LoginPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    // Elements will be automatically waited for by AjaxElementLocatorFactory
    @FindBy(xpath = "//button[normalize-space()='I consent' or normalize-space()='I Consent' or normalize-space()='Accept'] | //div[normalize-space()='I consent']")
    private WebElement consentButton;

    @FindBy(xpath = "//div[normalize-space()='My Account' or normalize-space()='Account' or @role='button' and .='My Account'] | //a[normalize-space()='My Account']")
    private WebElement myAccountButton;

    @FindBy(xpath = "//div[normalize-space()='Sign In'] | //a[normalize-space()='Sign In'] | //button[normalize-space()='Sign In']")
    private WebElement signInButton;

    @FindBy(id = "login")
    private WebElement emailInput;

    @FindBy(id = "pin")
    private WebElement pinInput;

    @FindBy(css = "button[data-test-id='button-continue']")
    private WebElement continueButton;

    @FindBy(xpath = "//div[normalize-space()='Send verification code']")
    private WebElement sendVerificationCodeButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Safe click with JavaScript fallback
     */
    private void safeClick(WebElement element) {
        WaitUtils.waitForClickable(driver, element, Duration.ofSeconds(15));
        try {
            element.click();
        } catch (Exception e) {
            log.warn("Standard click failed, using JavaScript click: {}", e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Safe send keys with JavaScript fallback
     */
    private void safeSendKeys(WebElement element, String text, Boolean humanLike) {
        WaitUtils.waitForClickable(driver, element, Duration.ofSeconds(15));
        try {
            element.clear();
            Thread.sleep(500);
            if (humanLike) {
                WaitUtils.humanType(element, text.toString(), 50, 100); // 25-90ms per key
            } else {
                element.sendKeys(text);
            }
        } catch (Exception e) {
            log.warn("Standard sendKeys failed, using JavaScript: {}", e.getMessage());
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input', {bubbles: true}));",
                element, text
            );
        }
    }

    @Step("Click consent button")
    public LoginPage clickConsent() {
        log.info("Clicking consent button");
        safeClick(consentButton);
        return this;
    }

    @Step("Click My Account button")
    public LoginPage clickMyAccount() {
        log.info("Clicking My Account button");
        safeClick(myAccountButton);
        return this;
    }

    @Step("Click Sign In button")
    public LoginPage clickSignIn() {
        log.info("Clicking Sign In button");
        safeClick(signInButton);
        return this;
    }

    @Step("Enter email: {email}")
    public LoginPage enterEmail(String email) {
        log.info("Entering email: {}", email);
        safeSendKeys(emailInput, email, true);
        return this;
    }

    @Step("Enter PIN")
    public LoginPage enterPin(String pin) {
        log.info("Entering PIN");
        safeSendKeys(pinInput, pin, true);
        return this;
    }

    @Step("Click Continue button")
    public LoginPage clickContinue() {
        log.info("Clicking Continue button");
        safeClick(continueButton);
        return this;
    }

    @Step("Click Send Verification Code button")
    public LoginPage clickSendVerificationCode() {
        log.info("Clicking Send Verification Code button");
        safeClick(sendVerificationCodeButton);
        return this;
    }

    @Step("Get page title")
    public String getPageTitle() {
        String title = driver.getTitle();
        log.info("Current page title: {}", title);
        return title;
    }
}

