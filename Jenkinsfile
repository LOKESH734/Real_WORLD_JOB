pipeline {
    agent any

    environment {
        AWS_REGION   = "ap-south-2"
        ACCOUNT_ID   = "846345203699"
        ECR_REGISTRY = "${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
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
                    env.IMAGE_TAG = bat(
                        script: '''
@echo off
for /f %%i in ('git rev-parse --short HEAD') do @echo %%i
''',
                        returnStdout: true
                    ).trim()

                    echo "IMAGE_TAG = ${env.IMAGE_TAG}"
                }
            }
        }

        stage('Verify Tools') {
            steps {
                bat 'docker --version'
                bat 'aws --version'
                bat 'git --version'
            }
        }

        stage('AWS Identity Check') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds'
                ]]) {
                    bat "aws sts get-caller-identity --region ${AWS_REGION}"
                }
            }
        }

        stage('Login to ECR') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds'
                ]]) {
                    bat "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}"
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                bat "docker build -t backend:${env.IMAGE_TAG} backend"
                bat "docker build -t frontend:${env.IMAGE_TAG} frontend"
            }
        }

        stage('Tag & Push to ECR') {
            steps {
                bat "docker tag backend:${env.IMAGE_TAG} ${ECR_REGISTRY}/backend:${env.IMAGE_TAG}"
                bat "docker tag frontend:${env.IMAGE_TAG} ${ECR_REGISTRY}/frontend:${env.IMAGE_TAG}"

                bat "docker push ${ECR_REGISTRY}/backend:${env.IMAGE_TAG}"
                bat "docker push ${ECR_REGISTRY}/frontend:${env.IMAGE_TAG}"
            }
        }
    }

    post {
        success {
            echo "✅ CI SUCCESS – Docker images built & pushed to AWS ECR"
        }
        failure {
            echo "❌ CI FAILED"
        }
    }
}
