pipeline {
    agent any

    environment {
        AWS_REGION = "ap-south-1"
        ECR_REGISTRY = "xxxxxxxxxxxx.dkr.ecr.ap-south-1.amazonaws.com"
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
                  docker --version
                  aws --version
                  git --version
                '''
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
                bat '''
                  docker build -t %BACKEND_REPO%:%GIT_COMMIT_SHORT% backend
                  docker build -t %FRONTEND_REPO%:%GIT_COMMIT_SHORT% frontend
                '''
            }
        }

        stage('Tag & Push to ECR') {
            steps {
                bat '''
                  docker tag %BACKEND_REPO%:%GIT_COMMIT_SHORT% ^
                    %ECR_REGISTRY%/%BACKEND_REPO%:%GIT_COMMIT_SHORT%

                  docker tag %FRONTEND_REPO%:%GIT_COMMIT_SHORT% ^
                    %ECR_REGISTRY%/%FRONTEND_REPO%:%GIT_COMMIT_SHORT%

                  docker push %ECR_REGISTRY%/%BACKEND_REPO%:%GIT_COMMIT_SHORT%
                  docker push %ECR_REGISTRY%/%FRONTEND_REPO%:%GIT_COMMIT_SHORT%
                '''
            }
        }
    }

    post {
        success {
            echo "✅ CI SUCCESS: Images pushed to ECR"
        }
        failure {
            echo "❌ CI FAILED"
        }
    }
}
