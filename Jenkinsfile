pipeline {
    agent any
    environment {
        version = "1"
        buildTag = "${version}.${BUILD_NUMBER}"
        develop_app_name = "develop-criteria-transformer-api"
        staging_app_name = "staging-criteria-transformer-api"
        prod_app_name = "criteria-transformer-api"
    }
    stages{
        stage('Checkout code'){
            steps{
                checkout scm: [
                    $class: 'GitSCM'
                ]               
            }
        }
        stage('Build and Tag Openshift Image Develop'){
            when{
                environment name: 'GIT_BRANCH', value: 'origin/develop'
            }
            steps{
                sh 'echo "${GIT_BRANCH}"'
                openshiftBuild(namespace:'criteria-transformer-api', bldCfg: "${develop_app_name}", showBuildLogs: 'true')
                openshiftTag(namespace:'criteria-transformer-api', srcStream: "${develop_app_name}", srcTag: 'latest', destStream: "${develop_app_name}", destTag:'${buildTag}')
            }
        }
        stage('Build and Tag Openshift Image Prod'){
            when{
                environment name: 'GIT_BRANCH', value: 'origin/master'
            }
            steps{
                sh 'echo "${GIT_BRANCH}"'
                openshiftBuild(namespace:'criteria-transformer-api', bldCfg: "${prod_app_name}", showBuildLogs: 'true')
                openshiftTag(namespace:'criteria-transformer-api', srcStream: "${prod_app_name}", srcTag: 'latest', destStream: "${prod_app_name}", destTag:'${buildTag}')
            }
        }
        stage('Deploy to Develop'){
            when{
                environment name: 'GIT_BRANCH', value: 'origin/develop'
            }
            steps{
                sh "oc set image dc/${develop_app_name} ${develop_app_name}=docker-registry.default.svc:5000/criteria-transformer-api/${develop_app_name}:${buildTag} -n criteria-transformer-api"
                openshiftDeploy(depCfg: "${develop_app_name}", namespace: 'criteria-transformer-api', verbose: 'false', waitTime: '', waitUnit: 'sec')
                openshiftVerifyDeployment(depCfg: "${develop_app_name}", namespace:'criteria-transformer-api', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'false', waitTime: '15', waitUnit: 'sec')
            }
        }
        stage('Deploy to Staging'){
            when{
                environment name: 'GIT_BRANCH', value: 'origin/master'
            }
            steps{
                sh "oc set image dc/${staging_app_name} ${staging_app_name}=docker-registry.default.svc:5000/criteria-transformer-api/${prod_app_name}:${buildTag} -n criteria-transformer-api"
                openshiftDeploy(depCfg: "${staging_app_name}", namespace: 'criteria-transformer-api', verbose: 'false', waitTime: '', waitUnit: 'sec')
                openshiftVerifyDeployment(depCfg: "${staging_app_name}", namespace:'criteria-transformer-api', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'false', waitTime: '15', waitUnit: 'sec')
            }
        }
        stage('Deploy to Prod?'){
            when{
                environment name: 'GIT_BRANCH', value: 'origin/master'
            }
            steps{
                input "Deploy to prod?"
            }
        }
        stage('Final Deploy to api'){
            when{
                environment name: 'GIT_BRANCH', value: 'origin/master'
            }
            steps{
                sh "oc set image dc/${prod_app_name} ${prod_app_name}=docker-registry.default.svc:5000/criteria-transformer-api/${prod_app_name}:${buildTag} -n criteria-transformer-api"
                openshiftDeploy(depCfg: "${prod_app_name}", namespace: 'criteria-transformer-api', verbose: 'false', waitTime: '', waitUnit: 'sec')
                openshiftVerifyDeployment(depCfg: "${prod_app_name}", namespace:'criteria-transformer-api', replicaCount: '1', verbose: 'false', verifyReplicaCount: 'false', waitTime: '15', waitUnit: 'sec')
            }
        }
    }
    post {
        success {
            slackSend color: 'good', message: "${GIT_URL}, Branch: ${GIT_BRANCH}, Commit: ${GIT_COMMIT} successfully built to criteria-transformer-api build: ${buildTag}."
        }
        failure {
            slackSend color: 'FF0000', channel: '#narval-sokapi', message: "${GIT_URL} ${GIT_BRANCH} ${GIT_COMMIT} failed to build to criteria-transformer-api build ${buildTag}."
        }
        unstable {
            slackSend color: 'FFFF00', message: "${GIT_URL} ${GIT_BRANCH} ${GIT_COMMIT} unstable build for criteria-transformer-api build ${buildTag}."
        }
    }
}

