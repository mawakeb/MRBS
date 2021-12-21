# Sprint Retrospective, Iteration #4

> Project: Meeting Room Booking System
>
> Group: 10B
>
> Duration: 11th December - 17th December


### **Scrum Overview**


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
   <td>#1
   </td>
   <td>#1
   </td>
   <td>high
   </td>
   <td>Ani, Timo
   </td>
   <td>8
   </td>
   <td>6
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#2, #3
   </td>
   <td>#2
   </td>
   <td>low
   </td>
   <td>Thijs
   </td>
   <td>2
   </td>
   <td>2
   </td>
   <td>no
   </td>
   <td>I can’t add current user checking or validator valid changes checking yet and I’ll do that next sprint.
   </td>
  </tr>
  <tr>
   <td>#4
   </td>
   <td>#3
   </td>
   <td>medium
   </td>
   <td>Cherin
   </td>
   <td>2
   </td>
   <td>1
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#5
   </td>
   <td>#4
   </td>
   <td>high
   </td>
   <td>Pau
   </td>
   <td>8
   </td>
   <td>6
   </td>
   <td>no
   </td>
   <td>Though some of the basic authentication is done, there is still some work to be done with connecting this to other microservices.
   </td>
  </tr>
  <tr>
   <td>#6
   </td>
   <td>#5
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
  <tr>
   <td>-
   </td>
   <td>#6
   </td>
   <td>high
   </td>
   <td>All
   </td>
   <td>5
   </td>
   <td>5
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>-
   </td>
   <td>#7
   </td>
   <td>high
   </td>
   <td>All
   </td>
   <td>10
   </td>
   <td>-
   </td>
   <td>no
   </td>
   <td>
   </td>
  </tr>
</table>



### **User Story**

* User Story 1: As an employee, I want to be able to make a reservation in a specific room and time
* User Story 2: As an employee, I want to be able to edit or cancel a reservation made by me
* User Story 3: As an admin, I want to be able to edit or cancel a reservation made by another employee
* User Story 4: As a user, I want to view all the reservations that are made for me
* User Story 5: As a user, I want a secure authentication service


### **Task Description**

* Task 1: Implement the method for making a reservation.
    * Priority: high.
    * Incorporate the Builder design pattern to make a reservation
    * Go through the chain of validators to check if the reservation is valid
        * Employees can make a reservation for themselves
        * Secretaries can make a reservation for other employees if they are in the same research group
        * Admins can make a reservation for any employee
* Task 2: Implement the features for editing/ canceling a reservation
    * Priority: low, as it does not affect other functionalities
    * Only admins/secretaries are allowed to do it for other users
* Task 3: Allow user to view all the reservations made for that user
    * Priority: medium; it is one of our must-have requirements but other features don’t depend on it
* Task 4: Implement authentication, security, and basic user functionality.
    * Priority: high
    * Using Spring Security for the authentication.
* Task 5: Allow admins to change the status of a room
    * Priority: low, it’s not a crucial feature that other features have to build up on
* Task 6: Finish Assignment 1 - Task 2:
    * Priority: high; there is a deadline on Friday 17th of December 
    * 2.1. Write a natural language description of why and how the pattern is implemented in your system (6 pts per each design pattern).
    * 2.2. Make a class diagram of how the pattern is structured in your code (3 pts per design pattern)
    * 2.3. Implement the design pattern (6 pts per pattern).
* Task 7: Prepare for the final presentation rehearsal
    * Priority: high, since we have to present on 21st December

## **Main Problems Encountered**

**Problem 1**

Description: There are currently no ways to get information about the current user of the system.

Reaction: All of the other code was made and it now works without this and that will be added later.

**Problem 2**

Description: GitLab was down/ failing before the deadline

Reaction: We tried to submit the work as soon as possible 


## **Adjustments for the next Sprint Plan**

We are done with most of the functionality, so we should extensively test the code we have, polish it and minimize errors. The project is close to ending so we should make sure to finish all tasks.