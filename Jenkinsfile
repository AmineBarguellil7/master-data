pipeline {
    agent any

    environment {
        KUBECONFIG = credentials('kubeconfig')
        registry = "aminebarguellil/master-data"
        DOCKER_IMAGE_TAG = "backend"
        registryCredential = "DockerHub-Credentials"
    }

    tools {
        maven "M3"
    }

    stages {
        stage('GIT') {
            steps {
                dir('projectRepo') {
                    echo "Getting Project from Git"
                    git branch: 'Amine',
                        url: 'https://github.com/zoubeirkaouech/master-data.git',
                        credentialsId: 'Git-Credentials'
                }
            }
        }

        stage('Unit Testing') {
            steps {
                dir('projectRepo') {
                    bat "mvn clean test"
                }
            }
        }

        stage('Integration Testing') {
            steps {
                dir('projectRepo') {
                    bat "mvn clean test -Dtest=com/scheidbachmann/masterdata/controller/*IT"
                }
            }
        }

        stage('Build Package') {
            steps {
                dir('projectRepo') {
                    bat "mvn clean package"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('projectRepo') {
                    bat "docker build -t ${registry}:${DOCKER_IMAGE_TAG} ."
                }
            }
        }

        stage('Deploy Image to Docker Hub') {
            steps {
                script {
                    dir('projectRepo') {
                        withCredentials([usernamePassword(credentialsId: registryCredential, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                            bat "docker login -u %DOCKER_USERNAME% -p %DOCKER_PASSWORD%"
                            bat "docker push ${registry}:${DOCKER_IMAGE_TAG}"
                        }
                    }
                }
            }
        }

        stage('K8s-Git') {
            steps {
                dir('k8sRepo') {
                    git branch: 'master',
                        url: 'https://github.com/AmineBarguellil7/masterData-K8s.git',
                        credentialsId: 'K8s-Credentials'
                }
            }
        }

        stage('Delete Existing Kubernetes Deployment') {
            steps {
                script {
                    dir('k8sRepo') {
                        bat 'helm uninstall keycloak-chart'
                        bat 'kubectl scale deployment master-data-frontend --replicas=0'
                        bat 'kubectl scale deployment master-data-backend --replicas=0'
                        bat 'kubectl scale deployment kafka-ui --replicas=0'
                        bat 'kubectl scale deployment postgres-deployment --replicas=0'
                        bat 'kubectl scale statefulset kafka --replicas=0'	
                        bat 'kubectl scale statefulset zookeeper --replicas=0'
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    dir('k8sRepo') {
                        bat 'helm install keycloak-chart ./keycloak-chart'
                        bat 'kubectl apply -f zookeeper.yaml'
                        bat 'kubectl apply -f kafka.yaml'
                        bat 'kubectl apply -f kafka-ui.yaml'
                        bat 'kubectl apply -f postgres.yaml'
                        bat 'kubectl apply -f postgres-config.yaml'
                        bat 'kubectl apply -f postgres-secret.yaml'
                        bat 'kubectl apply -f spring-keycloak-config.yaml'
                        bat 'kubectl apply -f backend.yaml'
                        bat 'kubectl apply -f frontend.yaml'
                    }
                }
            }
        }
    }
}
