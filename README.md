# Java Selenium Allure Framework

A comprehensive Selenium-based test automation framework with TestNG, Allure reporting, and parallel execution capabilities.

## Main Class Path

The main entry point for this project is:

```
src/test/java/org/navadiya/suiteRunner.java
```

This class contains the `main()` method that programmatically configures and executes TestNG test suites.

## Project Structure

```
src/
├── main/java/org/navadiya/
│   ├── config/
│   │   └── ApplicationConfig.java      # Configuration management
│   ├── driver/
│   │   ├── DriverManager.java          # WebDriver lifecycle management
│   │   └── WebDriverFactory.java       # WebDriver instance creation
│   └── visual/
│       └── VisualValidator.java        # Visual testing utilities
│
└── test/java/org/navadiya/
    ├── suiteRunner.java                # Main class - Test execution entry point
    ├── BaseTest.java                   # Base test class with setup/teardown
    ├── listeners/
    │   ├── RetryAnalyzer.java         # Test retry logic
    │   ├── RetryAnnotationTransformer.java
    │   └── TestListener.java          # TestNG listener for logging
    └── tests/
        ├── SampleTest.java
        ├── TitleTest.java
        ├── HeaderElementTest.java
        ├── FooterTest.java
        ├── ImageLoadTest.java
        └── LinkPresenceTest.java
```

## Features

- ✅ Multi-browser support (Chrome, Firefox, Edge)
- ✅ Parallel test execution
- ✅ Automatic test retry on failure
- ✅ Allure reporting integration
- ✅ Selenium Grid support
- ✅ Configurable environments (DEV, QA, PROD)
- ✅ Headless browser execution
- ✅ WebDriverManager for automatic driver management
- ✅ Jenkins CI/CD pipeline integration

## Prerequisites

- Java 22 or higher (as specified in pom.xml)
  - Note: The pom.xml is configured for Java 22, which is not an LTS version
  - For production use, consider updating to Java 17 (LTS) or Java 21 (LTS)
  - To change Java version: update `maven.compiler.source` and `maven.compiler.target` in `pom.xml`
- Maven 3.6+
- Browsers installed (Chrome, Firefox, Edge)

## Running Tests

### Option 1: Using Maven (Recommended)

Run all tests with default configuration:
```bash
mvn clean test
```

Run with specific browsers:
```bash
mvn test -Dapp.browsers=chrome,firefox
```

Run in parallel with custom thread count:
```bash
mvn test -Dapp.parallel.enabled=true -Dapp.parallel.threads=4
```

Run with environment configuration:
```bash
mvn test -Dapp.env.default=DEV
```

Run in headless mode:
```bash
mvn test -Dapp.headless=true
```

### Option 2: Using the Main Class Directly

You can also run tests by executing the main class directly after compiling:

```bash
# Compile the project
mvn clean compile test-compile

# Run the main class (Linux/Mac)
java -cp "target/test-classes:target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" \
  org.navadiya.suiteRunner

# Run the main class (Windows)
# First generate classpath: mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
# Then: java -cp "target/test-classes;target/classes;%classpath_from_file%" org.navadiya.suiteRunner
```

With system properties (Linux/Mac):
```bash
java -Dapp.browsers=chrome,firefox \
     -Dapp.parallel.enabled=true \
     -Dapp.parallel.threads=4 \
     -Dapp.env.default=PROD \
     -cp "target/test-classes:target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" \
     org.navadiya.suiteRunner
```

**Note:** Direct execution requires all dependencies on the classpath. Using Maven (Option 1) is recommended for simplicity.

### Option 3: Using Selenium Grid

Enable Selenium Grid in your run:
```bash
mvn test -Dselenium.grid.enabled=true -Dselenium.grid.url=http://localhost:4444
```

## Configuration

The framework uses `src/test/resources/application.properties` for default configuration:

```properties
# Default settings
app.env.default=PROD
app.browsers=firefox
app.timeout.seconds=15
app.parallel.enabled=true
app.parallel.threads=4
app.rerun.attempts=0
app.headless=true
suite.test.class=org.navadiya.tests.*

# Selenium Grid
selenium.grid.enabled=false
selenium.grid.url=http://localhost:4444
```

All configuration properties can be overridden using system properties (e.g., `-Dapp.browsers=chrome`).

## Viewing Allure Reports

Generate and view Allure reports after test execution:

```bash
# Generate report
mvn allure:report

# Serve report (opens in browser)
mvn allure:serve
```

The Allure report will be generated in `target/my-allure-html-report/`.

## Environment Configuration

Configure different test environments in `src/test/resources/environments.properties`:

```properties
# DEV environment
DEV.base.url=https://dev.example.com
DEV.api.url=https://api-dev.example.com

# QA environment
QA.base.url=https://qa.example.com
QA.api.url=https://api-qa.example.com

# PROD environment
PROD.base.url=https://www.example.com
PROD.api.url=https://api.example.com
```

## Writing Tests

All test classes should extend `BaseTest` which provides WebDriver setup and teardown:

```java
package org.navadiya.tests;

import org.navadiya.BaseTest;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MyTest extends BaseTest {
    
    @Test(description = "Verify page title")
    public void testPageTitle() {
        driver.get("https://www.example.com");
        String title = driver.getTitle();
        assertThat(title).isNotEmpty();
    }
}
```

## Jenkins Integration

This project includes a `Jenkinsfile` for CI/CD integration. See [README_JENKINS.md](README_JENKINS.md) for detailed Jenkins setup instructions.

## Docker Support

Run tests in Docker using the included `docker-compose.yml`:

```bash
docker-compose up -d
mvn test -Dselenium.grid.enabled=true -Dselenium.grid.url=http://localhost:4444
docker-compose down
```

## Troubleshooting

### ClassNotFoundException for test classes
Ensure test classes are compiled:
```bash
mvn clean test-compile
```

### Browser driver issues
The framework uses WebDriverManager which automatically downloads and manages browser drivers. If issues persist, clear the driver cache:
```bash
rm -rf ~/.cache/selenium
```

### Parallel execution issues
If tests fail during parallel execution, ensure tests are thread-safe and don't share mutable state.

## License

This project is open source and available for educational and commercial use.

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues.

## References

- Referenced structure from: https://github.com/ksqartechinc/selenium-automation-testing
- Selenium Documentation: https://www.selenium.dev/documentation/
- TestNG Documentation: https://testng.org/doc/
- Allure Reports: https://docs.qameta.io/allure/
