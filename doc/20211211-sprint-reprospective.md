# Sprint Retrospective, Iteration #3

> Project: Meeting Room Booking System
>
> Group: 10B
>
> Duration: 2nd December - 10th December


## **Scrum Overview**


<table>
  <tr>
   <td><strong>User Story</strong>
   </td>
   <td><strong>Task</strong>
   </td>
   <td><strong>Priority</strong>
   </td>
   <td><strong>Task Assigned to</strong>
   </td>
   <td><strong>Estimated Effort  (in hours)</strong>
   </td>
   <td><strong>Actual Effort  (in hours)</strong>
   </td>
   <td><strong>Done  (yes/no)</strong>
   </td>
   <td><strong>Notes</strong>
   </td>
  </tr>
  <tr>
   <td>-
   </td>
   <td>#1
   </td>
   <td>high
   </td>
   <td>Pau
   </td>
   <td>1
   </td>
   <td>2
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#1
   </td>
   <td>#2
   </td>
   <td>high
   </td>
   <td>Anna, Timo
   </td>
   <td>3
   </td>
   <td>2
   </td>
   <td>no
   </td>
   <td>decided to implement the Builder pattern during the next sprint
   </td>
  </tr>
  <tr>
   <td>-
   </td>
   <td>#3
   </td>
   <td>high
   </td>
   <td>Anna, Timo
   </td>
   <td>4
   </td>
   <td>8
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>-
   </td>
   <td>#4
   </td>
   <td>medium
   </td>
   <td>All
   </td>
   <td>3
   </td>
   <td>-
   </td>
   <td>no
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>-
   </td>
   <td>#5
   </td>
   <td>high
   </td>
   <td>Cherin
   </td>
   <td>3
   </td>
   <td>3
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#2
   </td>
   <td>#6
   </td>
   <td>high
   </td>
   <td>Thijs
   </td>
   <td>4
   </td>
   <td>6
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#3
   </td>
   <td>#7
   </td>
   <td>high
   </td>
   <td>Pau
   </td>
   <td>3
   </td>
   <td>8
   </td>
   <td>no
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#4
   </td>
   <td>#8
   </td>
   <td>low
   </td>
   <td>Cherin
   </td>
   <td>1
   </td>
   <td>1
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
</table>



### **User Story**

* User Story 1: As an employee, I want to be able to make a reservation in a specific room and time
* User Story 2: As an employee, I want to search available rooms based on room ID or other filters
* User Story 3: As a user, I want a secure authentication service
* User Story 4: As an employee, I want to be able to indicate if the room requires some maintenance



### **Task Description**

* Task 1: Connect all microservices to corresponding databases.
    * Priority: high, we need the databases to work on actual features
* Task 2: Implement the features for making a reservation.
    * Go through the chain of validators to check if the reservation is valid
    * Employees can make a reservation for themselves
    * Secretaries can make a reservation for other employees if they are in the same research group
    * Admins can make a reservation for any employee
* Task 3: Incorporate the Chain of Responsibility pattern in the reservations service
    * Priority: high, we need to use the design pattern as we build the service so we don’t need to restructure the service later
    * Make interface for Validators
    * Make a validator for each condition to check (user, role, room availability, research group, etc.)
    * Reference: [https://github.com/apanichella/example-design-patterns/tree/main/src/main/java/nl/tudelft/chain_of_responsibility](https://github.com/apanichella/example-design-patterns/tree/main/src/main/java/nl/tudelft/chain_of_responsibility) 
* Task 4: Finish Assignment 1 - Task 2:
    * Priority: medium, there is a deadline on Friday, 17th of December 
    * 2.1. Write a natural language description of why and how the pattern is implemented in your system (6 pts per each design pattern).
    * 2.2. Make a class diagram of how the pattern is structured in your code (3 pts per design pattern)
    * 2.3. Implement the design pattern (6 pts per pattern).
* Task 5: Implement check availability feature in the room service 
    * Priority: high
    * Check things like the building’s opening hours
* Task 6: Allow querying of rooms according to the conditions
    * Priority: high
    * Search by room Id
    * Search by time-slot availability, capacity, equipment, or building
* Task 7: Implement authentication, security, and basic user functionality.
    * Using Spring Security for the authentication.
* Task 8: Allow user to leave a notice regarding the room’s maintenance
    * Priority: low



## **Main Problems Encountered**

**Problem 1**

Description: Some members struggled to learn how the communication between classes works.

Reaction: Other experienced members helped them learn.

**Problem 2**

Description: Difficult to decide on which second design pattern we should use which creates delays.

Reaction: Consulted with TA, searched the tutorials of possible design patterns.

**Problem 3**

Description: Difficulty applying Spring Security to secure microservice applications, videos / blog posts online were not up to date / of high quality.

Reaction: Asked other group members to assist with designing a technical solution before actually writing the code with Spring. 


## **Adjustments for the next Sprint Plan**

Decide on the second design pattern and start working on it and finish the tasks that weren’t finished during this sprint. 