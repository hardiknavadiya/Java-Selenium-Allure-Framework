# 🚀 Java Selenium TestNG Allure Framework

[![Java](https://img.shields.io/badge/Java-22-orange.svg)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.36.0-green.svg)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.8.0-red.svg)](https://testng.org/)
[![Allure](https://img.shields.io/badge/Allure-2.20.1-yellow.svg)](https://docs.qameta.io/allure/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg)](https://maven.apache.org/)

A robust, production-ready Selenium test automation framework built with Java, TestNG, and Allure reporting. Features dynamic test suite generation, parallel execution, automatic retry mechanism, Selenium Grid support, and comprehensive CI/CD integration with Jenkins.

---

## 📋 Table of Contents

- [Features](#-features)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Running Tests](#-running-tests)
- [Selenium Grid Setup](#-selenium-grid-setup)
- [Docker Setup](#-docker-setup)
- [Jenkins Integration](#-jenkins-integration)
- [Allure Reporting](#-allure-reporting)
- [Advanced Features](#-advanced-features)
- [Troubleshooting](#-troubleshooting)

---

## ✨ Features

### Core Capabilities
- **Dynamic Test Suite Generation** - Programmatic TestNG suite creation via `SuiteRunner`
- **Multi-Browser Support** - Chrome, Firefox, Edge with automatic driver management
- **Parallel Execution** - Cross-browser parallel testing with configurable thread pools
- **Automatic Retry** - Failed test retry mechanism with configurable attempts
- **Allure Reporting** - Rich HTML reports with screenshots, logs, and history trends
- **Selenium Grid Integration** - Support for local grid, Docker Selenium, and cloud providers
- **Environment Management** - Multi-environment configuration (DEV, QA, PROD)
- **CI/CD Ready** - Full Jenkins pipeline with parameterized builds
- **Visual Testing** - Screenshot capture on test failure
- **Headless Mode** - Support for headless browser execution

### Technical Features
- ThreadLocal WebDriver management for thread-safe parallel execution
- Lombok integration for cleaner code
- WebDriverManager for automatic driver binary management
- Allure history preservation across builds
- Excel data-driven testing support (Apache POI)
- REST API testing capabilities (REST-assured)
- Comprehensive logging (SLF4J)
- Docker Compose for Selenium Grid orchestration

---

## 📁 Project Structure

```
Java-Selenium-Allure-Framework/
├── src/
│   ├── main/java/org/navadiya/
│   │   ├── config/           # Configuration management
│   │   ├── driver/           # WebDriver factory and manager
│   │   └── visual/           # Visual testing utilities
│   └── test/
│       ├── java/org/navadiya/
│       │   ├── BaseTest.java          # Base test class with setup/teardown
│       │   ├── SuiteRunner.java       # Dynamic TestNG suite generator
│       │   ├── listeners/             # TestNG listeners (Retry, Test)
│       │   └── tests/                 # Test classes
│       └── resources/
│           ├── application.properties      # Framework configuration
│           ├── environments.properties     # Environment URLs
│           └── allure.properties          # Allure settings
├── SuiteRunnerTest.java      # TestNG wrapper for Maven execution
├── pom.xml                   # Maven dependencies and plugins
├── Jenkinsfile               # CI/CD pipeline definition
├── docker-compose.yml        # Selenium Grid Docker setup
└── README.md                 # This file
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

Run `SuiteRunnerTest.java` as TestNG test.

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

### Generate Allure Report Locally

#### After Test Execution
```bash
# Generate report
mvn allure:report

# Serve report on local server
mvn allure:serve
```

#### View Report
Report opens automatically at `http://localhost:<random-port>`

### Allure Report Location
- **Results**: `target/allure-results/`
- **Report**: `target/my-allure-html-report/`
- **History**: `allure-history/` (persisted across builds)

### Allure Features in Framework

- ✅ Test step logging with `@Step` annotations
- ✅ Screenshot attachment on failure
- ✅ Test categorization with `@Epic`, `@Feature`, `@Story`
- ✅ Severity levels with `@Severity`
- ✅ Historical trend analysis
- ✅ Environment information
- ✅ Test retry tracking

### Access Allure in Jenkins

After pipeline execution:
- Click **Allure Report** link in build sidebar
- View trends, suites, graphs, and timelines

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

### 2. Dynamic Test Discovery

Execute all tests in a package:
```bash
mvn test -Dsuite.test.class=org.navadiya.tests.*
```

Execute specific test classes:
```bash
mvn test -Dsuite.test.class=org.navadiya.tests.TitleTest,org.navadiya.tests.LinkPresenceTest
```

### 3. ThreadLocal WebDriver

Each thread gets isolated WebDriver instance for parallel execution:
```java
WebDriver driver = DriverManager.getDriver();
```

### 4. Custom Test Listeners

- **RetryAnalyzer** - Automatic retry on failure
- **TestListener** - Allure attachments and logging
- **RetryAnnotationTransformer** - Apply retry to all tests

### 5. Multi-Environment Support

Switch environments dynamically:
```bash
mvn test -Dapp.env.default=QA
```

URLs configured in `environments.properties`.

---

## 🐛 Troubleshooting

### Common Issues

#### 1. SuiteRunner Not Found
**Error**: `No tests matching pattern "org.navadiya.SuiteRunnerTest" were executed`

**Solution**: Ensure `SuiteRunnerTest.java` exists in `src/test/java/` root directory.

#### 2. WebDriver Binary Issues
**Error**: `Cannot find Chrome/Firefox driver`

**Solution**: WebDriverManager handles this automatically. Check internet connectivity.

#### 3. Allure Results Empty
**Solution**: 
```bash
mvn clean test  # Regenerate results
mvn allure:report
```

#### 4. Selenium Grid Connection Failed
**Error**: `Could not start a new session`

**Solution**: 
- Verify grid is running: `curl http://localhost:4444/status`
- Check grid URL in configuration
- Ensure nodes are registered with hub

#### 5. Parallel Execution Issues
**Solution**:
- Reduce thread count: `-Dapp.parallel.threads=2`
- Disable parallel: `-Dapp.parallel.enabled=false`

#### 6. Jenkins Build Fails
**Solution**:
- Verify JDK, Maven, Allure tools are configured
- Check workspace permissions
- Review console output for specific errors

---

## 📞 Support & Contact

- **Author**: Hardik Navadiya
- **Email**: hardiknavadiya51@gmail.com
- **GitHub**: [hardiknavadiya](https://github.com/hardiknavadiya)
- **Repository**: [Java-Selenium-Allure-Framework](https://github.com/hardiknavadiya/Java-Selenium-Allure-Framework)

---

## 📄 License

This project is open-source and available under the MIT License.

---

## 🙏 Acknowledgments

- **Selenium** - Browser automation framework
- **TestNG** - Testing framework
- **Allure** - Reporting framework
- **WebDriverManager** - Automatic driver management
- **Apache Maven** - Build automation
- **Jenkins** - CI/CD orchestration

---

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
