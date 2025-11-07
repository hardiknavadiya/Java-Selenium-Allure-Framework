package org.navadiya.config;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple properties loader for test configuration. Reads from test resources application.properties
 * and environments.properties located on the classpath.
 */
public class ApplicationConfig {
    private static final Properties PROPS = new Properties();
    private static final Properties ENVS = new Properties();
    private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);

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
            log.warn("Failed to apply properties to System properties", e);
        }
    }

    /**
     * Apply properties from application.properties and the active environment section of
     * environments.properties to System properties if they are not already set.
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


        // apply environment-specific properties (like QA.app.url -> app.url)
        String activeEnv = System.getProperty("app.env.default", getEnv());
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
    }

    public static String getProperty(String key) {
        String val = System.getProperty(key);
        if (val != null) return val;
        return PROPS.getProperty(key);
    }

    public static String getEnv() {
        String e = System.getProperty("app.env.default");
        if (e != null) return e;
        return PROPS.getProperty("app.env.default");
    }

    public static String[] getBrowsers() {
        String b = System.getProperty("app.browsers");
        if (b == null || b.isEmpty()) b = PROPS.getProperty("app.browsers");
        return Arrays.stream(b.split(",")).map(String::trim).toArray(String[]::new);
    }

    public static boolean isParallelEnabled() {
        String p = System.getProperty("app.parallel.enabled");
        if (p == null) p = System.getProperty("app.parallel.enabled");
        if (p == null) p = PROPS.getProperty("app.parallel.enabled", "false");
        return Boolean.parseBoolean(p);
    }

    public static int getThreadCount() {
        String t = System.getProperty("app.parallel.threads");
        if (t == null) t = System.getProperty("app.parallel.threads");
        if (t == null) t = PROPS.getProperty("app.parallel.threads", "1");
        try { return Integer.parseInt(t); } catch (Exception e) { return 1; }
    }

    public static int getRerunAttempts() {
        String r = System.getProperty("app.rerun.attempts");
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
        String h = System.getProperty("app.headless");
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

    // --- Chrome profile configuration helpers ---

    /** Whether to launch Chrome using a persistent local user profile. */
    public static boolean isChromeProfileEnabled() {
        String v = getProperty("chrome.profile.enabled");
        return Boolean.parseBoolean(v);
    }

    /** Absolute path to Chrome user data directory (e.g., C:\\Users\\<you>\\AppData\\Local\\Google\\Chrome\\User Data). */
    public static String getChromeUserDataDir() {
        String path = getProperty("chrome.profile.path");
        if (path != null && !path.isBlank()) return path.trim();
        // Try to resolve a sensible default based on OS
        String os = System.getProperty("os.name", "").toLowerCase();
        String userHome = System.getProperty("user.home");
        if (os.contains("win")) {
            String localAppData = System.getenv("LOCALAPPDATA");
            if (localAppData == null || localAppData.isEmpty()) localAppData = userHome + "\\AppData\\Local";
            return localAppData + "\\Google\\Chrome\\User Data";
        } else if (os.contains("mac")) {
            return userHome + "/Library/Application Support/Google/Chrome";
        } else {
            // Linux
            return userHome + "/.config/google-chrome";
        }
    }

    /** Chrome profile directory name inside the user data dir (e.g., Default, Profile 1). */
    public static String getChromeProfileDirectory() {
        String dir = getProperty("chrome.profile.directory");
        return dir;
    }

    /** Validate that the computed Chrome user data dir exists; return null if not. */
    public static String validateChromeUserDataDir(String path) {
        try {
            if (path == null) return null;
            File f = new File(path);
            if (f.exists() && f.isDirectory()) return f.getAbsolutePath();
            log.warn("Configured chrome.profile.path does not exist: {}", path);
        } catch (Exception e) {
            log.warn("Unable to validate chrome.profile.path: {}", path, e);
        }
        return null;
    }
}
