package org.navadiya.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.navadiya.config.ApplicationConfig;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Duration;
import java.util.Locale;

public class WebDriverFactory {

    public static final Logger log = LoggerFactory.getLogger(WebDriverFactory.class);

    public static WebDriver createInstance(String browser) throws Exception {
        // decide grid usage from explicit flag in application.properties: selenium.grid.enabled=true/false
        String gridEnabled = ApplicationConfig.getProperty("selenium.grid.enabled");
        boolean useGrid = Boolean.parseBoolean(gridEnabled == null ? "false" : gridEnabled);
        String gridUrl = ApplicationConfig.getProperty("selenium.grid.url");
        if (useGrid && (gridUrl == null || gridUrl.trim().isEmpty())) {
            throw new IllegalArgumentException("selenium.grid.enabled is true but selenium.grid.url is not configured");
        }

        String b = (browser == null) ? "chrome" : browser.toLowerCase(Locale.ROOT);

        if (useGrid) {
            MutableCapabilities caps = createCapabilities(b);
            // avoid deprecated URL(String) constructor
            return new RemoteWebDriver(URI.create(gridUrl).toURL(), caps);
        }else {

            switch (b) {
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions fopts = new FirefoxOptions();
                    if (ApplicationConfig.isHeadless()) fopts.addArguments("-headless=new");
                    fopts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    FirefoxDriver f = new FirefoxDriver(fopts);
                    f.manage().window().maximize();
                    f.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                    return f;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions eopts = new EdgeOptions();
                    if (ApplicationConfig.isHeadless()) eopts.addArguments("--headless=new");
                    eopts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    EdgeDriver e = new EdgeDriver(eopts);
                    e.manage().window().maximize();
                    e.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                    return e;
                case "chrome":
                default:
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions opts = new ChromeOptions();
                    if (ApplicationConfig.isHeadless()) opts.addArguments("--headless=new");
                    opts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    opts.addArguments("window-size=1900,1080");
                    try {
                        // Add Chrome profile if enabled
                        if (ApplicationConfig.isChromeProfileEnabled()) {
                            String userDataDir = ApplicationConfig.getChromeUserDataDir();
                            String validatedPath = ApplicationConfig.validateChromeUserDataDir(userDataDir);

                            if (validatedPath != null) {
                                opts.addArguments("--user-data-dir=" + validatedPath);

                                String profileDir = ApplicationConfig.getChromeProfileDirectory();
                                if (profileDir != null && !profileDir.isBlank()) {
                                    opts.addArguments("--profile-directory=" + profileDir.trim());
                                }
                            }
                        }

                        ChromeDriver c = new ChromeDriver(opts);
                        c.manage().window().maximize();
                        c.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                        return c;
                    } catch (Exception ex) {
                        log.error("Please close all chrome browser instances before running tests with Chrome user profile.", ex);
                        throw new RuntimeException(ex);
                    }
            }
        }
    }

    private static MutableCapabilities createCapabilities(String browser) {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", browser);
        // cloud provider capabilities can be added by ApplicationConfig when required
        return caps;
    }
}
