This java library makes it easy to work with the OAuth API from Apigee.

## Notes ##
1. The libraries required to run in lib dir. Please check below for building your changes. 

2. There are a few sample scripts 
    a> create app user
    b> Get the Auth URL
    c> make a twitter /trends.json request
    
## Instructions to run ##

a. Review the [sample scripts](https://github.com/apigee/Apigee-Client-Library-for-Java/tree/master/src/main/sample-examples)

b. Add/modify the .java files for different input. For different request: 

* Create a new java file similar to GetTwitterTrends.java.

* Modify compile and run scripts accordingly.

* Run the compilation script

* Then run the script

## Building the source ##

a. Please make sure you have maven installed.

b. Run the command - mvn clean install at the root level (where pom.xml exists)

c. The required jars are in target and target/lib.
