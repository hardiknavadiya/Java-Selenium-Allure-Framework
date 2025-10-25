package org.navadiya.listeners;

import lombok.extern.slf4j.Slf4j;
import org.navadiya.config.ApplicationConfig;
import org.testng.IAnnotationTransformer;
import org.testng.ITestNGListener;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Annotation transformer that sets the retry analyzer on test methods when rerun attempts > 0.
 *
 * Implements ITestNGListener so it can be registered via TestNG listener APIs.
 */
@Slf4j
public class RetryAnnotationTransformer implements IAnnotationTransformer, ITestNGListener {
    private final int maxAttempts;

    public RetryAnnotationTransformer() {
        this.maxAttempts = ApplicationConfig.getRerunAttempts();
        log.info("RetryAnnotationTransformer initialized with maxAttempts={}", maxAttempts);
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (maxAttempts <= 0) return;
        // Set the retry analyzer (always set to ensure config-driven retries)
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
        log.debug("Set RetryAnalyzer for {}#{}", testClass != null ? testClass.getName() : "<unknown>", testMethod != null ? testMethod.getName() : "<unknown>");
    }
}
