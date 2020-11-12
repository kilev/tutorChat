pipeline {
    agent any

    triggers {
        pollSCM '* * * * *'
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew --info assemble'
            }
        }
        stage('Build Docker image') {
            steps {
                sh './gradlew --info docker'
            }
        }
        stage('Push Docker image') {
            environment {
                DOCKER_HUB_LOGIN = credentials('docker-hub')
            }
            steps {
                sh 'docker login --username=$DOCKER_HUB_LOGIN_USR --password=$DOCKER_HUB_LOGIN_PSW'
                sh './gradlew --info dockerPush'
            }
        }
        stage('Start Docker container') {
            steps {
                sh './gradlew --info dockerStop'
                sh './gradlew --info dockerRun'
            }
        }
    }
}
