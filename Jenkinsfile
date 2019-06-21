pipeline {
    agent  { label 'master' }
    tools {
        maven 'Maven 3.6.0'
        jdk 'jdk8'
    }
  environment {
    VERSION="6.9.0"
    GROUP = "NeotysLab"
    APP_NAME="WhiteblockBlockChain"
    DOCKERHUB="hrexed/whitebloclg"
  }

  stages {
      stage('Checkout') {
          agent { label 'master' }
          steps {
               git  url:"https://github.com/${GROUP}/${APP_NAME}.git",
                      branch :'master'
          }
      }
    stage('Docker build') {
        steps {
            sh "sed -i 's/VERSION_TO_REPLACE/${VERSION}/'  $WORKSPACE/docker/Dockerfile"

            withCredentials([usernamePassword(credentialsId: 'dockerHub', passwordVariable: 'TOKEN', usernameVariable: 'USER')]) {
                sh "docker build -t ${DOCKERHUB}:${VERSION} $WORKSPACEE/docker//"
                sh "docker push ${DOCKERHUB}:${VERSION}"
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