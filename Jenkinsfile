pipeline {
    agent any

    // 1. Создаем параметр для выбора в интерфейсе Jenkins
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
                    // 2. Формируем команду: если выбрано 'all', запускаем всё без фильтрации
                    if (params.TEST_TAG == 'all') {
                        sh 'mvn clean test'
                    } else {
                        sh "mvn clean test -Dgroups=${params.TEST_TAG}"
                    }
                }
            }
        }
    }

    // 3. Собираем стандартный JUnit-отчет после прогона (даже если тесты упали)
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
