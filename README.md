# Instructions for compiling and deploying this app

These are instructions for getting the web app up and running.
Using docker is the next step, but for now it is all done manually.

## Deployment on AWS Elastic Beanstalk

The code is built to be deployed on AWS, but can be used with any container orchestration service, if modified

### Docker Build Image

The definition is inside docker-builder

Running this image in a container will fetch the latest version of jiggen-backend, build it, and create a new docker
image for deploying it.

### Docker Deploy Image

The definition is inside docker-deploy

Running this image in a container will deploy the version of jiggen-backend installed inside it. It uses tomcat to serve
the app

### Dockerrun.aws.json

A configuration used for AWS Elastic beanstalk

Uploading this configuration will fetch the latest image created by docker-deploy and launch a container instance
using it. It will also connect the app to a database



## Deployment on AWS EC2 instance

This is the old method of deployment. It may not work fully anymore

#### Compiling the WAR file for EC2

Simply run

        ./gradlew war

The war file is generated in:

        build/libs

#### Installing on AWS EC2 instance

1. On AWS launch the basic 64 bit amazon AMI
2. Connect using:

        ssh -i '/path/to/file.pem' <user>@<ip-address>

3. Install jre 1.8
    Download rpm file from oracle site using wget.
    Then run the command:
    
        sudo yum localinstall jre~~.rpm

4. Install tomcat8

        sudo yum install tomcat8

5. Create app data folder and set permissions

        sudo mkdir /var/lib/jiggen
        sudo chown ec2-user /var/lib/jiggen

6. Add the war file to tomcat8 in directory:

        /usr/share/tomcat8/webapps
        
    NOTE: It automatically unpackages the war file and creates a directory using 
    the name of the war file

7. Install mysql-server

8. Create the database with an sql user:
        
        mysql> create database db_jiggen;
        mysql> create user 'springuser'@'localhost' identified by 'ThePassword';
        mysql> grant all on db_jiggen.* to 'springuser'@'localhost';

9. Revoke permissions on the sql user, to protect against attacks

        mysql> revoke all on db_jiggen.* from 'springuser'@'localhost';
        mysql> grant select, insert, delete, update on db_jiggen.*

10. Inside webapps/jiggen-backend/WEB-INF/classes/application.properties, change the following line:

        spring.jpa.hibernate.ddl-auto=update
    
    to
    
        spring.jpa.hibernate.ddl-auto=none
        
    This prevents hibernate from generating more tables
    
    
## Extra Notes

The default application.properties file is volatile, as it resides inside the webapps directory.
Therefore, the app looks in the following location for a spring configuration file:
    
    /etc/config/jiggen/application.properties
    
This ensures configuration persists between deployments
