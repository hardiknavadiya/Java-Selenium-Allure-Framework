pipeline {
    agent any

    environment {
        GIT_REPO = 'https://github.com/hardiknavadiya/Java-Selenium-Allure-Framework'
        BRANCH = 'master'
        ALLURE_RESULTS = 'target/allure-results'
        ALLURE_REPORT = 'target/allure-report'
        ALLURE_HISTORY = 'allure-history'
        EMAIL_RECIPIENTS = 'hardiknavadiya51@gmail.com'
        MAVEN_CMD = 'mvn clean test'
    }

    parameters {
        choice(
            name: 'ENV',
            choices: ['QA', 'PROD', 'DEV'],
            description: 'Choose environment to run tests'
        )
        string(
            name: 'BROWSERS',
            defaultValue: 'chrome,firefox,edge',
            description: 'Browsers to run tests'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browsers in headless mode'
        )
        booleanParam(
            name: 'PARALLEL',
            defaultValue: false,
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
                echo "Running tests in ${params.ENV} environment on browsers: ${params.BROWSERS} (headless: ${params.HEADLESS}) (parallel: ${params.PARALLEL})"
                    sh "${MAVEN_CMD} -Dapp.env.default=${params.ENV} -Dapp.browsers='${params.BROWSERS}' -Dapp.headless=${params.HEADLESS} -Dapp.parallel.enabled=${params.PARALLEL}"
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                echo 'Generating Allure Report...'
                sh '''
                    if [ -d "${ALLURE_HISTORY}" ]; then
                        cp -r ${ALLURE_HISTORY} ${ALLURE_RESULTS}/history || true
                    fi
                    allure generate ${ALLURE_RESULTS} --clean -o ${ALLURE_REPORT}
                    mkdir -p ${ALLURE_HISTORY}
                    cp -r ${ALLURE_REPORT}/history/* ${ALLURE_HISTORY}/ || true
                '''
            }
        }

        stage('Publish Allure Report') {
            steps {
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
