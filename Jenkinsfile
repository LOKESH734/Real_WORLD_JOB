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
                    // Linux version to get short commit hash
                    env.IMAGE_TAG = sh(
                        script: "git rev-parse --short HEAD",
                        returnStdout: true
                    ).trim()

                    echo "IMAGE_TAG = ${env.IMAGE_TAG}"
                }
            }
        }

        stage('Verify Tools') {
            steps {
                sh 'docker --version'
                sh 'aws --version'
                sh 'git --version'
            }
        }

        stage('AWS Identity Check') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds'
                ]]) {
                    sh "aws sts get-caller-identity --region ${AWS_REGION}"
                }
            }
        }

        stage('Login to ECR') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds'
                ]]) {
                    sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}"
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh "docker build -t backend:${env.IMAGE_TAG} realWordJob"
                sh "docker build -t frontend:${env.IMAGE_TAG} job-portal-frontend69"
            }
        }

        stage('Tag & Push to ECR') {
            steps {
                sh "docker tag backend:${env.IMAGE_TAG} ${ECR_REGISTRY}/backend:${env.IMAGE_TAG}"
                sh "docker tag frontend:${env.IMAGE_TAG} ${ECR_REGISTRY}/frontend:${env.IMAGE_TAG}"

                sh "docker push ${ECR_REGISTRY}/backend:${env.IMAGE_TAG}"
                sh "docker push ${ECR_REGISTRY}/frontend:${env.IMAGE_TAG}"
            }
        }
    }

    post {
        success {
            echo "✅ CI SUCCESS – Images built & pushed to AWS ECR"
        }
        failure {
            echo "❌ CI FAILED"
        }
    }
}
