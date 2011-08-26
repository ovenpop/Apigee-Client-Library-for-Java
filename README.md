This java library makes it easy to work with the OAuth API from Apigee.

## Notes ##
1. The libraries required to run in lib dir. Normally this should be built by a maven script for example (this is a TODO). So while all required jars are present in lib dir, they will be removed later. 

2. There are a couple of sample scripts - (windows bat files - had some problem in shell scripts):
    a> create app user
    b> make a twitter /trends.json request
    
## Instructions to run ##

a. Review the [sample scripts](https://github.com/apigee/Apigee-Client-Library-for-Java/tree/master/sdk/oauth/src/main/sample-examples)

b. Add/modify the .java files for different input. For different request: 
* create a new java file similar to GetTwitterTrends.java.
*  modify compile and run scripts accordingly.
* run the compilation script
* then run the script