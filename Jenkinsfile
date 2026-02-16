pipeline {
    agent any

    environment {
        AWS_REGION   = "ap-south-2"
        ACCOUNT_ID   = "846345203699"
        CLUSTER_NAME = "realworld-eks"
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
                sh 'kubectl version --client'
            }
        }

        stage('Authenticate AWS') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-creds'
                ]]) {

                    sh """
                        aws sts get-caller-identity --region ${AWS_REGION}
                        aws eks update-kubeconfig --region ${AWS_REGION} --name ${CLUSTER_NAME}
                    """
                }
            }
        }

        
        stage('Login to ECR') {
    steps {
        withCredentials([[
            $class: 'AmazonWebServicesCredentialsBinding',
            credentialsId: 'aws-creds'
        ]]) {
            sh """
                aws ecr get-login-password --region ${AWS_REGION} \
                | docker login --username AWS --password-stdin ${ECR_REGISTRY}
            """
        }
    }
}


        stage('Build Docker Images') {
            steps {
                sh """
                    docker build -t backend:${IMAGE_TAG} realWordJob
                    docker build -t frontend:${IMAGE_TAG} job-portal-frontend69
                """
            }
        }

        stage('Tag & Push to ECR') {
            steps {
                sh """
                    docker tag backend:${IMAGE_TAG} ${ECR_REGISTRY}/backend:${IMAGE_TAG}
                    docker tag frontend:${IMAGE_TAG} ${ECR_REGISTRY}/frontend:${IMAGE_TAG}

                    docker push ${ECR_REGISTRY}/backend:${IMAGE_TAG}
                    docker push ${ECR_REGISTRY}/frontend:${IMAGE_TAG}
                """
            }
        }

        stage('Deploy to EKS') {
    steps {
        withCredentials([[
            $class: 'AmazonWebServicesCredentialsBinding',
            credentialsId: 'aws-creds'
        ]]) {
            sh """
                aws eks update-kubeconfig --region ${AWS_REGION} --name realworld-eks

                kubectl apply -f k8s/

                kubectl rollout status deployment/backend
                kubectl rollout status deployment/frontend
            """
        }
    }
}



        stage('Verify Rollout') {
            steps {
                sh """
                    kubectl rollout status deployment/backend --timeout=120s
                    kubectl rollout status deployment/frontend --timeout=120s
                """
            }
        }
    }

    post {
        success {
            echo "🚀 CI/CD SUCCESS – App Deployed Successfully"
        }
        failure {
            echo "❌ Deployment Failed – Investigate Immediately"
        }
    }
}
