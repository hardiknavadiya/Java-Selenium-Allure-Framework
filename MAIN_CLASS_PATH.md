# Quick Reference: Main Class Path

## Main Entry Point

The main class for this Java Selenium Allure Framework is located at:

**Path:** `src/test/java/org/navadiya/suiteRunner.java`

**Full Qualified Class Name:** `org.navadiya.suiteRunner`

## What is the Main Class?

The `suiteRunner` class is the programmatic TestNG runner that:
- Configures and executes test suites
- Manages multi-browser test execution
- Handles parallel execution settings
- Implements test retry logic
- Integrates with Allure reporting

## How to Execute

### Using Maven:
```bash
mvn clean test
```

### Using Java directly:
```bash
java -cp "target/test-classes:target/classes:$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)" org.navadiya.suiteRunner
```

### With Configuration Options:
```bash
mvn test -Dapp.browsers=chrome,firefox -Dapp.parallel.enabled=true
```

## Key Features of suiteRunner

- ✅ Reads configuration from `application.properties`
- ✅ Accepts system property overrides
- ✅ Creates TestNG XML suites programmatically
- ✅ Supports package-level test discovery (e.g., `org.navadiya.tests.*`)
- ✅ Enables retry on test failure
- ✅ Integrates Allure listeners automatically

## Configuration Properties

The main class respects these configuration properties:

| Property | Default | Description |
|----------|---------|-------------|
| `app.env.default` | `PROD` | Test environment (DEV, QA, PROD) |
| `app.browsers` | `firefox` | Comma-separated browser list |
| `app.parallel.enabled` | `true` | Enable parallel execution |
| `app.parallel.threads` | `4` | Number of parallel threads |
| `app.rerun.attempts` | `0` | Number of retry attempts for failed tests |
| `suite.test.class` | `org.navadiya.tests.*` | Test class(es) to execute |

## Related Files

- Configuration: `src/test/resources/application.properties`
- Environment settings: `src/test/resources/environments.properties`
- Base test class: `src/test/java/org/navadiya/BaseTest.java`
- Test classes: `src/test/java/org/navadiya/tests/`

For detailed documentation, see [README.md](README.md).
