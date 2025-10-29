pipeline {
  agent any
  tools {
    // Set these tool names in Jenkins Global Tool Configuration to match your environment
    maven 'Maven'
    jdk 'JDK'
  }
  environment {
    MAVEN_OPTS = "-Xmx1024m"
    // optional override - set GRID_URL in Jenkins job or leave default below
    GRID_URL = "http://localhost:4444"
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    stage('Build & Test') {
      steps {
        script {
          // Build command: enable Selenium Grid and point to grid URL, enable parallel if desired
          def gridUrl = env.GRID_URL ?: 'http://localhost:4444'
          def mvnCmd = "mvn -B clean test -Dselenium.grid.enabled=true -Dselenium.grid.url=${gridUrl} -Dapp.parallel.enabled=true"

          if (isUnix()) {
            sh mvnCmd
          } else {
            bat mvnCmd
          }
        }
      }
    }

    stage('Publish Allure Report') {
      steps {
        // Requires Allure Jenkins Plugin installed
        allure includeProperties: false, results: [[path: 'target/allure-results']]
      }
    }
  }

  post {
    always {
      // publish sure-fire / junit results and archive allure results
      junit 'target/surefire-reports/*.xml'
      archiveArtifacts artifacts: 'target/allure-results/**', allowEmptyArchive: true
    }
  }
}

