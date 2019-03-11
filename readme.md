BUILD TOOL GRADLE

BUILD
`mvn clean package`

START APPLICATION
`./gradlew bootRun`


DOCKER BUILD IMG
`docker build .`

TAG
`docker build -t criteriatransformerapi .`

LOGIN
`docker login`

TAG BUILD WITH USER
`docker build -t akyse/criteriatransformerapi .`

PUSH IMAGE TO DOCKER HUB
`docker push akyse/criteriatransformerapi`


OPENSHIFT 

LOGIN 
`oc login`

PUSH IMAGE
`oc import-image akyse/criteria-transformer-api`