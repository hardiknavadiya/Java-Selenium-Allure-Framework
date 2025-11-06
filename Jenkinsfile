pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/hardiknavadiya/Java-Selenium-Allure-Framework'
        BRANCH = 'develop'
        ALLURE_RESULTS = 'target/allure-results'
        ALLURE_REPORT = 'target/allure-report'
        EMAIL_RECIPIENTS = 'hardiknavadiya5@gmail.com'
        // Let POM Surefire <includes> pick the wrapper test; avoid -Dtest pattern mismatch
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
        string(
            name: 'SUITE_TEST_CLASS',
            defaultValue: 'org.navadiya.tests.SampleTest',
            description: 'FQCN(s) for SuiteRunner to execute (comma-separated or package.*)'
        )
    }

    stages {
        stage('Clean Workspace') {
            steps {
                echo "Cleaning workspace and removing all files..."
                script {
                    if (isUnix()) {
                        sh '''
                          # Remove all files and directories in workspace
                          rm -rf ./*
                          rm -rf ./.git
                          rm -rf ./.[!.]*
                        '''
                    } else {
                        bat '''
                          @echo off
                          echo Removing all files from workspace...
                          del /F /S /Q * 2>nul
                          for /d %%x in (*) do @rd /s /q "%%x" 2>nul
                          if exist .git rd /s /q .git 2>nul
                        '''
                    }
                }
                // Alternative: Use Jenkins built-in workspace cleanup
                // deleteDir()
            }
        }

        stage('Checkout') {
            steps {
                echo "Cloning fresh repository from ${GIT_REPO} (branch: ${BRANCH})..."
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${BRANCH}"]],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [
                        [$class: 'CleanBeforeCheckout'],
                        [$class: 'CloneOption', noTags: false, shallow: false, depth: 0]
                    ],
                    userRemoteConfigs: [[url: "${GIT_REPO}"]]
                ])
            }
        }

        stage('Build & Test') {
            steps {
                echo "Running tests in ${params.ENV} environment with browsers: ${params.BROWSERS}"

                script {
                    def args = "${MAVEN_CMD} -Dapp.env.default=${params.ENV} -Dapp.browsers=${params.BROWSERS} -Dapp.headless=${params.HEADLESS} -Dapp.parallel.enabled=${params.PARALLEL} -Dselenium.grid.enabled=${params.GRID} -Dsuite.test.class=${params.SUITE_TEST_CLASS}"
                    if (isUnix()) {
                        sh """
                          echo "Cleaning previous build artifacts..."
                          rm -rf target allure-results test-output
                          echo "Running Maven tests..."
                          ${args}
                        """
                    } else {
                        bat """
                          @echo off
                          echo Cleaning previous build artifacts...
                          if exist target rmdir /s /q target 2>nul
                          if exist allure-results rmdir /s /q allure-results 2>nul
                          if exist test-output rmdir /s /q test-output 2>nul
                          echo Running Maven tests...
                          ${args}
                        """
                    }
                }
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
                         <p>See Allure report: <a href=\"${BUILD_URL}allure\">Click here</a></p>""",
                to: "${EMAIL_RECIPIENTS}",
                mimeType: 'text/html'
            )
        }
        failure {
            emailext (
                subject: "❌ Selenium Tests Failed - Build #${BUILD_NUMBER}",
                body: """<p>Some tests failed in ${params.ENV} environment.</p>
                         <p>See Allure report: <a href=\"${BUILD_URL}allure\">Click here</a></p>""",
                to: "${EMAIL_RECIPIENTS}",
                mimeType: 'text/html'
            )
        }
    }
}
