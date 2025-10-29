Running this Selenium/TestNG/Allure Maven project on Jenkins

Overview
This repository contains a Maven-based Selenium TestNG project with Allure reporting. The included `Jenkinsfile` defines a Declarative Pipeline that:
- checks out the repository
- runs `mvn clean test` with Selenium Grid enabled (configurable)
- publishes Allure results to the Allure Jenkins plugin

Prerequisites on Jenkins
1. Jenkins installed (recommended LTS). Install the following plugins:
   - Git
   - Pipeline
   - Maven Integration
   - Allure Jenkins Plugin
2. Configure Global Tools in Jenkins > Manage Jenkins > Global Tool Configuration:
   - JDK (name it `JDK` or change name in `Jenkinsfile`)
   - Maven (name it `Maven` or change name in `Jenkinsfile`)
   - (Optional) Allure Commandline (not required if using plugin's builtin)
3. Ensure the Jenkins agent(s) that run the job have access to a running Selenium Grid (or browsers)
   - If using Docker-based Selenium Grid, ensure the grid URL (e.g., http://localhost:4444) is reachable from the agent.
4. Add any cloud credentials (BrowserStack/Sauce/Lambda) as Jenkins credentials and map them to environment variables if used.

How the pipeline runs
- The pipeline calls:
  mvn -B clean test -Dselenium.grid.enabled=true -Dselenium.grid.url=${GRID_URL} -Dapp.parallel.enabled=true
- `GRID_URL` environment variable may be set in Jenkins job configuration (recommended to point to your Grid)

Tips & Troubleshooting
- If you see `Cannot find class in classpath: org.navadiya.tests.SampleTest`, ensure that the test classes are under `src/test/java/org/navadiya/tests` and that `pom.xml` compiles tests.
- If Allure shows duplicate or skipped test entries when retrying, remove duplicate Allure listener registrations in `testng.xml` or test classes (only one Allure adapter should be registered).
- Make sure the Jenkins agent has network access to the Selenium Grid (correct host/port), or run the Grid on the same agent.
- On Windows agents, Jenkins runs with limited PATH; ensure browser driver binaries or Docker are accessible, or rely on the project using `WebDriverManager` (this project uses WebDriverManager for local runs).

Customizing the job
- To run only specific browsers, override system property `-Dbrowsers=chrome,firefox,edge` or change `app.browsers` in `src/test/resources/application.properties`.
- To run tests in parallel across browsers, keep `app.parallel.enabled=true` and configure `app.parallel.threads` or edit `testng.xml` accordingly.

Example Jenkins multi-branch pipeline job
- Add this repo as a Multibranch Pipeline or regular Pipeline and ensure Jenkinsfile is in repo root.
- Set `GRID_URL` variable in the job to `http://your.grid.host:4444`.

Contact
If you want, I can also:
- Add a `testng.xml` that runs `org.navadiya.tests` package in parallel across chrome/firefox/edge
- Add a ThreadLocal `DriverManager` and `BaseTest` class to make parallel runs safe
- Add example Jenkins job configuration and sample Groovy script for a scripted pipeline


