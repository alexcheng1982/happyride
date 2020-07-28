pipeline {
  agent {
    kubernetes {
      yaml """
apiVersion: v1
kind: Pod
spec:
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
        cpu: 0.5
        memory: 512Mi
      limits:
        cpu: 1
        memory: 1Gi  
    volumeMounts:
      - name: dockersock
        mountPath: "/var/run/docker.sock"
  volumes:
    - name: dockersock
      hostPath:
        path: /var/run/docker.sock
"""
    }
  }
  stages {
    stage('Build') {
      steps {
        git 'https://github.com/alexcheng1982/happyride'
        container('maven') {
          sh 'mvn -B -ntp -Dmaven.test.failure.ignore compile'
          junit '**/target/surefire-reports/TEST-*.xml'
        }
      }
    }
    stage('Publish') {
      steps {
        git 'https://github.com/alexcheng1982/happyride'
        container('maven') {
          withEnv(['BUILD_DOCKER=true', 'PUBLISH_IMAGE=true']) {
            sh 'mvn -B -ntp -DskipTests package'
          }
        }
      }
    }
  }
}