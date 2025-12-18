package org.navadiya.visual;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.opencv.core.*;
import org.opencv.features2d.BFMatcher;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Visual validation utility using OpenCV for image comparison
 */
public class VisualValidator {

    private static final Logger log = LoggerFactory.getLogger(VisualValidator.class);
    private static final String BASELINE_DIR = "screenshots/baseline";
    private static final String ACTUAL_DIR = "screenshots/actual";
    private static final String DIFF_DIR = "screenshots/diff";
    private static boolean openCvInitialized;

    static {
        try {
            nu.pattern.OpenCV.loadLocally();
            openCvInitialized = true;
            log.info("OpenCV initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize OpenCV: {}", e.getMessage());
            openCvInitialized = false;
        }
    }


    /**
     * Take screenshot and save to file
     */
    public static String takeAndSaveScreenshot(WebDriver driver, String name) {
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = name.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";

            Path actualDir = Paths.get(ACTUAL_DIR);
            Files.createDirectories(actualDir);

            Path filePath = actualDir.resolve(fileName);
            Files.write(filePath, bytes);

            log.info("Screenshot saved: {}", filePath);

            // Also attach to Allure
            Allure.addAttachment(name, new ByteArrayInputStream(bytes));

            return filePath.toString();
        } catch (Exception e) {
            log.error("Failed to save screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Compare two images using pixel-by-pixel comparison
     *
     * @param baselineImagePath Path to baseline image
     * @param actualImagePath Path to actual image
     * @param threshold Similarity threshold (0.0 to 1.0, where 1.0 is identical)
     * @return true if images match within threshold
     */
    public static boolean compareImages(String baselineImagePath, String actualImagePath, double threshold) {
        if (!openCvInitialized) {
            log.error("OpenCV not initialized. Cannot perform image comparison.");
            return false;
        }

        try {
            Mat baseline = Imgcodecs.imread(baselineImagePath);
            Mat actual = Imgcodecs.imread(actualImagePath);

            if (baseline.empty() || actual.empty()) {
                log.error("Failed to load images for comparison");
                return false;
            }

            // Ensure images are same size
            if (baseline.size().width != actual.size().width || baseline.size().height != actual.size().height) {
                log.warn("Images have different dimensions. Resizing actual to match baseline.");
                Imgproc.resize(actual, actual, baseline.size());
            }

            // Calculate structural similarity
            double similarity = calculateSimilarity(baseline, actual);

            log.info("Image similarity: {}", similarity);

            // Generate diff image
            if (similarity < threshold) {
                generateDiffImage(baseline, actual, baselineImagePath);
            }

            return similarity >= threshold;
        } catch (Exception e) {
            log.error("Error comparing images: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Calculate similarity percentage between two images
     */
    private static double calculateSimilarity(Mat baseline, Mat actual) {
        Mat diff = new Mat();
        Core.absdiff(baseline, actual, diff);

        // Convert to grayscale
        Mat grayDiff = new Mat();
        Imgproc.cvtColor(diff, grayDiff, Imgproc.COLOR_BGR2GRAY);

        // Calculate mean difference
        Scalar meanDiff = Core.mean(grayDiff);
        double diffPercentage = meanDiff.val[0] / 255.0;

        return 1.0 - diffPercentage;
    }

    /**
     * Generate difference image highlighting changes
     */
    private static void generateDiffImage(Mat baseline, Mat actual, String baselineImagePath) {
        try {
            Mat diff = new Mat();
            Core.absdiff(baseline, actual, diff);

            // Threshold to highlight significant differences
            Mat grayDiff = new Mat();
            Imgproc.cvtColor(diff, grayDiff, Imgproc.COLOR_BGR2GRAY);
            Imgproc.threshold(grayDiff, grayDiff, 30, 255, Imgproc.THRESH_BINARY);

            // Create colored diff
            Mat coloredDiff = new Mat();
            Imgproc.cvtColor(grayDiff, coloredDiff, Imgproc.COLOR_GRAY2BGR);
            coloredDiff.setTo(new Scalar(0, 0, 255), grayDiff); // Red for differences

            // Blend with actual image
            Mat result = new Mat();
            Core.addWeighted(actual, 0.7, coloredDiff, 0.3, 0, result);

            // Save diff image
            Path diffDir = Paths.get(DIFF_DIR);
            Files.createDirectories(diffDir);

            String fileName = Paths.get(baselineImagePath).getFileName().toString();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String diffFileName = "diff_" + timestamp + "_" + fileName;
            Path diffPath = diffDir.resolve(diffFileName);

            Imgcodecs.imwrite(diffPath.toString(), result);
            log.info("Diff image saved: {}", diffPath);

            // Attach diff to Allure
            byte[] diffBytes = Files.readAllBytes(diffPath);
            Allure.addAttachment("Visual Diff", new ByteArrayInputStream(diffBytes));

        } catch (Exception e) {
            log.error("Error generating diff image: {}", e.getMessage());
        }
    }

    /**
     * Compare images using ORB feature matching (more robust to minor changes)
     *
     * @param baselineImagePath Path to baseline image
     * @param actualImagePath Path to actual image
     * @param matchThreshold Minimum good matches ratio (0.0 to 1.0)
     * @return true if images match based on feature matching
     */
    public static boolean compareImagesWithORB(String baselineImagePath, String actualImagePath, double matchThreshold) {
        if (!openCvInitialized) {
            log.error("OpenCV not initialized. Cannot perform ORB comparison.");
            return false;
        }

        try {
            Mat baseline = Imgcodecs.imread(baselineImagePath, Imgcodecs.IMREAD_GRAYSCALE);
            Mat actual = Imgcodecs.imread(actualImagePath, Imgcodecs.IMREAD_GRAYSCALE);

            if (baseline.empty() || actual.empty()) {
                log.error("Failed to load images for ORB comparison");
                return false;
            }

            // Initialize ORB detector
            ORB orb = ORB.create(1000);

            // Detect keypoints and compute descriptors
            MatOfKeyPoint keypointsBaseline = new MatOfKeyPoint();
            MatOfKeyPoint keypointsActual = new MatOfKeyPoint();
            Mat descriptorsBaseline = new Mat();
            Mat descriptorsActual = new Mat();

            orb.detectAndCompute(baseline, new Mat(), keypointsBaseline, descriptorsBaseline);
            orb.detectAndCompute(actual, new Mat(), keypointsActual, descriptorsActual);

            if (descriptorsBaseline.empty() || descriptorsActual.empty()) {
                log.warn("No features detected in one or both images");
                return false;
            }

            // Match descriptors
            BFMatcher matcher = BFMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING, true);
            MatOfDMatch matches = new MatOfDMatch();
            matcher.match(descriptorsBaseline, descriptorsActual, matches);

            List<DMatch> matchList = matches.toList();

            // Filter good matches
            List<DMatch> goodMatches = new ArrayList<>();
            double maxDist = 0;
            double minDist = 100;

            for (DMatch match : matchList) {
                double dist = match.distance;
                if (dist < minDist) minDist = dist;
                if (dist > maxDist) maxDist = dist;
            }

            for (DMatch match : matchList) {
                if (match.distance <= Math.max(2 * minDist, 30.0)) {
                    goodMatches.add(match);
                }
            }

            double matchRatio = (double) goodMatches.size() / Math.min(keypointsBaseline.toList().size(),
                                                                        keypointsActual.toList().size());

            log.info("ORB Match ratio: {} (good matches: {}, total keypoints: {})",
                    matchRatio, goodMatches.size(), keypointsBaseline.toList().size());

            // Draw matches for visualization
            if (matchRatio < matchThreshold) {
                drawMatches(baseline, keypointsBaseline, actual, keypointsActual, goodMatches, baselineImagePath);
            }

            return matchRatio >= matchThreshold;

        } catch (Exception e) {
            log.error("Error in ORB comparison: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Draw and save feature matches
     */
    private static void drawMatches(Mat baseline, MatOfKeyPoint kpBaseline, Mat actual,
                                     MatOfKeyPoint kpActual, List<DMatch> matches, String baselineImagePath) {
        try {
            Mat matchesImg = new Mat();
            MatOfDMatch goodMatchesMat = new MatOfDMatch();
            goodMatchesMat.fromList(matches);

            Features2d.drawMatches(baseline, kpBaseline, actual, kpActual, goodMatchesMat, matchesImg);

            // Save matches image
            Path diffDir = Paths.get(DIFF_DIR);
            Files.createDirectories(diffDir);

            String fileName = Paths.get(baselineImagePath).getFileName().toString();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String matchesFileName = "matches_" + timestamp + "_" + fileName;
            Path matchesPath = diffDir.resolve(matchesFileName);

            Imgcodecs.imwrite(matchesPath.toString(), matchesImg);
            log.info("Feature matches saved: {}", matchesPath);

            // Attach to Allure
            byte[] matchesBytes = Files.readAllBytes(matchesPath);
            Allure.addAttachment("Feature Matches", new ByteArrayInputStream(matchesBytes));

        } catch (Exception e) {
            log.error("Error drawing matches: {}", e.getMessage());
        }
    }

    /**
     * Save baseline image for future comparisons
     */
    public static String saveBaseline(WebDriver driver, String name) {
        try {
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            String fileName = name.replaceAll("[^a-zA-Z0-9]", "_") + ".png";

            Path baselineDir = Paths.get(BASELINE_DIR);
            Files.createDirectories(baselineDir);

            Path filePath = baselineDir.resolve(fileName);
            Files.write(filePath, bytes);

            log.info("Baseline saved: {}", filePath);
            return filePath.toString();
        } catch (Exception e) {
            log.error("Failed to save baseline: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Compare current screen with baseline
     *
     * @param driver WebDriver instance
     * @param baselineName Name of the baseline image
     * @param threshold Similarity threshold (0.0 to 1.0)
     * @return true if current screen matches baseline
     */
    public static boolean validateAgainstBaseline(WebDriver driver, String baselineName, double threshold) {
        String actualPath = takeAndSaveScreenshot(driver, baselineName);
        if (actualPath == null) {
            return false;
        }

        String baselinePath = Paths.get(BASELINE_DIR, baselineName.replaceAll("[^a-zA-Z0-9]", "_") + ".png").toString();

        File baselineFile = new File(baselinePath);
        if (!baselineFile.exists()) {
            log.warn("Baseline not found: {}. Creating new baseline.", baselinePath);
            saveBaseline(driver, baselineName);
            return true; // First run, assume pass
        }

        return compareImages(baselinePath, actualPath, threshold);
    }

    /**
     * Compare current screen with baseline using ORB feature matching
     */
    public static boolean validateAgainstBaselineWithORB(WebDriver driver, String baselineName, double matchThreshold) {
        String actualPath = takeAndSaveScreenshot(driver, baselineName);
        if (actualPath == null) {
            return false;
        }

        String baselinePath = Paths.get(BASELINE_DIR, baselineName.replaceAll("[^a-zA-Z0-9]", "_") + ".png").toString();

        File baselineFile = new File(baselinePath);
        if (!baselineFile.exists()) {
            log.warn("Baseline not found: {}. Creating new baseline.", baselinePath);
            saveBaseline(driver, baselineName);
            return true; // First run, assume pass
        }

        return compareImagesWithORB(baselinePath, actualPath, matchThreshold);
    }

    /**
     * Initialize screenshot directories
     */
    public static void initializeDirectories() {
        try {
            Files.createDirectories(Paths.get(BASELINE_DIR));
            Files.createDirectories(Paths.get(ACTUAL_DIR));
            Files.createDirectories(Paths.get(DIFF_DIR));
            log.info("Screenshot directories initialized");
        } catch (IOException e) {
            log.error("Failed to create screenshot directories: {}", e.getMessage());
        }
    }
}
