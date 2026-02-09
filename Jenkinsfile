pipeline {
    agent any

    tools {
        maven 'maven-3.9'
        nodejs 'node-18'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/your-org/realworldjob.git'
            }
        }

        stage('Backend Build') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Frontend Build') {
            steps {
                dir('frontend') {
                    sh '''
                      npm ci
                      npm run build
                    '''
                }
            }
        }

        stage('Docker Build Images') {
            steps {
                sh '''
                  docker build -t backend:local backend/
                  docker build -t frontend:local frontend/
                '''
            }
        }

        stage('Load Images into Kubernetes') {
            steps {
                sh '''
                  minikube image load backend:local || true
                  minikube image load frontend:local || true
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                  kubectl apply -f k8s/
                '''
            }
        }

        stage('Setup Monitoring') {
            steps {
                sh '''
                  kubectl apply -f monitoring/prometheus.yaml
                  kubectl apply -f monitoring/grafana.yaml
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                  kubectl get pods
                  kubectl get svc
                '''
            }
        }
    }

    post {
        success {
            echo "✅ LOCAL CI/CD PIPELINE COMPLETED SUCCESSFULLY!"
        }
        failure {
            echo "❌ PIPELINE FAILED – CHECK LOGS"
        }
    }
}
