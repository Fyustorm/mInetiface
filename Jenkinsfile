pipeline {
    agent any

    tools {
        maven 'maven-3.9.6'
        jdk 'jdk21'
    }

    environment {
        NEXUS_VERSION = 'nexus3'
        NEXUS_PROTOCOL = 'http'
        NEXUS_URL = '192.168.1.16:10288'
        NEXUS_REPOSITORY = 'maven-nexus-repo'
        NEXUS_CREDENTIAL_ID = 'nexus-credentials'
    }

    stages {
        stage('Gradle build') {
            steps {
                script {
                    props = readProperties file: 'gradle.properties'

                    // Read properties
                    mcVer = props['minecraft_version']
                    modVer = props['mod_version']
                    MOD_VERSION = "${mcVer}-${modVer}"
                }

                sh './gradlew build'
            }
        }

        stage('Publish to Nexus') {
            steps {
                script {
                    // Fabric
                    nexusArtifactUploader(
                        nexusVersion: NEXUS_VERSION,
                        protocol: NEXUS_PROTOCOL,
                        nexusUrl: NEXUS_URL,
                        groupId: 'fr.fyustorm.minetiface',
                        version: "${MOD_VERSION}",
                        repository: NEXUS_REPOSITORY,
                        credentialsId: NEXUS_CREDENTIAL_ID,
                        artifacts: [
                            [
                                artifactId: 'fr.fyustorm.minetiface-fabric',
                                classifier: '',
                                file: "fabric/build/libs/minetiface-fabric_${MOD_VERSION}.jar",
                                type: 'jar'
                            ],
                        ]
                    )

                    // Forge
                    nexusArtifactUploader(
                        nexusVersion: NEXUS_VERSION,
                        protocol: NEXUS_PROTOCOL,
                        nexusUrl: NEXUS_URL,
                        groupId: 'fr.fyustorm.minetiface',
                        version: "${MOD_VERSION}",
                        repository: NEXUS_REPOSITORY,
                        credentialsId: NEXUS_CREDENTIAL_ID,
                        artifacts: [
                            [
                                artifactId: 'fr.fyustorm.minetiface-forge',
                                classifier: '',
                                file: "forge/build/libs/minetiface-forge_${MOD_VERSION}.jar",
                                type: 'jar'
                            ],
                        ]
                    )
                }
            }
        }
    }
}
