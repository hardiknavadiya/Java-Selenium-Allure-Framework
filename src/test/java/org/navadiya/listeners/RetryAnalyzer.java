package org.navadiya.listeners;

import lombok.extern.slf4j.Slf4j;
import org.navadiya.config.ApplicationConfig;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retry analyzer that retries failed tests based on ApplicationConfig.getRerunAttempts().
 */
@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {
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

