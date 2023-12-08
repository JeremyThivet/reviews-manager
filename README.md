# Reviews Manager

## About

A simple Single Page Application built with Spring Boot and React.js. It lets you manage your custom lists of reviews (favorites movies, visited museum, preferred restaurants, ...). You can create your own lists and populate them.

## Live demo

You can access a live demo of the app here: https://revieworld-demo.onrender.com/mesclassements

⚠️ Please note that this demo does not persist data, the first access can be slow due to the hosting provider launching the container for the first incoming request after an idle period.

## Project status

ℹ️ This project was developed for self-education purpose, I wanted to put my theorical knowledge on Spring, React and Devops in practice.

As it is a first version, the codebase is not perfect though, coming back to it after one year, here are some improvements that could be done:

- Moving from a 3 tiers to an hexagonal architecture using the clean architecture principles following Robert Martin's Guidelines
- Rewriting tests to make them clearer (using the Given / When / Then approach for instance)
- Rework the DevOps part to make it easier to deploy


## Installation

### Docker (Auto configuration)

You can use Docker to automatically create a postegresql containing the required schema. Then, it will build the application inside a JRE container and will launch the application inside a lightweight container (alpine linux).

The docker procedure builds an HTTPS version of the application, **Therefore, you must have an HTTPS certificate embedded inside a keystore.** If you need help generating your keystore from your certificate files, you can check https://ordina-jworks.github.io/security/2019/08/14/Using-Lets-Encrypt-Certificates-In-Java.html


You will need last version of Docker.
See : https://docs.docker.com/engine/install/

1) Clone repository


2) Generate your keystore containing your certificate and put it in `api/src/main/resources`. The default name is keystore.p12. If you changed it, do not forget to edit the application.properties : `server.ssl.key-store=classpath:keystore.p12`


3) `./build_docker.sh <postgresql_user> <postgresql_pass> <jwt_secret> <keystore_pass>`
* *<postgresql_user>* : The administrator user of the soon-to-be-created postegresql container.
* *<postgresql_pass>* : The administrator password of the soon-to-be-created postegresql container.
* *<jwt_secret>* : Secret sequence used by the application to produce Java Web Tokens. **Must be at least 64 characters long**
* <keystore_pass> : Password for your keystore file.


4) You can access the application through https://localhost 


### Docker (Manual configuration)

If you want to manually configure the docker instance, you can solely build and run the app container and provide environement variables when you run it.

1) Clone the repository


2) Generate your keystore containing your certificate and put it in `api/src/main/resources`. The default name is keystore.p12. If you changed it, do not forget to edit the application.properties : `server.ssl.key-store=classpath:keystore.p12`


3) From root path of the cloned directory, run `docker build -t reviewsmanager-app` .


4) Launch with `docker run -d -p <you_port>:9000 reviewsmanager-app` **you will need to provide the following environement variable depending on your configuration:**

-  `SPRING_DATASOURCE_URL`: URL to your db server which hosts the revieworld database (*default is jdbc:postgresql://localhost:5432/revieworld*)
-  `SPRING_DATASOURCE_USERNAME`: Your db user which ows the app database
-  `SPRING_DATASOURCE_PASSWORD`: You db user password
-  `APPLICATION_JWT_SECRETKEY`: The secret used for JWT generation, must be at least 64 characters long
-  `SERVER_SSL_KEYSTORE` : Path to keystore (*default is classpath:keystore.p12*)
-  `SERVER_SSL_KEYSTOREPASSWORD`: The keystore password

These are the most common settings to change, you can overwrite other application's parameter that the `application.properties` file contains. For instance, if you want to change the listening port of the application inside the docker container, you can use the following environement variable : `SERVER_PORT

### Manual (Linux)

You will need Java 17 in order to build and run the application.

1) Clone the repository


2) Generate your keystore containing your certificate and put it in `api/src/main/resources`. The default name is keystore.p12. If you changed it, do not forget to edit the application.properties : `server.ssl.key-store=classpath:keystore.p12`


3) Make sure you have a database server which runs the revieworld database.


4) Adapt the `application.properties` file according to your parameters



4) Build the project with `mvn clean install` inside the `api` folder


5) Run with `java -jar api/target/api-<buildversion>.jar`


### Database schema

The file `api/database_scheme.sql.pattern` provides a postegresql export for the database schema.
By default, the table are created for the user `admin`. You can change it by using the following command : `sed "s/ admin/ <you_username>/g" api/database_scheme.sql.pattern > api/database_scheme.sql`

Then, you can use the `api/database_schene.sql` in your custom database to create the application's required database.


### Disable HTTPS

No matter wich method you use, you may want to disable HTTPS/ To do so, you need to edit the `application.properties` file before you build anything.

Delete or comment the following lines :
```
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-type=PKCS12
server.ssl.key-store-password=changeit
server.ssl.key-alias=revieworld.tk
```
