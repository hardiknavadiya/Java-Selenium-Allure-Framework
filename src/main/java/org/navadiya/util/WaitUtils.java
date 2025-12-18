package org.navadiya.util;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;
import java.util.function.Function;

/**
 * Utility class providing explicit waits for page load, visibility, clickability and custom conditions.
 */
public final class WaitUtils {
    private WaitUtils() {}

    /**
     * Wait until document.readyState is 'complete'. Optionally attempts to wait for jQuery AJAX to finish if present.
     */
    @Step("Wait for page to fully load")
    public static void waitForPageLoad(WebDriver driver, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(d -> {
            Object state = ((JavascriptExecutor) d).executeScript("return document.readyState");
            return "complete".equals(state);
        });
        // Try jQuery active == 0 (ignore errors if jQuery not present)
        try {
            WebDriverWait ajaxWait = new WebDriverWait(driver, timeout);
            ajaxWait.until(d -> {
                Object active = ((JavascriptExecutor) d).executeScript("return window.jQuery ? jQuery.active : 0");
                return active instanceof Long && ((Long) active) == 0L;
            });
        } catch (Exception ignored) {}
    }

    @Step("Wait for element visible: {locator}")
    public static WebElement waitForVisible(WebDriver driver, By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Step("Wait for element visible: {locator}")
    public static WebElement waitForVisible(WebDriver driver, WebElement locator, Duration timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(locator));
    }

    @Step("Wait for element clickable: {locator}")
    public static WebElement waitForClickable(WebDriver driver, By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(locator));
    }

    @Step("Wait for element clickable: {locator}")
    public static WebElement waitForClickable(WebDriver driver, WebElement locator, Duration timeout) {
        return new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(locator));
    }

    @Step("Click when clickable: {locator}")
    public static void clickWhenClickable(WebDriver driver, By locator, Duration timeout) {
        waitForClickable(driver, locator, timeout).click();
    }

    /**
     * Generic wait for custom condition.
     */
    public static <T> T waitFor(WebDriver driver, Duration timeout, Function<WebDriver, T> condition) {
        return new WebDriverWait(driver, timeout).until(condition);
    }
    public static void waitForOverlayGone(WebDriver driver, Duration timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        By byId = By.id("captchaModalOverlay");
        By byClass = By.cssSelector(".captcha-overlay");
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(byId)); } catch (Exception ignored) {}
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(byClass)); } catch (Exception ignored) {}
    }

    @Step("Type into element when interactable (human-like optional): {locator}")
    public static void typeWhenInteractable(WebDriver driver, By locator, Duration timeout, CharSequence text) throws InterruptedException {
        typeWhenInteractable(driver, locator, timeout, text, false);
    }

    public static void typeWhenInteractable(WebDriver driver, By locator, Duration timeout, CharSequence text, boolean humanLike) throws InterruptedException {
        waitForOverlayGone(driver, timeout);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(d -> { try { return el.isEnabled(); } catch (StaleElementReferenceException e) { return false; } });
        try { ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el); } catch (Exception ignored) {}
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        try {
            el.clear();
            if (humanLike) {
                Thread.sleep(2000);
                humanType(el, text.toString(), 200, 400); // 25-90ms per key
            } else {
                el.sendKeys(text);
            }
        } catch (ElementNotInteractableException ex) {
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input',{bubbles:true}));",
                el, text.toString()
            );
        }
    }

    /** Human-like typing with per-character delay and small random jitter */
    public static void humanType(WebElement element, String value, long minDelayMs, long maxDelayMs) {
        Random r = new Random();
        for (char c : value.toCharArray()) {
            element.sendKeys(Character.toString(c));
            try { Thread.sleep(minDelayMs + r.nextInt((int) Math.max(1, maxDelayMs - minDelayMs))); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
        }
    }

    /**
     * Press TAB on the target element after ensuring it is visible and focused.
     */
    @Step("Press TAB on element: {locator}")
    public static void pressTab(WebDriver driver, By locator, Duration timeout) {
        waitForOverlayGone(driver, timeout);
        WebElement el = waitForVisible(driver, locator, timeout);
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        } catch (Exception ignored) {}
        try {
            // Try to focus by clicking
            el.click();
        } catch (Exception ignored) {}
        try {
            el.sendKeys(Keys.TAB);
        } catch (ElementNotInteractableException ex) {
            try {
                driver.switchTo().activeElement().sendKeys(Keys.TAB);
            } catch (Exception ignored2) {}
        }
    }
}
