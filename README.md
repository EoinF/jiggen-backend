#Instructions for compiling and deploying this app

These are instructions for getting the web app up and running.
Using docker is the next step, but for now it is all done manually.

####Compiling the WAR file

Simply run

        ./gradlew war

The war file is generated in:

        build/libs

####Installing on AWS EC2 instance

1. On AWS launch the basic 64 bit amazon AMI
2. Connect using:

        ssh -i '/path/to/file.pem' <user>@<ip-address>

3. Install jre 1.8
    Download rpm file from oracle site using wget.
    Then run the command:
    
        sudo yum localinstall jre~~.rpm

4. Install tomcat8

        sudo yum install tomcat8
        
5. Add the war file to tomcat8 in directory:

        /usr/share/tomcat8/webapps
        
    NOTE: It automatically unpackages the war file and creates a directory using 
    the name of the war file

6. Install mysql-server

7. Create the database with an sql user:
        
        mysql> create database db_jiggen;
        mysql> create user 'springuser'@'localhost' identified by 'ThePassword';
        mysql> grant all on db_jiggen.* to 'springuser'@'localhost';

8. Revoke permissions on the sql user, to protect against attacks

        mysql> revoke all on db_jiggen.* from 'springuser'@'localhost';
        mysql> grant select, insert, delete, update on db_jiggen.*

9. Inside webapps/jiggen-backend/WEB-INF/classes/application.properties, change the following line:

        spring.jpa.hibernate.ddl-auto=create
    
    to
    
        spring.jpa.hibernate.ddl-auto=none
        
    This prevents hibernate from generating more tables
    
    
## Extra Notes

The default application.properties file is volatile, as it resides inside the webapps directory.
Therefore, the app looks in the following location for a spring configuration file:
    
    /etc/config/jiggen/application.properties
    
This ensures configuration persists between deployments
