# 🚀 Java Selenium TestNG Allure Framework

[![Java](https://img.shields.io/badge/Java-22-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.36.0-green.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.8.0-red.svg)](https://testng.org/)
[![Allure](https://img.shields.io/badge/Allure-2.20.1-yellow.svg)](https://docs.qameta.io/allure/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg)](https://maven.apache.org/)
[![OpenCV](https://img.shields.io/badge/OpenCV-4.9.0-blue.svg)](https://opencv.org/)

A **production-ready** Selenium test automation framework with advanced capabilities including **OpenCV visual validation**, **Healenium self-healing**, parallel execution, automatic retry, and comprehensive CI/CD integration. Built for enterprise-level test automation.

---

## 📋 Table of Contents

- [Features](#-features)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Configuration](#-configuration)
- [Running Tests](#-running-tests)
- [Visual Validation](#-visual-validation-opencv)
- [Self-Healing Tests](#-self-healing-tests-healenium)
- [Selenium Grid Setup](#-selenium-grid-setup)
- [Docker Setup](#-docker-setup)
- [Jenkins Integration](#-jenkins-integration)
- [Allure Reporting](#-allure-reporting)
- [Advanced Features](#-advanced-features)
- [Troubleshooting](#-troubleshooting)

---

## ✨ Features

### 🎯 Core Test Automation
- ✅ **Multi-Browser Support** - Chrome, Firefox, Edge with automatic driver management (WebDriverManager)
- ✅ **Mobile Testing Support** - Android and iOS app testing with Appium
- ✅ **Parallel Execution** - Cross-browser parallel testing with configurable thread pools
- ✅ **Automatic Retry** - Smart retry mechanism for flaky tests with configurable attempts
- ✅ **TestNG Framework** - Industry-standard test execution and reporting
- ✅ **Thread-Safe Execution** - ThreadLocal WebDriver management for safe parallel testing
- ✅ **Headless Mode** - Support for CI/CD headless browser execution
- ✅ **Environment Management** - Multi-environment configuration (DEV, QA, PROD)

### 🔍 Visual Testing & Self-Healing
- ✅ **OpenCV Visual Validation** - Advanced image comparison with pixel-by-pixel and feature matching
- ✅ **ORB Feature Matching** - Robust visual comparison tolerant to minor changes
- ✅ **Automatic Baseline Management** - Smart baseline creation and comparison
- ✅ **Diff Image Generation** - Visual difference highlighting in red
- ✅ **Healenium Integration** - Self-healing locators that auto-recover from DOM changes
- ✅ **Smart Element Recovery** - AI-powered locator healing when elements change

### 📊 Reporting & Monitoring
- ✅ **Allure Reports** - Beautiful HTML reports with screenshots, videos, and trends
- ✅ **History Trends** - Track test execution history across builds
- ✅ **Screenshot Attachments** - Automatic screenshot capture on failures
- ✅ **Visual Diff Reports** - Compare baseline vs actual images in reports
- ✅ **Comprehensive Logging** - SLF4J + Logback structured logging
- ✅ **Real-time Reporting** - View results as tests execute

### 🚀 CI/CD & DevOps
- ✅ **Jenkins Pipeline** - Complete Jenkinsfile with parameterized builds
- ✅ **Docker Integration** - Selenium Grid via Docker Compose
- ✅ **Selenium Grid Support** - Local grid, Docker, and cloud providers (Sauce Labs, BrowserStack)
- ✅ **Email Notifications** - Automatic test result emails
- ✅ **Build Artifacts** - Persistent Allure reports and screenshots

### 🛠️ Developer Experience
- ✅ **Lombok Support** - Cleaner code with @Slf4j, @Data annotations
- ✅ **Fluent Assertions** - Readable test assertions
- ✅ **Page Object Model** - Organized test architecture
- ✅ **Utility Classes** - WaitUtils, VisualValidator, DriverManager
- ✅ **Property-driven Config** - Externalized configuration management
- ✅ **Maven Build** - Standard Maven lifecycle support

---

## 📁 Project Structure

```
Java-Selenium-Allure-Framework/
├── src/
│   ├── main/java/org/navadiya/
│   │   ├── config/                    # Configuration management
│   │   │   ├── ApplicationConfig.java # Property-based configuration
│   │   │   └── EnvironmentConfig.java # Multi-environment support
│   │   ├── driver/                    # WebDriver management
│   │   │   └── DriverManager.java     # ThreadLocal driver factory
│   │   ├── locators/                  # Centralized locators
│   │   │   └── AppLocators.java       # Page element locators
│   │   ├── util/                      # Utility classes
│   │   │   └── WaitUtils.java         # Selenium wait helpers
│   │   └── visual/                    # Visual validation
│   │       └── VisualValidator.java   # OpenCV image comparison
│   └── test/
│       ├── java/org/navadiya/
│       │   ├── BaseTest.java          # Base test class with setup/teardown
│       │   ├── SuiteRunner.java       # Dynamic TestNG suite generator (optional)
│       │   ├── listeners/             # TestNG listeners
│       │   │   ├── RetryAnalyzer.java           # Retry logic
│       │   │   ├── RetryAnnotationTransformer.java
│       │   │   └── TestListener.java            # Test event handling
│       │   └── tests/                 # Test classes
│       │       ├── amazonSignIn.java
│       │       └── VisualValidationTest.java   # Visual regression tests
│       └── resources/
│           ├── application.properties      # Framework configuration
│           ├── environments.properties     # Environment URLs
│           └── allure.properties          # Allure settings
├── screenshots/                       # Visual validation images
│   ├── baseline/                     # Reference images
│   ├── actual/                       # Current screenshots
│   └── diff/                         # Difference images
├── allure-history/                   # Persistent test history
├── db/sql/                           # Healenium database (optional)
├── pom.xml                           # Maven dependencies
├── Jenkinsfile                       # CI/CD pipeline
├── docker-compose.yml                # Selenium Grid setup
├── README.md                         # This file
└── VISUAL_VALIDATION_GUIDE.md       # Visual testing guide
```

---

## 🔧 Prerequisites

### Required Software
- **Java JDK 22** or higher ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.x** ([Download](https://maven.apache.org/download.cgi))
- **Git** ([Download](https://git-scm.com/downloads))

### Optional (for advanced features)
- **Docker** & **Docker Compose** - For Selenium Grid ([Download](https://www.docker.com/))
- **Jenkins** - For CI/CD integration ([Download](https://www.jenkins.io/))
- **Allure CLI** - For local report generation ([Install Guide](https://docs.qameta.io/allure/#_installing_a_commandline))

### Browsers
The framework automatically downloads and manages browser drivers via **WebDriverManager**. Supported browsers:
- Google Chrome
- Mozilla Firefox  
- Microsoft Edge

---

## 🚀 Quick Start

### 1. Clone Repository
```bash
git clone https://github.com/hardiknavadiya/Java-Selenium-Allure-Framework.git
cd Java-Selenium-Allure-Framework
```

### 2. Install Dependencies
```bash
mvn clean install -DskipTests
```

### 3. Run Tests
```bash
# Basic execution
mvn clean test

# With custom browser
mvn test -Dapp.browsers=chrome

# Headless mode
mvn test -Dapp.headless=true
```

### 4. View Allure Report
```bash
mvn allure:serve
```

---

## 🔧 Prerequisites

- **Java Development Kit (JDK)** - Version 22 or higher
- **Apache Maven** - Version 3.6 or higher
- **Browsers** - Chrome, Firefox, and/or Edge installed
- **Git** - For version control
- **Docker** (Optional) - For Selenium Grid containerized setup
- **Jenkins** (Optional) - For CI/CD automation
- **Allure Command Line** (Optional) - For local report generation

---

## 📦 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/hardiknavadiya/Java-Selenium-Allure-Framework.git
cd Java-Selenium-Allure-Framework
```

### 2. Install Dependencies
```bash
mvn clean install -DskipTests
```

### 3. Verify Installation
```bash
mvn clean compile test-compile
```

---

## ⚙️ Configuration

### Application Configuration
Edit `src/test/resources/application.properties`:

```properties
# Environment Configuration
app.env.default=PROD                    # Default environment: DEV, QA, PROD

# Browser Configuration
app.browsers=chrome,firefox,edge        # Comma-separated browser list

# Execution Settings
app.headless=false                      # Run browsers in headless mode
app.parallel.enabled=true               # Enable parallel execution
app.parallel.threads=4                  # Number of parallel threads
app.timeout.seconds=15                  # Element wait timeout

# Retry Configuration
app.rerun.attempts=0                    # Number of retry attempts for failed tests

# Test Selection
suite.test.class=org.navadiya.tests.SampleTest  # Test class(es) to execute

# Selenium Grid
selenium.grid.enabled=false             # Enable Selenium Grid
selenium.grid.url=http://192.168.168.131:4444  # Grid hub URL
```

### Environment URLs
Edit `src/test/resources/environments.properties`:

```properties
QA.app.url=https://stage.vwo.com
PROD.app.url=https://app.vwo.com
DEV.app.url=https://dev.vwo.com
```

---

## 🚀 Running Tests

### Local Execution (IDE)

**VM Options:**
```
-Dapp.env.default=PROD 
-Dapp.browsers=chrome,firefox 
-Dapp.headless=false 
-Dapp.parallel.enabled=false 
-Dselenium.grid.enabled=false
```

Run your test classes (e.g., `amazonSignIn.java`) directly as TestNG tests from your IDE.

### Maven Command Line

#### Basic Execution
```bash
mvn clean test
```

#### With Custom Parameters
```bash
mvn test -Dapp.env.default=PROD -Dapp.browsers=chrome,firefox,edge -Dapp.headless=true
```

#### Single Browser Execution
```bash
mvn test -Dapp.env.default=QA -Dapp.browsers=chrome -Dapp.headless=false
```

#### Parallel Execution
```bash
mvn test -Dapp.env.default=PROD -Dapp.browsers=chrome,firefox,edge -Dapp.parallel.enabled=true -Dapp.parallel.threads=3
```

#### With Selenium Grid
```bash
mvn test -Dapp.env.default=PROD -Dapp.browsers=chrome,firefox -Dselenium.grid.enabled=true -Dselenium.grid.url=http://192.168.168.131:4444
```

#### Headless Mode (CI/CD)
```bash
mvn test -Dapp.env.default=PROD -Dapp.browsers=chrome,firefox -Dapp.headless=true -Dapp.parallel.enabled=true
```

#### With Retry Mechanism
```bash
mvn test -Dapp.env.default=PROD -Dapp.browsers=chrome -Dapp.rerun.attempts=2
```

#### Execute Specific Test Package
```bash
mvn test -Dsuite.test.class=org.navadiya.tests.*
```

---

## 🔍 Visual Validation (OpenCV)

### Overview
The framework includes advanced **OpenCV-based visual validation** for automated UI regression testing. Compare screenshots using pixel-by-pixel comparison or ORB feature matching.

### Key Features
- ✅ **Pixel-Perfect Comparison** - Detect exact visual changes
- ✅ **ORB Feature Matching** - Robust to lighting and minor shifts
- ✅ **Automatic Baseline Management** - First run creates baseline, subsequent runs compare
- ✅ **Diff Image Generation** - Visual differences highlighted in red
- ✅ **Allure Integration** - Screenshots and diffs attached to reports

### Quick Example

```java
import org.navadiya.visual.VisualValidator;

@Test
public void testHomePageVisualRegression() {
    WebDriver driver = getDriver();
    driver.get("https://example.com");
    
    // Validate against baseline (creates baseline on first run)
    boolean passed = VisualValidator.validateAgainstBaseline(
        driver,
        "HomePage",    // Baseline name
        0.95           // 95% similarity threshold
    );
    
    Assert.assertTrue(passed, "Visual regression detected!");
}
```

### Available Methods

#### 1. Validate Against Baseline (Recommended)
```java
// Pixel-by-pixel comparison
boolean passed = VisualValidator.validateAgainstBaseline(driver, "PageName", 0.95);

// ORB feature matching (more forgiving)
boolean passed = VisualValidator.validateAgainstBaselineWithORB(driver, "PageName", 0.70);
```

#### 2. Manual Comparison
```java
// Take screenshot and save
String actualPath = VisualValidator.takeAndSaveScreenshot(driver, "Page");

// Compare with baseline
boolean matches = VisualValidator.compareImages(
    "screenshots/baseline/page.png",
    actualPath,
    0.98  // threshold
);
```

#### 3. Save Baseline
```java
// Manually create/update baseline
String baselinePath = VisualValidator.saveBaseline(driver, "PageName");
```

### Directory Structure
```
screenshots/
├── baseline/       # Reference images
├── actual/         # Current screenshots  
└── diff/           # Difference images (red highlights)
```

### Threshold Guidelines
- **0.98 - 1.0**: Strict (pixel-perfect)
- **0.90 - 0.97**: Normal (minor differences allowed)
- **0.70 - 0.89**: Lenient (for dynamic content)


## 🔧 Self-Healing Tests (Healenium)

### Overview
**Healenium** provides AI-powered self-healing capabilities that automatically recover from broken locators when page elements change.

### How It Works
1. Test runs and finds an element using original locator
2. If locator breaks (DOM changes), Healenium analyzes the page
3. Finds the most similar element using AI/ML algorithms
4. Test continues without failing
5. Logs healing events for review

### Integration Status
✅ **Healenium is integrated** into the framework:
- Dependency: `com.epam.healenium:healenium-web:3.5.7`
- Automatic initialization in `BaseTest`
- Transparent operation (no code changes needed)
- Falls back to standard WebDriver if backend unavailable


### Configuration
Healenium requires a backend service. The framework includes:
- Database setup in `db/sql/init.sql`
- Docker Compose configuration (optional)

### Starting Healenium Backend (Optional)
```bash
# Using Docker Compose
docker-compose up -d healenium-hlm healenium-postgres

# Access Healenium UI
http://localhost:7878
```

### Benefits
- ✅ Reduced test maintenance
- ✅ Fewer false failures
- ✅ Automatic locator recovery
- ✅ Detailed healing reports
- ✅ Works with existing tests (no code changes)

---

## 🌐 Selenium Grid Setup

### Standalone Server (Manual)

#### Start Hub
```bash
java -jar selenium-server-4.38.0.jar hub --host 192.168.168.131 --port 4444
```

#### Start Node
```bash
java -jar selenium-server-4.38.0.jar node --hub http://192.168.168.131:4444
```

#### Grid Console
Open browser: `http://192.168.168.131:4444/ui`

### Configure Framework
```properties
selenium.grid.enabled=true
selenium.grid.url=http://192.168.168.131:4444
```

---

## 🐳 Docker Setup

### Start Selenium Grid with Docker Compose

```bash
docker-compose up -d
```

This starts:
- **Selenium Hub** on port 4444
- **Chrome Node** (2 sessions)
- **Firefox Node** (2 sessions)
- **Edge Node** (2 sessions)

### Check Grid Status
```bash
curl http://localhost:4444/status
```

### View Grid Console
Open browser: `http://localhost:4444/ui`

### Stop Grid
```bash
docker-compose down
```

### Configure Framework for Docker Grid
```bash
mvn test -Dapp.browsers=chrome,firefox -Dselenium.grid.enabled=true -Dselenium.grid.url=http://localhost:4444
```

---

## 🔄 Jenkins Integration

### Pipeline Parameters

The Jenkinsfile defines the following build parameters:

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| **ENV** | Choice | PROD | Environment: PROD, QA, DEV |
| **BROWSERS** | String | chrome,firefox,edge | Browsers to execute |
| **HEADLESS** | Boolean | false | Run headless mode |
| **GRID** | Boolean | true | Use Selenium Grid |
| **PARALLEL** | Boolean | true | Enable parallel execution |
| **SUITE_TEST_CLASS** | String | org.navadiya.tests.SampleTest | Test class(es) to run |

### Jenkins Job Configuration

1. **Create Pipeline Job** in Jenkins
2. **Configure Pipeline from SCM**:
   - Repository: `https://github.com/hardiknavadiya/Java-Selenium-Allure-Framework`
   - Branch: `develop`
   - Script Path: `Jenkinsfile`

3. **Required Jenkins Plugins**:
   - Allure Plugin
   - Pipeline Plugin
   - Git Plugin
   - Email Extension Plugin

4. **Configure Tools**:
   - **JDK**: JDK22
   - **Maven**: M3
   - **Allure**: Allure

### Jenkins Command Examples

```bash
# Standard Jenkins execution
mvn test -Dapp.env.default=${ENV} -Dapp.browsers=${BROWSERS} -Dapp.headless=${HEADLESS}

# With all parameters
mvn test -Dapp.env.default=PROD -Dapp.browsers=chrome,firefox -Dapp.headless=true -Dapp.parallel.enabled=true -Dselenium.grid.enabled=true -Dsuite.test.class=org.navadiya.tests.*
```

### Pipeline Stages

1. **Checkout** - Clone repository from GitHub
2. **Build & Test** - Execute Maven tests with parameters
3. **Publish Allure Report** - Generate and publish Allure HTML report
4. **Email Notification** - Send test results via email

---

## 📊 Allure Reporting

### Generate & View Reports

#### Local Execution
```bash
# Run tests and generate report
mvn clean test

# Serve report (opens in browser automatically)
mvn allure:serve

# Or open existing report
mvn allure:report
# Report location: target/my-allure-html-report/
```

#### Report Features
The framework includes comprehensive Allure reporting with:

- ✅ **Test Execution Details** - Duration, status, parameters
- ✅ **Screenshots** - Attached on test failures automatically
- ✅ **Visual Diffs** - OpenCV comparison images with red highlights
- ✅ **Feature Matches** - ORB algorithm visualization
- ✅ **Step-by-Step Logs** - Detailed test execution flow
- ✅ **Historical Trends** - Track test stability over time
- ✅ **Categories** - Flaky tests, product defects, test defects
- ✅ **Environment Info** - Browser, OS, configuration
- ✅ **Retry Tracking** - View retry attempts and results
- ✅ **Test Organization** - Epic, Feature, Story hierarchy

### Allure Annotations

```java
@Epic("E-Commerce")
@Feature("Checkout")
@Story("Payment Processing")
@Severity(SeverityLevel.CRITICAL)
@Test
public void testPaymentFlow() {
    // Test implementation
}

@Step("Enter credit card details")
public void enterCardDetails(String cardNumber) {
    // Step implementation
}
```

### Visual Validation in Reports

When visual validation fails, Allure automatically includes:
1. **Baseline Image** - Original reference image
2. **Actual Screenshot** - Current test screenshot
3. **Diff Image** - Red-highlighted differences
4. **Feature Matches** - ORB algorithm visualization (if used)
5. **Similarity Score** - Percentage match

### History Preservation

Reports maintain history across builds:
```
allure-history/
├── categories-trend.json
├── duration-trend.json
├── history-trend.json
├── history.json
└── retry-trend.json
```

This enables:
- Trend analysis over time
- Flaky test identification
- Performance tracking
- Failure pattern detection

### Jenkins Integration

After Jenkins pipeline execution:
1. Click **Allure Report** in build sidebar
2. View comprehensive test results
3. Analyze trends and failures
4. Download artifacts

---

## 🎯 Advanced Features

### 1. Test Retry Mechanism

Configure retry attempts for flaky tests:

```properties
app.rerun.attempts=2
```

Or via command line:
```bash
mvn test -Dapp.rerun.attempts=2
```

### 2. OpenCV Visual Validation

Advanced image comparison with multiple algorithms:

**Pixel-by-Pixel Comparison:**
```java
boolean passed = VisualValidator.validateAgainstBaseline(driver, "HomePage", 0.95);
```

**ORB Feature Matching (more robust):**
```java
boolean passed = VisualValidator.validateAgainstBaselineWithORB(driver, "Dashboard", 0.70);
```

**Manual Comparison:**
```java
String actual = VisualValidator.takeAndSaveScreenshot(driver, "Page");
boolean matches = VisualValidator.compareImages(baseline, actual, 0.98);
```

### 3. Healenium Self-Healing

Automatic locator recovery when elements change:
- ✅ No code changes required
- ✅ AI-powered element matching
- ✅ Transparent operation
- ✅ Detailed healing logs

### 4. Dynamic Test Discovery

Execute all tests in a package:
```bash
mvn test -Dsuite.test.class=org.navadiya.tests.*
```

Execute specific test classes:
```bash
mvn test -Dsuite.test.class=org.navadiya.tests.TitleTest,org.navadiya.tests.LinkPresenceTest
```

### 5. ThreadLocal WebDriver

Thread-safe WebDriver management for parallel execution:
```java
// Each thread gets isolated WebDriver instance
WebDriver driver = DriverManager.getDriver();
```

### 6. Custom Test Listeners

Automated test management:
- **RetryAnalyzer** - Automatic retry on failure
- **TestListener** - Allure attachments, screenshots, logging
- **RetryAnnotationTransformer** - Apply retry strategy to all tests

### 7. Multi-Environment Support

Switch environments dynamically:
```bash
mvn test -Dapp.env.default=QA
mvn test -Dapp.env.default=PROD
```

URLs configured in `environments.properties`.

### 8. Comprehensive Logging

SLF4J with Logback:
```java
@Slf4j  // Lombok annotation
public class MyTest extends BaseTest {
    @Test
    public void testExample() {
        log.info("Test started");
        log.debug("Debug information");
        log.error("Error occurred", exception);
    }
}
```

---

## 📦 Technology Stack

| Category | Technology | Version | Purpose |
|----------|-----------|---------|---------|
| **Language** | Java | 22 | Core programming language |
| **Build Tool** | Maven | 3.x | Dependency & build management |
| **Test Framework** | TestNG | 7.8.0 | Test execution & organization |
| **Web Automation** | Selenium | 4.36.0 | Browser automation |
| **Driver Management** | WebDriverManager | 6.3.2 | Automatic driver binaries |
| **Reporting** | Allure | 2.20.1 | Test reporting & analytics |
| **Visual Testing** | OpenCV | 4.9.0 | Image comparison & analysis |
| **Self-Healing** | Healenium | 3.5.7 | Auto-recovery of locators |
| **Image Processing** | TwelveMonkeys | 3.11.0 | Enhanced image I/O |
| **JSON** | Gson | 2.10.1 | JSON parsing |
| **HTTP Client** | OkHttp | 4.12.0 | HTTP operations |
| **Logging** | SLF4J + Logback | 2.0.7 | Structured logging |
| **Code Quality** | Lombok | 1.18.42 | Boilerplate reduction |

---

## 🐛 Troubleshooting

### Common Issues

#### 1. WebDriver Binary Issues
**Error**: `Cannot find Chrome/Firefox driver`

**Solution**: 
- WebDriverManager handles this automatically
- Check internet connectivity
- Verify browser is installed
- Clear WebDriverManager cache: `~/.cache/selenium/`

#### 2. OpenCV Initialization Failed
**Error**: `OpenCV not initialized. Cannot perform image comparison.`

**Solution**:
```bash
# Verify OpenCV dependency
mvn dependency:tree | grep opencv

# Clean and rebuild
mvn clean install -DskipTests
```

#### 3. Healenium Connection Failed
**Error**: `Unable to connect to the hlm-backend service`

**Solution**:
- This is expected if Healenium backend is not running
- Framework falls back to standard WebDriver automatically
- To enable Healenium, start backend via Docker:
```bash
docker-compose up -d healenium-hlm healenium-postgres
```

#### 4. Allure Results Empty
**Solution**: 
```bash
mvn clean test  # Regenerate results
mvn allure:report
```

#### 5. Visual Validation Baseline Not Found
**Error**: `Baseline not found`

**Solution**:
- First run creates baseline automatically
- Or manually create: `VisualValidator.saveBaseline(driver, "PageName")`
- Verify `screenshots/baseline/` directory exists

#### 6. Visual Validation False Positives
**Issue**: Tests fail due to acceptable changes

**Solution**:
- Lower threshold: `0.95` → `0.90`
- Use ORB matching instead: `validateAgainstBaselineWithORB()`
- Update baseline if changes are intentional

#### 7. Selenium Grid Connection Failed
**Error**: `Could not start a new session`

**Solution**: 
- Verify grid is running: `curl http://localhost:4444/status`
- Check grid URL in configuration
- Ensure nodes are registered with hub
- Verify browser nodes are available

#### 8. Parallel Execution Issues
**Solution**:
- Reduce thread count: `-Dapp.parallel.threads=2`
- Disable parallel: `-Dapp.parallel.enabled=false`
- Verify sufficient system resources

#### 9. Jenkins Build Fails
**Solution**:
- Verify JDK 22, Maven 3.x, Allure tools are configured
- Check workspace permissions
- Review console output for specific errors
- Validate Jenkinsfile syntax

#### 10. Element Not Found (Despite Healenium)
**Solution**:
- Healenium backend must be running for self-healing
- Check logs for healing attempts
- Increase wait timeouts
- Update locator strategy

---

## 📚 Additional Resources

### Documentation
- 📖 **Selenium Docs**: [selenium.dev](https://www.selenium.dev/)
- 📖 **TestNG Docs**: [testng.org](https://testng.org/)
- 📖 **Allure Docs**: [docs.qameta.io/allure](https://docs.qameta.io/allure/)
- 📖 **OpenCV Docs**: [opencv.org](https://opencv.org/)
- 📖 **Healenium Docs**: [healenium.io](https://healenium.io/)

### Video Tutorials
- 🎥 Selenium 4 Features
- 🎥 TestNG Best Practices
- 🎥 Allure Reporting
- 🎥 OpenCV Visual Testing

---

## 🎯 Framework Highlights

### What Makes This Framework Production-Ready?

✅ **Advanced Visual Testing**
- OpenCV integration for pixel-perfect and feature-based comparison
- Automatic baseline management
- Visual diff generation with red highlights

✅ **Self-Healing Capabilities**
- Healenium AI-powered locator recovery
- Reduced test maintenance
- Automatic element identification

✅ **Robust Test Execution**
- Thread-safe parallel execution
- Automatic retry mechanism
- Smart wait utilities

✅ **Comprehensive Reporting**
- Beautiful Allure reports
- Visual validation results
- Historical trend analysis

✅ **Enterprise CI/CD**
- Jenkins pipeline integration
- Docker Selenium Grid support
- Email notifications

✅ **Developer Friendly**
- Clean architecture
- Extensive logging
- Property-driven configuration
- Well-documented code

---

## 📞 Support & Contact

- **Author**: Hardik Navadiya
- **Email**: hardiknavadiya5@gmail.com
- **GitHub**: [@hardiknavadiya](https://github.com/hardiknavadiya)
- **Repository**: [Java-Selenium-Allure-Framework](https://github.com/hardiknavadiya/Java-Selenium-Allure-Framework)

### Contributing
Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Submit a pull request

---

## 📄 License

This project is open-source and available under the **MIT License**.

---

## 🙏 Acknowledgments

Special thanks to the open-source community and these amazing projects:

- **[Selenium](https://www.selenium.dev/)** - Browser automation framework
- **[TestNG](https://testng.org/)** - Testing framework  
- **[Allure](https://docs.qameta.io/allure/)** - Reporting framework
- **[OpenCV](https://opencv.org/)** - Computer vision library
- **[Healenium](https://healenium.io/)** - Self-healing test library
- **[WebDriverManager](https://bonigarcia.dev/webdrivermanager/)** - Automatic driver management
- **[Apache Maven](https://maven.apache.org/)** - Build automation
- **[Jenkins](https://www.jenkins.io/)** - CI/CD orchestration
- **[Docker](https://www.docker.com/)** - Containerization platform

---

## ⭐ Star This Repository

If you find this framework helpful, please consider giving it a ⭐ on GitHub!

**Built with ❤️ for the Test Automation Community**

---

*Last Updated: November 2025*

## 📝 Quick Reference Commands

```bash
# Local execution
mvn clean test

# CI/CD execution (Jenkins)
mvn test -Dapp.env.default=PROD -Dapp.browsers=chrome,firefox -Dapp.headless=true

# Selenium Grid execution
mvn test -Dapp.browsers=chrome,firefox -Dselenium.grid.enabled=true -Dselenium.grid.url=http://192.168.168.131:4444

# Parallel execution
mvn test -Dapp.browsers=chrome,firefox,edge -Dapp.parallel.enabled=true -Dapp.parallel.threads=3

# Generate Allure report
mvn allure:serve

# Start Docker Selenium Grid
docker-compose up -d

# Stop Docker Grid
docker-compose down
```

---

**Happy Testing! 🎉**
