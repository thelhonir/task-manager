# TASK MANAGER

## Spring boot version

> Java version: **11** 

> Spring boot version: **2.3.0.RELEASE**
 

### Run the application with Maven
-----

`mvn spring-boot:run`



### Run the application on a docker container
-----

Build the docker image with the jar, the Dockerfile on this case is simple and quite raw.

`docker build -t thelhonir/task-manager .`

To run the image in a container at 8080

`docker run -p 8080:8080 thelhonir/task-manager`

### Consume the endpoints
----

To facilitate this. an export of an **Insomnia** workspace has been added to the project `Insomnia_2020-06-09.json`.

The application doesn't have any authentication at the moment or any persistence layer.