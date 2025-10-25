package org.navadiya.config;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Simple properties loader for test configuration. Reads from test resources application.properties
 * and environments.properties located on the classpath.
 */
@Slf4j
public class ApplicationConfig {
    private static final Properties PROPS = new Properties();
    private static final Properties ENVS = new Properties();

    static {
        try (InputStream in = ApplicationConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) PROPS.load(in);
        } catch (Exception ignored) {}
        try (InputStream in = ApplicationConfig.class.getClassLoader().getResourceAsStream("environments.properties")) {
            if (in != null) ENVS.load(in);
        } catch (Exception ignored) {}

        // Apply values from application.properties and environments.properties as default System properties
        try {
            applyPropertiesToSystem();
        } catch (Exception e) {
            log.warn("Failed to apply properties to System properties: {}", e.getMessage(), e);
        }
    }

    /**
     * Apply properties from application.properties and the active environment section of
     * environments.properties to System properties if they are not already set.
     *
     * This provides defaults that can be overridden by -D system properties or environment variables.
     */
    public static void applyPropertiesToSystem() {
        // apply application.properties entries
        for (String key : PROPS.stringPropertyNames()) {
            String current = System.getProperty(key);
            if (current == null) {
                String val = PROPS.getProperty(key);
                if (val != null && !val.isEmpty()) {
                    System.setProperty(key, val);
//                    log.info("System property set from application.properties: " + key + "=" + val);
                }
            } else {
                log.debug("System property already set, skipping: {} (value={})", key, current);
            }
        }

        // ensure there is an 'env' system property reflecting the chosen environment (convenience)
        if (System.getProperty("env") == null) {
            String envFromProps = PROPS.getProperty("app.env.default");
            if (envFromProps != null && !envFromProps.isEmpty()) {
                System.setProperty("env", envFromProps);
//                log.info("System property 'env' set to default from application.properties: {}", envFromProps);
            }
        }

        // apply environment-specific properties (like QA.app.url -> app.url)
        String activeEnv = System.getProperty("env", getEnv());
        for (String fullKey : ENVS.stringPropertyNames()) {
            if (fullKey.startsWith(activeEnv + ".")) {
                String key = fullKey.substring(activeEnv.length() + 1);
                if (System.getProperty(key) == null) {
                    String val = ENVS.getProperty(fullKey);
                    if (val != null && !val.isEmpty()) {
                        System.setProperty(key, val);
//                        log.info("System property set from environments.properties [{}]: {}={}", activeEnv, key, val);
                    }
                } else {
                    log.debug("System property already set, skipping env-mapped key: {}", key);
                }
            }
        }

        // --- Additional convenience mappings ---
        // Map commonly used short names (used by suiteRunner and examples) to the values from application.properties
        try {
            // env already handled above, but ensure short keys are present for consumers that expect them
            if (System.getProperty("browsers") == null) {
                String b = PROPS.getProperty("app.browsers");
                if (b != null && !b.isEmpty()) {
                    System.setProperty("browsers", b);
//                    log.info("Convenience System property set: browsers={}", b);
                }
            }

            if (System.getProperty("parallel.enabled") == null) {
                String p = PROPS.getProperty("app.parallel.enabled");
                if (p != null && !p.isEmpty()) {
                    System.setProperty("parallel.enabled", p);
//                    log.info("Convenience System property set: parallel.enabled={}", p);
                }
            }

            if (System.getProperty("parallel.threads") == null) {
                String t = PROPS.getProperty("app.parallel.threads");
                if (t != null && !t.isEmpty()) {
                    System.setProperty("parallel.threads", t);
//                    log.info("Convenience System property set: parallel.threads={}", t);
                }
            }

            if (System.getProperty("rerun.attempts") == null) {
                String r = PROPS.getProperty("app.rerun.attempts");
                if (r != null && !r.isEmpty()) {
                    System.setProperty("rerun.attempts", r);
//                    log.info("Convenience System property set: rerun.attempts={}", r);
                }
            }

            if (System.getProperty("headless") == null) {
                String h = PROPS.getProperty("app.headless");
                if (h != null && !h.isEmpty()) {
                    System.setProperty("headless", h);
//                    log.info("Convenience System property set: headless={}", h);
                }
            }
        } catch (Exception e) {
            log.debug("Error while applying convenience mappings: {}", e.getMessage(), e);
        }
    }

    public static String getProperty(String key) {
        String val = System.getProperty(key);
        if (val != null) return val;
        val = PROPS.getProperty(key);
        if (val != null) return val;
        return null;
    }

    public static String getEnv() {
        String e = System.getProperty("env");
        if (e != null) return e;
        return PROPS.getProperty("app.env.default", "QA");
    }

    public static String[] getBrowsers() {
        String b = System.getProperty("browsers");
        if (b == null || b.isEmpty()) b = PROPS.getProperty("app.browsers", "chrome");
        return Arrays.stream(b.split(",")).map(String::trim).toArray(String[]::new);
    }

    public static boolean isParallelEnabled() {
        String p = System.getProperty("parallel.enabled");
        if (p == null) p = System.getProperty("app.parallel.enabled");
        if (p == null) p = PROPS.getProperty("app.parallel.enabled", "false");
        return Boolean.parseBoolean(p);
    }

    public static int getThreadCount() {
        String t = System.getProperty("parallel.threads");
        if (t == null) t = System.getProperty("app.parallel.threads");
        if (t == null) t = PROPS.getProperty("app.parallel.threads", "1");
        try { return Integer.parseInt(t); } catch (Exception e) { return 1; }
    }

    public static int getRerunAttempts() {
        String r = System.getProperty("rerun.attempts");
        if (r == null) r = System.getProperty("app.rerun.attempts");
        if (r == null) r = PROPS.getProperty("app.rerun.attempts", "0");
        try { return Integer.parseInt(r); } catch (Exception e) { return 0; }
    }

    public static String getSuiteTestClass() {
        String c = System.getProperty("suite.test.class");
        if (c == null) c = PROPS.getProperty("suite.test.class", "org.navadiya.tests.SampleTest");
        return c;
    }

    public static boolean isHeadless() {
        String h = System.getProperty("headless");
        if (h == null) h = System.getProperty("app.headless");
        if (h == null) h = PROPS.getProperty("app.headless", "false");
        return Boolean.parseBoolean(h);
    }

    public static String getEnvProperty(String key) {
        String env = getEnv();
        String val = ENVS.getProperty(env + "." + key);
        if (val == null) val = ENVS.getProperty("QA." + key);
        return val;
    }
}
