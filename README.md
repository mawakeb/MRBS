# CSE2115 - Project

### Running 
`gradle bootRun`

### Testing
```
gradle test
```

To generate a coverage report:
```
gradle jacocoTestCoverageVerification
```


And
```
gradle jacocoTestReport
```
The coverage report is generated in: build/reports/jacoco/test/html, which does not get pushed to the repo. Open index.html in your browser to see the report. 

### Static analysis
```
gradle checkStyleMain
gradle checkStyleTest
gradle pmdMain
gradle pmdTest
```

### Notes
- You should have a local .gitignore file to make sure that any OS-specific and IDE-specific files do not get pushed to the repo (e.g. .idea). These files do not belong in the .gitignore on the repo.
- If you change the name of the repo to something other than template, you should also edit the build.gradle file.
- You can add issue and merge request templates in the .gitlab folder on your repo. 

### How to Use
- To begin, run (either with IntelliJ or gradle bootRun) the `discovery-server` Spring Application, followed by `user-authentication-service`, `room`, `group` and `reservation`. Then, run `api-gateway`. 
- Make a request to `localhost:8900/user/register` (returning 200 if accepted and 400 if netId already exists) with body:
```
{
    "netId": <your net id>,
    "name": <your name>,
    "password": <your password>
    "type": <type> (USER | SECRETARY | ADMIN)
}
```

- After this, make a request to `localhost:8900/user/login` with body: 
```
{
    "netId": <your net id>,
    "password": <your password>
}
```
- You will receive a response body with a JWT; copy this, and then add it to the header "Authorization", with value "Bearer <your jwt>". This will be needed for all next requests.