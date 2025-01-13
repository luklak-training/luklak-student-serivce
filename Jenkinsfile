pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub-thanh5320'
        DOCKER_IMAGE = 'luklak-test/service-app'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Determine Version') {
            steps {
                script {
                    // Tạo tag phiên bản dựa trên số build
                    VERSION = "${env.BUILD_NUMBER}" // Số build tự động của Jenkins
                    echo "Image version: ${VERSION}"
                }
            }
        }

        stage('Build with Maven') {
            steps {
                echo 'Building project with Maven...'
                sh 'mvn clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t ${DOCKER_IMAGE}:${VERSION} .'
            }
        }

        stage('Login to Docker Hub') {
            steps {
                echo 'Logging in to Docker Hub...'
                withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS,
                                                 usernameVariable: 'DOCKER_USERNAME',
                                                 passwordVariable: 'DOCKER_PASSWORD')]) {
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                sh 'docker push ${DOCKER_IMAGE}:${VERSION}'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up Docker images...'
            sh 'docker rmi ${DOCKER_IMAGE}:${VERSION} || true'
        }

        success {
            echo 'Pipeline completed successfully. Image version: ${VERSION}'
        }

        failure {
            echo 'Pipeline failed.'
        }
    }
}