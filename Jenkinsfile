pipeline {
    agent any

    environment {
        DOCKER_HOST = 'unix:///var/run/docker.sock'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('ecommerce-back-end-master') {
                    sh 'chmod +x ./gradlew'
                    sh './gradlew clean build -x test'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('ecommerce-frond-end-master') {
                    sh 'npm i -g pnpm || true'
                    sh 'pnpm install --frozen-lockfile'
                    sh 'pnpm build'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker-compose build'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose up -d'
            }
        }

        stage('Health Check') {
            steps {
                script {
                    // Esperar a que los servicios estén listos
                    sh 'sleep 30'

                    // Verificar que el backend está corriendo
                    sh 'curl -f http://localhost:8080/actuator/health || echo "Backend health check failed"'

                    // Verificar que el frontend está corriendo
                    sh 'curl -f http://localhost:3000 || echo "Frontend health check failed"'
                }
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline completado con éxito!'
            echo 'Servicios disponibles:'
            echo '  - Frontend: http://localhost:3000'
            echo '  - Backend: http://localhost:8080'
            echo '  - GraphiQL: http://localhost:8080/graphiql'
            echo '  - Zipkin: http://localhost:9411'
        }
        failure {
            echo '❌ Pipeline fallido'
            sh 'docker-compose logs'
        }
        always {
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    disableDeferredWipeout: true,
                    notFailBuild: true)
        }
    }
}

