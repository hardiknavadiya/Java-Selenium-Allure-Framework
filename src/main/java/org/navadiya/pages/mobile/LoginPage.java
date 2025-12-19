package org.navadiya.pages.mobile;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Sample mobile login page demonstrating cross-platform element location.
 * Uses @AndroidFindBy and @iOSXCUITFindBy annotations for platform-specific locators.
 */
public class LoginPage extends BaseMobilePage {

    // Username field - different locators for Android and iOS
    @AndroidFindBy(id = "com.example.app:id/username")
    @iOSXCUITFindBy(accessibility = "usernameField")
    private WebElement usernameField;

    // Password field
    @AndroidFindBy(id = "com.example.app:id/password")
    @iOSXCUITFindBy(accessibility = "passwordField")
    private WebElement passwordField;

    // Login button
    @AndroidFindBy(id = "com.example.app:id/login_button")
    @iOSXCUITFindBy(accessibility = "loginButton")
    private WebElement loginButton;

    // Error message
    @AndroidFindBy(id = "com.example.app:id/error_message")
    @iOSXCUITFindBy(accessibility = "errorMessage")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enters username
     */
    public LoginPage enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
        log.info("Entered username: {}", username);
        return this;
    }

    /**
     * Enters password
     */
    public LoginPage enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordField));
        passwordField.clear();
        passwordField.sendKeys(password);
        log.info("Entered password");
        return this;
    }

    /**
     * Clicks login button
     */
    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
        log.info("Clicked login button");
        hideKeyboard(); // Hide keyboard after login attempt
    }

    /**
     * Gets error message text
     */
    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOf(errorMessage));
        String error = errorMessage.getText();
        log.info("Error message: {}", error);
        return error;
    }

    /**
     * Performs complete login action
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    /**
     * Checks if login button is displayed
     */
    public boolean isLoginButtonDisplayed() {
        try {
            return loginButton.isDisplayed();
        } catch (Exception e) {
            log.error("Login button not displayed", e);
            return false;
        }
    }
}

