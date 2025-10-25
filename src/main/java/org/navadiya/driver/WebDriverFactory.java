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
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.Locale;

public class WebDriverFactory {

    public static WebDriver createInstance(String browser) throws Exception {
        String gridUrl = ApplicationConfig.getProperty("selenium.grid.url");
        boolean useGrid = gridUrl != null && !gridUrl.trim().isEmpty() && !gridUrl.contains("localhost:0");
        String b = browser.toLowerCase(Locale.ROOT);

        if (useGrid) {
            MutableCapabilities caps = createCapabilities(b);
            return new RemoteWebDriver(new URL(gridUrl), caps);
        }

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
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions opts = new ChromeOptions();
                if (ApplicationConfig.isHeadless()) opts.addArguments("--headless=new");
//                opts.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                opts.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                opts.addArguments("window-size=1900,1080");
                ChromeDriver c = new ChromeDriver(opts);
                c.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
                return c;
        }
    }

    private static MutableCapabilities createCapabilities(String browser) {
        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability("browserName", browser);
        // cloud provider capabilities can be added by ApplicationConfig when required
        return caps;
    }
}

