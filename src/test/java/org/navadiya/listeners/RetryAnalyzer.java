package org.navadiya.listeners;

import org.navadiya.config.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer that retries failed tests based on ApplicationConfig.getRerunAttempts().
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger log = LoggerFactory.getLogger(RetryAnalyzer.class);
    private int count = 0;
    private final int maxAttempts;

    public RetryAnalyzer() {
        this.maxAttempts = ApplicationConfig.getRerunAttempts();
    }

    @Override
    public boolean retry(ITestResult result) {
        if (maxAttempts <= 0) return false;
        if (count < maxAttempts) {
            count++;
            log.info("Retrying test {} (attempt {}/{})", result.getName(), count, maxAttempts);
            return true;
        }
        return false;
    }
}
