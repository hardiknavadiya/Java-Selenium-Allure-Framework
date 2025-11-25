package org.navadiya.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.navadiya.visual.VisualValidator;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Visual Regression Testing")
@Feature("Screenshot Comparison")
public class VisualValidationTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(VisualValidationTest.class);

    @BeforeClass
    public void setupVisualTesting() {
        // Initialize screenshot directories
        VisualValidator.initializeDirectories();
        log.info("Visual validation directories initialized");
    }

    @Test(priority = 1)
    @Story("Baseline Creation")
    @Description("Create baseline images for visual comparison")
    public void testCreateBaselines() {
        navigateToApp();
        WebDriver driver = getDriver();

        // Set consistent window size for reproducible screenshots
        driver.manage().window().setSize(new Dimension(1536, 864));

        try {
            Thread.sleep(2000); // Wait for page load
        } catch (InterruptedException e) {
            log.error("Sleep interrupted", e);
        }

        // Save baseline for home page
        String baselinePath = VisualValidator.saveBaseline(driver, "HomePage");
        log.info("Baseline created: {}", baselinePath);

        Assert.assertNotNull(baselinePath, "Failed to create baseline");
    }

    @Test(priority = 2)
    @Story("Visual Regression Detection")
    @Description("Validate current page against baseline using pixel comparison")
    public void testVisualRegressionPixelComparison() {
        navigateToApp();
        WebDriver driver = getDriver();

        // Set consistent window size
        driver.manage().window().setSize(new Dimension(1536, 864));

        try {
            Thread.sleep(2000); // Wait for page load
        } catch (InterruptedException e) {
            log.error("Sleep interrupted", e);
        }

        // Validate against baseline with 95% similarity threshold
        boolean passed = VisualValidator.validateAgainstBaseline(driver, "HomePage", 0.95);

        if (!passed) {
            log.error("Visual regression detected on Home Page!");
        }

        Assert.assertTrue(passed, "Visual regression detected! Check Allure report for diff image.");
    }

    @Test(priority = 3)
    @Story("Feature-based Comparison")
    @Description("Validate page layout using ORB feature matching")
    public void testVisualValidationWithORB() {
        navigateToApp();
        WebDriver driver = getDriver();

        // Set consistent window size
        driver.manage().window().setSize(new Dimension(1536, 864));

        try {
            Thread.sleep(2000); // Wait for page load
        } catch (InterruptedException e) {
            log.error("Sleep interrupted", e);
        }

        // Validate using ORB feature matching (more forgiving of minor changes)
        boolean passed = VisualValidator.validateAgainstBaselineWithORB(driver, "HomePage", 0.80);

        if (!passed) {
            log.error("Significant layout changes detected!");
        }

        Assert.assertTrue(passed, "Layout validation failed! Check Allure report for feature matches.");
    }
}

