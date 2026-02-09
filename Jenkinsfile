pipeline {
    agent any

    environment {
        AWS_REGION   = "ap-south-2"
        ACCOUNT_ID   = "846345203699"   // ✅ your AWS account ID
        ECR_REGISTRY = "${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        BACKEND_REPO = "backend"
        FRONTEND_REPO = "frontend"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/LOKESH734/Real_WORLD_JOB.git'
            }
        }

        stage('Set Image Tag') {
            steps {
                script {
                    env.GIT_COMMIT_SHORT = bat(
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()
                }
            }
        }

        stage('Verify Tools') {
            steps {
                bat '''
                  echo ==== TOOL CHECK ====
                  docker --version
                  aws --version
                  git --version
                '''
            }
        }

        stage('AWS Identity Check') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds'
                ]]) {
                    bat "aws sts get-caller-identity --region %AWS_REGION%"
                }
            }
        }

        stage('Login to ECR') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds'
                ]]) {
                    bat '''
                      aws ecr get-login-password --region %AWS_REGION% ^
                      | docker login --username AWS --password-stdin %ECR_REGISTRY%
                    '''
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    bat "docker build -t backend:${env.GIT_COMMIT_SHORT} backend"
                    bat "docker build -t frontend:${env.GIT_COMMIT_SHORT} frontend"
                }
            }
        }

        stage('Tag & Push to ECR') {
            steps {
                script {
                    bat "docker tag backend:${env.GIT_COMMIT_SHORT} ${env.ECR_REGISTRY}/backend:${env.GIT_COMMIT_SHORT}"
                    bat "docker tag frontend:${env.GIT_COMMIT_SHORT} ${env.ECR_REGISTRY}/frontend:${env.GIT_COMMIT_SHORT}"

                    bat "docker push ${env.ECR_REGISTRY}/backend:${env.GIT_COMMIT_SHORT}"
                    bat "docker push ${env.ECR_REGISTRY}/frontend:${env.GIT_COMMIT_SHORT}"
                }
            }
        }
    }

    post {
        success {
            echo "✅ CI SUCCESS: Docker images built and pushed to AWS ECR"
        }
        failure {
            echo "❌ CI FAILED"
        }
    }
}
