addressServiceImageTag = ''

pipeline {
  agent {
    kubernetes {
      yaml """
apiVersion: v1
kind: Pod
spec:
  serviceAccountName: deploy-user
  securityContext:
    fsGroup: 1000
  containers:
  - name: maven
    image: maven:3.6.3-jdk-8
    command:
    - sleep
    args:
    - infinity
    resources:
      requests:
        cpu: "0.5"
        memory: 512Mi
      limits:
        cpu: "1"
        memory: 1Gi  
    volumeMounts:
      - name: dockersock
        mountPath: "/var/run/docker.sock"
  - name: helmfile
    image: quay.io/roboll/helmfile:helm3-v0.125.0
    command:
    - sleep
    args:
    - infinity
    resources:
      limits:
        cpu: "0.5"
        memory: 256Mi
  volumes:
    - name: dockersock
      hostPath:
        path: /var/run/docker.sock
"""
    }
  }
  stages {
    stage('Build') {
      environment {
        BUILD_DOCKER = true
        CONTAINER_REGISTRY='docker-registry:5000'
      }
      steps {
        git 'https://github.com/alexcheng1982/happyride'
        container('maven') {
          sh 'mvn -B -ntp -Dmaven.test.failure.ignore install'
          junit '**/target/surefire-reports/TEST-*.xml'
          script {
            addressServiceImageTag = readFile("happyride-address-service/target/image_tag.txt")
          }
        }
      }
    }
    stage('Deploy') {
      environment {
        ADDRESS_SERVICE_VERSION = "${addressServiceImageTag}"
        CONTAINER_REGISTRY = "localhost:30000"
      }
      steps {
        git 'https://github.com/alexcheng1982/happyride'
        container('helmfile') {
          sh 'cd k8s/happyride/apps/address-service && helmfile apply'
        }
      }
    }
  }
}