podTemplate(
    label: 'slave', 
    cloud: 'kubernetes',
    serviceAccount: 'jenkins',
    containers: [
        containerTemplate(
            name: 'docker', 
            image: 'docker:dind', 
            ttyEnabled: true, 
            alwaysPullImage: true, 
            privileged: true,
            command: 'dockerd --host=unix:///var/run/docker.sock --host=tcp://0.0.0.0:2375 --storage-driver=overlay'
        ),
        containerTemplate(
            name: 'maven',
            image: 'maven:alpine',
            ttyEnabled: true,
            command: 'cat'
        ),
        containerTemplate(
            name: 'kubectl', 
            image: 'lachlanevenson/k8s-kubectl:latest', 
            command: 'cat', 
            ttyEnabled: true
        )
    ],
   volumes: [
       emptyDirVolume(
           memory: false, 
           mountPath: '/var/lib/docker'
        )
    ]
) {
    node('slave') {
        def PROJECT      = 'sophosstore'
        def SERVICENAME  = 'wsrestlogin'
        def AWS_REGION   = 'us-east-2'
        def REGISTRY_URL = "https://887482798966.dkr.ecr.us-east-2.amazonaws.com"
        def IMAGEVERSION = "a${env.BUILD_NUMBER}"
        def NAMESPACE    = 'dev'
        def IMAGETAG     = "$PROJECT/$SERVICENAME:$IMAGEVERSION"

        stage('Checkout code') {
            checkout scm
        }
        
        container('maven') {
            stage('Build app') {
                sh 'mvn package'
            }
            stage('Test app') {
                try {
                    sh 'mvn test'
                } 
                finally {
                    junit '**/target/*-reports/TEST-*.xml'
                }
            }
        }//maven

        container('docker') {
            stage('Create image') {
                docker.withRegistry("$REGISTRY_URL", "ecr:us-east-2:aws") {
                    image = docker.build("$IMAGETAG")
                    image.inside {
                        sh 'ls -alh'
                    }
                    image.push()
                }
            }
        }//docker

        container('kubectl') {
            stage('Deploy image') {
                sh "kubectl get ns $NAMESPACE || kubectl create ns $NAMESPACE"
                sh "kubectl get pods --namespace $NAMESPACE"
                sh "sed -i.bak 's#$PROJECT/$SERVICENAME:$IMAGEVERSION#$IMAGETAG#' ./k8s/dev/*.yaml"
                sh "kubectl --namespace=$NAMESPACE apply -f k8s/dev/deployment.yaml"
                sh "kubectl --namespace=$NAMESPACE apply -f k8s/dev/service.yaml"
                sh '''
                    SERVICE_IP=$(kubectl --namespace=$NAMESPACE get service/$SERVICENAME  --template "{{ range (index .status.loadBalancer.ingress 0) }}{{ . }}{{ end }}")
                    echo http://$SERVICE_IP:18080/
                '''
            }
        }

    }//node
}//podTemplate


// pipeline {
//     agent {
//         kubernetes {
//             label 'jenkins-maven'
//             containerTemplate {
//                 name 'maven'
//                 image 'maven:alpine'
//                 ttyEnabled true
//                 command 'cat'
//                 privileged true
//             }
//         }
//     }
//         environment {
//             PROJECT      = 'sophosstore'
//             SERVICENAME  = 'wsrestpedido'
//             AWS_REGION   = 'us-east-2'
//             REGISTRY_URL = "https://887482798966.dkr.ecr.us-east-2.amazonaws.com/poc-sophos"
//             IMAGEVERSION = 'beta'
//             NAMESPACE    = 'dev'
//             IMAGETAG     = "${PROJECT}/${SERVICENAME}:${IMAGEVERSION}${env.BUILD_NUMBER}"
//         }

//         stages {
//             stage('Initialize'){
//                 steps {
//                     script {
//                         def dockerHome = tool 'docker'
//                         env.PATH = "${dockerHome}/bin:${env.PATH}"
//                     }
//                 }
//             }

//             // stage 1: Checkout code from git
//             stage('Checkout') {
//                 steps {
//                     checkout scm
//                 }
//             }

//             // stage 2: Build application
//             stage('Build') {
//                 steps {
//                     sh 'mvn clean install'
//                 }
//             }

//             // stage 3: Test application
//             stage('Test') {
//                 steps {
//                     sh 'mvn clean test'
//                 }
//                 post {
//                     always {
//                         junit '**/target/*-reports/TEST-*.xml'
//                     }
//                 }
//             }

//             // stage 4: Build the docker image and push to ECR
//             stage('Build docker image and push to registry') {
//                 steps {
//                         script {
//                             echo "Build with server ${env.DOCKER_HOST}"
//                             //docker.withRegistry("${REGISTRY_URL}", "ecr:us-east-2:aws") {
//                             withDockerRegistry("${REGISTRY_URL}", "ecr:us-east-2:aws") {
//                                 //build image
//                                 def dockerImage = docker.build("${IMAGETAG}")
                                
//                                 //push image
//                                 dockerImage.push()
//                             }

//                             /* echo "Connect to registry at ${REGISTRY_URL}"
//                             login_command = sh(returnStdout: true,
//                                 script: "aws ecr get-login --region ${AWS_REGION} | sed -e 's|-e none||g'"
//                             )
//                             sh "${login_command}"
//                             echo "Build ${IMAGETAG}"
//                             sh "docker build -t ${IMAGETAG} ."
//                             echo "Register ${IMAGETAG} at ${REGISTRY_URL}"
//                             sh "docker -- push ${IMAGETAG}"
//                             echo "Disconnect from registry at ${REGISTRY_URL}"
//                             sh "docker logout ${REGISTRY_URL}" */
//                         }
//                     }
                
//             }

//             // stage 5: Deploy application
//             stage('Deploy application') {
//                 steps {
//                     sh "kubectl get ns ${NAMESPACE} || kubectl create ns ${NAMESPACE}"
//                     sh "sed -i.bak 's#${PROJECT}/${SERVICENAME}:${IMAGEVERSION}#${IMAGETAG}#' ./k8s/dev/*.yaml"
//                     sh "kubectl --namespace=${NAMESPACE} apply -f k8s/dev/deployment.yaml"
//                     sh "kubectl --namespace=${NAMESPACE} apply -f k8s/dev/service.yaml"
//                     sh "echo http://`kubectl --namespace=${NAMESPACE} get service/${SERVICENAME} --output=json | jq -r '.status.loadBalancer.ingress[0].ip'` > ${SERVICENAME}"
//                 }
//             }

//         }
    

// }