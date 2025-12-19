package org.navadiya.util.mobile;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;

/**
 * Utility class for mobile gestures like swipe, scroll, tap, etc.
 * Uses W3C actions for cross-platform compatibility.
 */
public class MobileGestures {

    private static final Logger log = LoggerFactory.getLogger(MobileGestures.class);
    private final AppiumDriver driver;

    public MobileGestures(AppiumDriver driver) {
        this.driver = driver;
    }

    /**
     * Swipe from one point to another
     */
    public void swipe(Point start, Point end, Duration duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start.x, start.y));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), end.x, end.y));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
        log.info("Swiped from ({}, {}) to ({}, {})", start.x, start.y, end.x, end.y);
    }

    /**
     * Swipe up on the screen
     */
    public void swipeUp() {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        swipe(new Point(startX, startY), new Point(startX, endY), Duration.ofMillis(800));
        log.info("Swiped up");
    }

    /**
     * Swipe down on the screen
     */
    public void swipeDown() {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.2);
        int endY = (int) (size.height * 0.8);

        swipe(new Point(startX, startY), new Point(startX, endY), Duration.ofMillis(800));
        log.info("Swiped down");
    }

    /**
     * Swipe left on the screen
     */
    public void swipeLeft() {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.8);
        int endX = (int) (size.width * 0.2);
        int y = size.height / 2;

        swipe(new Point(startX, y), new Point(endX, y), Duration.ofMillis(800));
        log.info("Swiped left");
    }

    /**
     * Swipe right on the screen
     */
    public void swipeRight() {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.2);
        int endX = (int) (size.width * 0.8);
        int y = size.height / 2;

        swipe(new Point(startX, y), new Point(endX, y), Duration.ofMillis(800));
        log.info("Swiped right");
    }

    /**
     * Swipe on an element from left to right
     */
    public void swipeElementLeft(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();

        int startX = location.x + (int)(size.width * 0.8);
        int endX = location.x + (int)(size.width * 0.2);
        int y = location.y + size.height / 2;

        swipe(new Point(startX, y), new Point(endX, y), Duration.ofMillis(800));
        log.info("Swiped element left");
    }

    /**
     * Swipe on an element from right to left
     */
    public void swipeElementRight(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();

        int startX = location.x + (int)(size.width * 0.2);
        int endX = location.x + (int)(size.width * 0.8);
        int y = location.y + size.height / 2;

        swipe(new Point(startX, y), new Point(endX, y), Duration.ofMillis(800));
        log.info("Swiped element right");
    }

    /**
     * Tap on a specific point
     */
    public void tap(Point point) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), point.x, point.y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
        log.info("Tapped at ({}, {})", point.x, point.y);
    }

    /**
     * Long press on an element
     */
    public void longPress(WebElement element, Duration duration) {
        Point location = element.getLocation();
        Dimension size = element.getSize();

        int x = location.x + size.width / 2;
        int y = location.y + size.height / 2;

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence longPress = new Sequence(finger, 1);

        longPress.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        longPress.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPress.addAction(finger.createPointerMove(duration, PointerInput.Origin.viewport(), x, y));
        longPress.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(longPress));
        log.info("Long pressed element for {} ms", duration.toMillis());
    }

    /**
     * Scroll to an element until it's visible
     */
    public void scrollToElement(WebElement element, int maxSwipes) {
        int swipeCount = 0;
        while (!element.isDisplayed() && swipeCount < maxSwipes) {
            swipeUp();
            swipeCount++;
        }

        if (element.isDisplayed()) {
            log.info("Element found after {} swipes", swipeCount);
        } else {
            log.warn("Element not found after {} swipes", maxSwipes);
        }
    }

    /**
     * Zoom in using pinch gesture
     */
    public void zoomIn(Point center) {
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        int offset = 100;
        Point start1 = new Point(center.x - offset, center.y);
        Point end1 = new Point(center.x - offset * 2, center.y);
        Point start2 = new Point(center.x + offset, center.y);
        Point end2 = new Point(center.x + offset * 2, center.y);

        Sequence finger1Sequence = new Sequence(finger1, 1);
        finger1Sequence.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start1.x, start1.y));
        finger1Sequence.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        finger1Sequence.addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), end1.x, end1.y));
        finger1Sequence.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence finger2Sequence = new Sequence(finger2, 1);
        finger2Sequence.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start2.x, start2.y));
        finger2Sequence.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        finger2Sequence.addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), end2.x, end2.y));
        finger2Sequence.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(java.util.Arrays.asList(finger1Sequence, finger2Sequence));
        log.info("Zoomed in at ({}, {})", center.x, center.y);
    }

    /**
     * Zoom out using pinch gesture
     */
    public void zoomOut(Point center) {
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        int offset = 200;
        Point start1 = new Point(center.x - offset, center.y);
        Point end1 = new Point(center.x - offset / 2, center.y);
        Point start2 = new Point(center.x + offset, center.y);
        Point end2 = new Point(center.x + offset / 2, center.y);

        Sequence finger1Sequence = new Sequence(finger1, 1);
        finger1Sequence.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start1.x, start1.y));
        finger1Sequence.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        finger1Sequence.addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), end1.x, end1.y));
        finger1Sequence.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence finger2Sequence = new Sequence(finger2, 1);
        finger2Sequence.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), start2.x, start2.y));
        finger2Sequence.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        finger2Sequence.addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), end2.x, end2.y));
        finger2Sequence.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(java.util.Arrays.asList(finger1Sequence, finger2Sequence));
        log.info("Zoomed out at ({}, {})", center.x, center.y);
    }
}

