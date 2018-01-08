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

7. 
