pipeline {
    agent any
    options {
        buildDiscarder(logRotator(
            numToKeepStr: '30',          // Keep the last 30 builds
            artifactNumToKeepStr: '30',  // Keep artifacts for the last 30 builds
            daysToKeepStr: '30'          // Keep build logs for 30 days
        ))
    }

    environment {
        AWS_REGION = 'ap-southeast-1'
        ECR_REPO = 'chatai/auth'
        AWS_ACCOUNT_ID = '767397758009'
        URL_REGISTRY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
        EC2_USER = 'ec2-user'  // Replace with your EC2 username
        EC2_HOST = '122.248.225.23'  // Replace with your EC2 host
        DOCKER_COMPOSE_FILE = '/home/chatai/docker/auth/docker-compose.yaml'  // Replace with the path to your docker-compose.yml on the EC2 VM
    }

    stages {

        stage('Build and Push Docker Image') {
            steps {
                script {
                   withCredentials([[
                        $class: 'AmazonWebServicesCredentialsBinding',
                        accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                        secretKeyVariable: 'AWS_SECRET_ACCESS_KEY',
                        credentialsId: 'Aws-ecr-credentials'
                    ]]) {

                        // Run Gradle clean and bootJar to prepare the JAR file
                        sh "./gradlew clean bootJar"
                        // Login to ECR
                        sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${URL_REGISTRY}"

                        // Get the Git commit ID
                        COMMIT_ID = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()


                        sh "docker build -t ${URL_REGISTRY}/${ECR_REPO}:${COMMIT_ID} ."

                        // Push Docker image to ECR
                        sh "docker push ${URL_REGISTRY}/${ECR_REPO}:${COMMIT_ID}"
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    sshagent(['chatai-ssh-key']) {

                        // Update the docker-compose.yml file on the EC2 VM with the new tag
                        sh """
                            ssh ${EC2_USER}@${EC2_HOST} 'sudo sed -i "s|${URL_REGISTRY}/${ECR_REPO}:.*|${URL_REGISTRY}/${ECR_REPO}:${COMMIT_ID}|" ${DOCKER_COMPOSE_FILE}'
                        """

                        //login docker in des
                        sh """
                            ssh ${EC2_USER}@${EC2_HOST} 'aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${URL_REGISTRY}'
                        """
                        //Restart docker-compose
                        sh """
                            ssh ${EC2_USER}@${EC2_HOST} 'docker-compose -f ${DOCKER_COMPOSE_FILE} down'
                            ssh ${EC2_USER}@${EC2_HOST} 'docker-compose -f ${DOCKER_COMPOSE_FILE} up -d'
                        """

                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}