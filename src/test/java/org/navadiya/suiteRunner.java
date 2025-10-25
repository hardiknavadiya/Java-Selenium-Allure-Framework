package org.navadiya;

import lombok.extern.slf4j.Slf4j;
import org.navadiya.config.ApplicationConfig;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.util.jar.JarFile;
import java.nio.file.Files;
import java.util.*;

/**
 * Programmatic TestNG runner using ApplicationConfig for defaults.
 * System properties override application.properties values.
 *
 * Usage examples:
 *  mvn -Denv=DEV -Dbrowsers=chrome,firefox -Dparallel.threads=10 -Drerun.attempts=2 test
 */
@Slf4j
public class suiteRunner {

    public static void main(String[] args) {
        String env = ApplicationConfig.getEnv();
        String[] browsers = ApplicationConfig.getBrowsers();
        boolean parallel = ApplicationConfig.isParallelEnabled();
        int threads = ApplicationConfig.getThreadCount();
        int rerunAttempts = ApplicationConfig.getRerunAttempts();
        String testClass = ApplicationConfig.getSuiteTestClass();

        log.info("Env={}, browsers={}, parallel={}, threads={}, rerunAttempts={}, testClass={}",
                env, Arrays.toString(browsers), parallel, threads, rerunAttempts, testClass);

        // build suites
        List<XmlSuite> suites = new ArrayList<>();
        for (String b : browsers) {
            String browser = b.trim();
            XmlSuite s = new XmlSuite();
            s.setName(browser + "-suite");
            s.getParameters().put("browser", browser);
            s.getParameters().put("env", env);
            s.setParallel(parallel ? XmlSuite.ParallelMode.METHODS : XmlSuite.ParallelMode.NONE);
            if (parallel) s.setThreadCount(threads);

            XmlTest t = new XmlTest(s);
            t.setName(browser + "-tests");
            // ensure the test has the browser and env parameters available at test level
            Map<String, String> params = new HashMap<>();
            params.put("browser", browser);
            params.put("env", env);
            t.setParameters(params);
            // Resolve one or more test class names (comma-separated) to Class objects so TestNG receives proper class references.
            String[] testClasses;
            if (testClass != null && testClass.trim().endsWith(".*")) {
                String pkg = testClass.trim().substring(0, testClass.trim().length() - 2);
                List<String> discovered = discoverTestClassesInPackage(pkg);
                testClasses = discovered.toArray(new String[0]);
            } else {
                testClasses = testClass.split(",");
            }
             for (String tc : testClasses) {
                 tc = tc.trim();
                 if (tc.isEmpty()) continue;
                 try {
                     Class<?> clazz;
                     try {
                         clazz = Class.forName(tc);
                     } catch (ClassNotFoundException e) {
                         // Try context classloader as a fallback
                         clazz = Thread.currentThread().getContextClassLoader().loadClass(tc);
                     }
                     t.getClasses().add(new XmlClass(clazz));
                 } catch (ClassNotFoundException e) {
                     String cp = System.getProperty("java.class.path");
                     log.error("Cannot find test class '{}' on classpath. java.class.path={}", tc, cp);
                     throw new RuntimeException("Test class not found on classpath: " + tc + ". Ensure test classes are available when running the runner.", e);
                 }
             }
             suites.add(s);
        }

        // first run
        // Run each browser's suite separately so we get a per-browser testng-failed.xml
        for (XmlSuite s : suites) {
            TestNG single = new TestNG();
            single.setXmlSuites(Collections.singletonList(s));
            // Allure TestNG listener is usually auto-registered via ServiceLoader (allure-testng). Avoid adding it explicitly to prevent duplicate listener warnings.
            if (rerunAttempts > 0) {
               single.addListener(new org.navadiya.listeners.RetryAnnotationTransformer());
                log.info("Retry listeners registered (rerunAttempts={}) for suite: {}", rerunAttempts, s.getName());
            }
            single.run();
        }
        log.info("Execution finished");
    }

    // Discover test classes in the given package by scanning classpath directories and jars.
    private static List<String> discoverTestClassesInPackage(String pkg) {
        List<String> result = new ArrayList<>();
        String pkgPath = pkg.replace('.', '/');
        String classpath = System.getProperty("java.class.path", "");
        String[] entries = classpath.split(File.pathSeparator);
        for (String entry : entries) {
            try {
                File f = new File(entry);
                if (f.isDirectory()) {
                    File pkgDir = new File(f, pkg.replace('.', File.separatorChar));
                    if (pkgDir.exists() && pkgDir.isDirectory()) {
                        Files.walk(pkgDir.toPath())
                                .filter(p -> p.toString().endsWith(".class"))
                                .forEach(p -> {
                                    String rel = pkgDir.toPath().relativize(p).toString();
                                    String className = rel.replace(File.separatorChar, '.').replaceAll("\\.class$", "");
                                    if (!className.contains("$")) {
                                        String fqcn = pkg + "." + className;
                                        if (isTestClass(fqcn)) result.add(fqcn);
                                    }
                                });
                    }
                } else if (f.isFile() && f.getName().endsWith(".jar")) {
                    try (JarFile jf = new JarFile(f)) {
                        Enumeration<java.util.jar.JarEntry> en = jf.entries();
                        while (en.hasMoreElements()) {
                            java.util.jar.JarEntry je = en.nextElement();
                            String name = je.getName();
                            if (name.startsWith(pkgPath) && name.endsWith(".class") && !name.contains("$")) {
                                String fqcn = name.replace('/', '.').replaceAll("\\.class$", "");
                                if (isTestClass(fqcn)) result.add(fqcn);
                            }
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
        // remove duplicates and sort
        return result.stream().distinct().sorted().toList();
    }

    private static boolean isTestClass(String fqcn) {
        try {
            Class<?> c = Class.forName(fqcn);
            // exclude abstract classes and interfaces
            if (c.isInterface() || java.lang.reflect.Modifier.isAbstract(c.getModifiers())) return false;
            // include if any method has TestNG @Test
            for (java.lang.reflect.Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(org.testng.annotations.Test.class)) return true;
            }
        } catch (Throwable t) {
            // ignore load failures
        }
        return false;
    }
}
