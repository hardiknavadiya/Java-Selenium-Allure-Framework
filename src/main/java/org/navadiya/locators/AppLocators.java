package org.navadiya.locators;

import org.openqa.selenium.By;

/**
 * Central place for UI locators used across tests.
 *
 * Tip: Prefer robust, text/aria-based selectors over dynamic hashed class names.
 * Update a locator here once, and all tests will pick it up automatically.
 */
public final class AppLocators {
    private AppLocators() {}

    // --- Global / Common ---
    public static final By CONSENT_BUTTON = By.xpath("//button[normalize-space()='I consent' or normalize-space()='I Consent' or normalize-space()='Accept'] | //div[normalize-space()='I consent']");

    // --- Header / Account ---
    public static final By MY_ACCOUNT = By.xpath("//div[normalize-space()='My Account' or normalize-space()='Account' or @role='button' and .='My Account'] | //a[normalize-space()='My Account']");
    public static final By SIGN_IN = By.xpath("//div[normalize-space()='Sign In'] | //a[normalize-space()='Sign In'] | //button[normalize-space()='Sign In']");

    // --- Auth form ---
    public static final By EMAIL_INPUT = By.id("email");
    public static final By PIN_INPUT = By.id("pin");
    public static final By CONTINUE_BUTTON = By.cssSelector("button[data-test-id='button-continue']");
    public static final By SEND_VERIFICATION_CODE_BUTTON= By.xpath("//div[normalize-space()='Send verification code']");

    // Optional overlays
    public static final By CAPTCHA_OVERLAY_BY_ID = By.id("captchaModalOverlay");
    public static final By CAPTCHA_OVERLAY_BY_CLASS = By.cssSelector(".captcha-overlay");
}
