# CandyFactoryProject
External libraries needed for the project:

-amqp-client-4.1.0

-gson-2.8.6

-jakarta.activation 3.0

-jakarta.xml.bind-api 3.0

-jaxb-impl 3.0

-json-simple-1.1.1

-javafx-sdk-17.0.6

-apache tomcat 9.0

-slf4j-api-1.7.21

-slf4j-simple-1.7.22

Java edition used in project is JavaSE-11 due to compatiility issues with Tomcat server and newer Java editions.

To successfuly configure this project, it is also needed to install the following:

-Rabbit MQ server 3.12.6

-Erlang OTP 25.0 (required by the MQ server)

-Redis db 3.2.100 (no installation, just extract the zip)

After everything is above is downloaded, installed, jars added to the module paths, steps for starting the projects are below:

-Run the redis-server.exe and rabbitmq-server.bat both as administrator

-Run the CandyFactoryServer project as the "Run on server" (there might be issue with the Tomcat server, it might be needed to access the local server and check if the ports are configured properly)

-Acccess the mdp.candyfactory.secure package in same project and run the SecureServer as the Java application

-In the project CandyFactoryDistributerApp run the mdp.candyfactory.distributer.rmi.DistributerServer as the Java application

-Now you can run each project's Main application (multiple)
