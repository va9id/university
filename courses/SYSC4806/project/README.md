# 4th Year Project Management System
[![Build and deploy JAR app to Azure Web App - SYSC4806Project](https://github.com/ConnorMarcus/SYSC4806Project/actions/workflows/main_sysc4806project.yml/badge.svg)](https://github.com/ConnorMarcus/SYSC4806Project/actions/workflows/main_sysc4806project.yml)

SYSC 4806 Group Project

## About
We have designed a 4th Year Project Management System for Engineering students. Our service allows students to create an account and join any available 4th year engineering project. Professors can create 4th year projects for students and can help manage them. 

## Table of Contents
- [About](#about)
- [Usage](#usage)
     - [Production](#production)
     - [Local](#local)
- [Sprints](#sprints)
     - [Agile Practices](#agile-practices)
- [Tests](#tests)
- [Diagrams](#diagrams)
     - [Database Schema](#database-schema)
     - [UML Diagram](#uml-class-diagram)
- [Contributors](#contributors)

## Usage 
### Production 
The 4th Year Project Management System is available at [sysc4806project.azurewebsites.net](https://sysc4806project.azurewebsites.net/)

### Local 
1. Clone the repo
   ```
   git clone https://github.com/ConnorMarcus/SYSC4806Project.git
   ```
2. Package the app
   ```
   mvn package
   ```
3. Execute the jar file
   ```
   cd target
   java -jar SYSC4806Project-1.0-SNAPSHOT.jar
   ```
4. Open your browser and navigate to [localhost:8080](https://localhost:8080)

## Sprints
Our project is divided into three 2-week sprints: 
- Early Prototype (Released Nov 13, 2023)
- Alpha Release (Released Nov 27, 2023)
- Final Product (Released Dec 8, 2023)

### Agile Practices
Tickets for new features, bug fixes, and more are available in the [Issues tab](https://github.com/ConnorMarcus/SYSC4806Project/issues). Our weekly scrums are also available in the [Issues tab](https://github.com/ConnorMarcus/SYSC4806Project/issues). The Kanban board is available in the [Project tab](https://github.com/users/ConnorMarcus/projects/1). 

## Tests
1. Run all tests: 
  ```
  mvn clean test
  ```
2. Run an individual test:
```
mvn clean test -Dtest=<PATH_TO_TEST>.<TEST_CLASS>
```
E.g., [TestStudent](/src/test/java/sysc4806/project/models/TestStudent.java)
```
mvn clean test -Dtest=sysc4806.project.models.TestStudent
```

## Diagrams
### Database Schema
The Database Schema diagram can be found in the [diagrams](/diagrams/db_schema_diagram.jpg) folder.

### UML Class Diagram
The UML class diagram for all the Models can be found in the [diagrams](/diagrams/uml_class_diagram.jpg) folder.


   
## Contributors
Connor, Noah, George, and Vahid

<a href="https://github.com/ConnorMarcus/SYSC4806Project/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=ConnorMarcus/SYSC4806Project" />
</a> 
