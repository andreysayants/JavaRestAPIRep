pipeline {
    agent any

    // Jenkins сам скачает Maven3 при первом запуске и добавит его в PATH
    tools {
        maven 'Maven3'
    }

    parameters {
        choice(
            name: 'TEST_TAG',
            choices: ['smoke', 'regress', 'all'],
            description: 'Выберите тег для запуска тестов'
        )
    }

    stages {
        stage('Run Tests') {
            steps {
                script {
                    if (params.TEST_TAG == 'all') {
                        bat 'mvn clean test'
                    } else {
                        bat "mvn clean test -Dgroups=${params.TEST_TAG}"
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
    }
}
