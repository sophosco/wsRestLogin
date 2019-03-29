pipeline {
    agent {
        kubernetes {
            //cloud 'kubernetes'
            label 'maven'
            containerTemplate {
                name 'maven'
                image 'maven:3.3.9-jdk-8-alpine'
                ttyEnabled true
                command 'cat'
            }
        }
  }

    environment {
        PROJECT      = 'sophosstore'
        SERVICENAME  = 'wsrestpedido'
        AWS_REGION   = 'us-east-2'
        REGISTRY_URL = "https://887482798966.dkr.ecr.${AWS_REGION}.amazonaws.com"
        IMAGEVERSION = 'beta'
        NAMESPACE    = 'dev'
        IMAGETAG     = "${PROJECT}/${SERVICENAME}:${IMAGEVERSION}${env.BUILD_NUMBER}"
    }

    stages {

        stage('Initialize'){
            steps {
                script {
                    def dockerHome = tool 'docker'
                    env.PATH = "${dockerHome}/bin:${env.PATH}"
                }
            }
        }

        // stage 1: Checkout code from git
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // stage 2: Build application
        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        // stage 3: Test application
        stage('Test') {
            steps {
                sh 'mvn clean test'
            }
            post {
                always {
                    junit '**/target/*-reports/TEST-*.xml'
                }
            }
        }

        // stage 4: Build the docker image and push to ECR
        stage('Build docker image and push to registry') {
            steps {
                script {

                    docker.withRegistry("${REGISTRY_URL}", "ecr:us-east-2:aws") {
                        docker.image("your-image-name").push()

                        //build image
                        def ecrImage = docker.build("${IMAGETAG}")
                        
                        //push image
                        ecrImage.push()
                    }
                }
            }
        }

        // stage 5: Deploy application
        stage('Deploy application') {
            steps {
                sh "kubectl get ns ${NAMESPACE} || kubectl create ns ${NAMESPACE}"
                sh "sed -i.bak 's#${PROJECT}/${SERVICENAME}:${IMAGEVERSION}#${IMAGETAG}#' ./k8s/dev/*.yaml"
                sh "kubectl --namespace=${NAMESPACE} apply -f k8s/dev/deployment.yaml"
                sh "kubectl --namespace=${NAMESPACE} apply -f k8s/dev/service.yaml"
                sh "echo http://`kubectl --namespace=${NAMESPACE} get service/${SERVICENAME} --output=json | jq -r '.status.loadBalancer.ingress[0].ip'` > ${SERVICENAME}"
            }
        }

    }

}