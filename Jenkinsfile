pipeline {
    agent {
        node {
            // spin up a maven slave pod to run this build on
            label 'maven'
        }
    }

    environment {
        PROJECT      = 'sophosstore'
        SERVICENAME  = 'wsrestpedido'
        AWS_REGION   = 'us-east-2'
        REGISTRY_URL = "https://887482798966.dkr.ecr.${AWS_REGION}.amazonaws.com/poc-sophos"
        switch(env.BRANCH_NAME.toLowerCase()) {
            case "master":
                IMAGEVERSION = 'latest'
                NAMESPACE    = 'prd'
                IMAGETAG     = "${PROJECT}/${SERVICENAME}:${IMAGEVERSION}"
                break
            case "release":
                IMAGEVERSION = 'rc'
                NAMESPACE    = 'stg'
                IMAGETAG     = "${PROJECT}/${SERVICENAME}:${IMAGEVERSION}${env.BUILD_NUMBER}"
                break
            default:
                IMAGEVERSION = 'beta'
                NAMESPACE    = 'dev'
                IMAGETAG     = "${PROJECT}/${SERVICENAME}:${IMAGEVERSION}${env.BUILD_NUMBER}"
                break
        }
    }

    stages {

        // stage 1: Checkout code from git
        stage('Checkout') {
            checkout scm
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

        // stage 4: Build the docker image
        stage('Build docker image') {
            steps {
                sh "docker build ${IMAGETAG}"
            }
        }

        // stage 5: Push image to docker registry
        stage('Push image to registry') {
            steps {
                echo "Connect to registry at ${REGISTRY_URL}"
                login_command = sh(returnStdout: true,
                    script: "aws ecr get-login --region ${AWS_REGION} | sed -e 's|-e none||g'"
                )
                sh "${login_command}"
                echo "Register ${IMAGETAG} at ${REGISTRY_URL}"
                sh "docker -- push ${IMAGETAG}"
                echo "Disconnect from registry at ${REGISTRY_URL}"
                sh "docker logout ${REGISTRY_URL}"
            }
        }

        // stage 6: Deploy application
        stage('Deploy application') {
            steps {
                switch(${NAMESPACE}) {
                    // rollout to staging environment
                    case "stg":
                        // create namespace if it doesn't exist
                        sh "kubectl get ns ${NAMESPACE} || kubectl create ns ${NAMESPACE}"
                        // update the IMAGETAG to the latest version
                        sh "sed -i.bak 's#${PROJECT}/${SERVICENAME}:${IMAGEVERSION}#${IMAGETAG}#' ./k8s/stg/*.yaml"
                        // create or update resources
                        sh "kubectl --namespace=${NAMESPACE} apply -f k8s/stg/deployment.yaml"
                        sh "kubectl --namespace=${NAMESPACE} apply -f k8s/stg/service.yaml"
                        // grab the external Ip address of the service
                        sh "echo http://`kubectl --namespace=${NAMESPACE} get service/${SERVICENAME} --output=json | jq -r '.status.loadBalancer.ingress[0].ip'` > ${SERVICENAME}"
                        break
                    // rollout to production environment
                    case "prd":
                        // create namespace if it doesn't exist
                        sh "kubectl get ns ${NAMESPACE} || kubectl create ns ${NAMESPACE}"
                        // update the IMAGETAG to the latest version
                        sh "sed -i.bak 's#${PROJECT}/${SERVICENAME}:${IMAGEVERSION}#${IMAGETAG}#' ./k8s/prd/*.yaml"
                        // create or update resources
                        sh "kubectl --namespace=${NAMESPACE} apply -f k8s/prd/deployment.yaml"
                        sh "kubectl --namespace=${NAMESPACE} apply -f k8s/prd/service.yaml"
                        // grab the external Ip address of the service
                        sh "echo http://`kubectl --namespace=${NAMESPACE} get service/${SERVICENAME} --output=json | jq -r '.status.loadBalancer.ingress[0].ip'` > ${SERVICENAME}"
                        break
                    default:
                        sh "kubectl get ns ${NAMESPACE} || kubectl create ns ${NAMESPACE}"
                        sh "sed -i.bak 's#${PROJECT}/${SERVICENAME}:${IMAGEVERSION}#${IMAGETAG}#' ./k8s/dev/*.yaml"
                        sh "kubectl --namespace=${NAMESPACE} apply -f k8s/dev/deployment.yaml"
                        sh "kubectl --namespace=${NAMESPACE} apply -f k8s/dev/service.yaml"
                        sh "echo http://`kubectl --namespace=${NAMESPACE} get service/${SERVICENAME} --output=json | jq -r '.status.loadBalancer.ingress[0].ip'` > ${SERVICENAME}"
                        break
                }
            }
        }

    }

}