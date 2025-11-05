pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/hardiknavadiya/Java-Selenium-Allure-Framework'
        BRANCH = 'master'
        ALLURE_RESULTS = 'target/allure-results'
        ALLURE_REPORT = 'target/allure-report'
        EMAIL_RECIPIENTS = 'hardiknavadiya5@gmail.com'
        MAVEN_CMD = 'mvn test'
    }
    tools {
            allure 'Allure'
            maven 'M3'
            jdk 'JDK22'
    }

    parameters {
        choice(
            name: 'ENV',
            choices: ['PROD', 'QA', 'DEV'],
            description: 'Choose environment to run tests'
        )
        string(
            name: 'BROWSERS',
            defaultValue: 'chrome,firefox,edge',
            description: 'Browsers to run tests'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: false,
            description: 'Run browsers in headless mode'
        )
        booleanParam(
            name: 'GRID',
            defaultValue: true,
            description: 'Run browsers in Selenium Grid'
        )
        booleanParam(
            name: 'PARALLEL',
            defaultValue: true,
            description: 'Run browsers in parallel mode'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: "${BRANCH}", url: "${GIT_REPO}"
            }
        }

        stage('Build & Test') {
            steps {
                echo "Running tests in ${params.ENV} environment with browsers: ${params.BROWSERS}"

                sh """
                  rm -rf target
                  ${MAVEN_CMD} \
                    -Dapp.env.default=${params.ENV} \
                    -Dapp.browsers=${params.BROWSERS} \
                    -Dapp.headless=${params.HEADLESS} \
                    -Dapp.parallel.enabled=${params.PARALLEL} \
                    -Dselenium.grid.enabled=${params.GRID}
                """
              }
              post {
                always {
                  // Publish TestNG test results from surefire reports
                  junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
              }
        }

        stage('Publish Allure Report') {
            steps {
                // This step USES the 'Allure' tool to generate and publish the report
                allure includeProperties: false, jdk: '', results: [[path: "${ALLURE_RESULTS}"]]
            }
        }
    }

    post {
        success {
            emailext (
                subject: "✅ Selenium Tests Passed - Build #${BUILD_NUMBER}",
                body: """<p>All tests passed successfully in ${params.ENV} environment.</p>
                         <p>See Allure report: <a href="${BUILD_URL}allure">Click here</a></p>""",
                to: "${EMAIL_RECIPIENTS}",
                mimeType: 'text/html'
            )
        }
        failure {
            emailext (
                subject: "❌ Selenium Tests Failed - Build #${BUILD_NUMBER}",
                body: """<p>Some tests failed in ${params.ENV} environment.</p>
                         <p>See Allure report: <a href="${BUILD_URL}allure">Click here</a></p>""",
                to: "${EMAIL_RECIPIENTS}",
                mimeType: 'text/html'
            )
        }
    }
}
