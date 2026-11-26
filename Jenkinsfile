pipeline {
    agent any

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
                    def mvnHome = tool 'Maven3'
                    withEnv(["PATH+MAVEN=${mvnHome}\\bin"]) {
                        if (params.TEST_TAG == 'all') {
                            bat 'mvn clean test'
                        } else {
                            bat "mvn clean test -Dgroups=${params.TEST_TAG}"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            // Стандартный отчет JUnit (опционально)
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'

            // Генерация красивого Allure-отчета
            allure([
                includeProperties: false,
                jdk: '',
                commandline: 'Allure',
                results: [[path: 'target/allure-results']]
            ])
        }
    }
}
