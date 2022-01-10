<!-- Output copied to clipboard! -->

<!-----

Yay, no errors, warnings, or alerts!

Conversion time: 0.786 seconds.


Using this Markdown file:

1. Paste this output into your source file.
2. See the notes and action items below regarding this conversion run.
3. Check the rendered output (headings, lists, code blocks, tables) for proper
   formatting and use a linkchecker before you publish this page.

Conversion notes:

* Docs to Markdown version 1.0β33
* Sun Jan 09 2022 15:22:30 GMT-0800 (PST)
* Source doc: Sprint Retrospective, Iteration #5
* Tables are currently converted to HTML tables.
----->



# Sprint Retrospective, Iteration #5

Project: Meeting Room Booking System

Group: 10B

Duration: 18th December - 9th January


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
   <td>-
   </td>
   <td>#1
   </td>
   <td>high
   </td>
   <td>All
   </td>
   <td>10
   </td>
   <td>15
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#1, #2
   </td>
   <td>#2
   </td>
   <td>high
   </td>
   <td>Thijs
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
   <td>#3
   </td>
   <td>#3
   </td>
   <td>high
   </td>
   <td>Pau, Timo
   </td>
   <td>6
   </td>
   <td>8
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#4
   </td>
   <td>#4
   </td>
   <td>high
   </td>
   <td>Thijs, Ani
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
   <td>#5
   </td>
   <td>high
   </td>
   <td>All
   </td>
   <td>10
   </td>
   <td>2
   </td>
   <td>no
   </td>
   <td>Our presentation was postponed to week 9
   </td>
  </tr>
  <tr>
   <td>#5
   </td>
   <td>#6
   </td>
   <td>high
   </td>
   <td>Cherin
   </td>
   <td>2
   </td>
   <td>2
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#6
   </td>
   <td>#7
   </td>
   <td>high
   </td>
   <td>Pau, Timo
   </td>
   <td>2
   </td>
   <td>2
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>#5
   </td>
   <td>#8
   </td>
   <td>high
   </td>
   <td>Thijs, Ani
   </td>
   <td>2
   </td>
   <td>2
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
  <tr>
   <td>-
   </td>
   <td>#9
   </td>
   <td>high
   </td>
   <td>All
   </td>
   <td>20
   </td>
   <td>30
   </td>
   <td>yes
   </td>
   <td>
   </td>
  </tr>
</table>



### **User Story**



* User Story 1: As an employee, I want to be able to edit or cancel a reservation made by me
* User Story 2: As an admin, I want to be able to edit or cancel a reservation made by another employee
* User Story 3: As a user, I want a secure authentication service
* User story 4: As an employee, I want the system to keep track of my research group with the role.
* User story 5: As an admin, I want to be able to add and manage objects like adding new rooms, buildings, research groups etc.
* User story 6: As a user, I want to be able to make an account with my role


### **Task Description**



* Task 1: Prepare for the final presentation (rehearsal)
    * Priority: High, since we have to present on December 21st
* Task 2: Finish the features for editing/canceling a reservation
    * Priority: high
    * Only admins/secretaries are allowed to do it for other users
    * Make use of the validators to see if an edited reservation is valid
* Task 3: Implement authentication, security, and basic user functionality.
    * Priority: high
    * Using Spring Security for authentication.
* Task 4: Implement research group microservice 
    * Priority: high, we have the final presentation on January 18th
    * Check if the given user is a secretary of another given user upon request
    * Keep track of the research group with their secretaries
* Task 5: Prepare for the final presentation
    * Priority: high, we had
    * Apply the feedback from the rehearsal
* Task 6: Allow creating a room, building and adding equipment in the database
    * Priority: high
    * For admins only
* Task 7: Allow creating a user account
    * Priority: high
    * Need verification of existing admins to get secretary/ admin role
* Task 8: Allow creating a research group 
    * Priority: high
    * For admins only
* Task 9: Polish, comment & test the code
    * Priority: high
    * Make sure all pmd and checkstyle tests pass

**Main Problems Encountered**

**Problem 1**

Description: A lot of checkstyle errors piled up

Reaction: Divided the files and worked on them together to resolve them at once

**Problem 2**

Description: Mocking the static communication methods didn’t work

Reaction: We added methods in the controller class that could be mocked that called these static methods and mocked those

**Problem 3**

Description: Dealing with testing security and authentication was very complex, due to having to pass many tokens around, as well as dealing with the usual mocks.

Reaction: Upon doing some research, importing a package (spring-security-test) made this a bit easier, as you could make a test be run through an authenticated user in one line.


## **Adjustments for the next Sprint Plan**

This is the last sprint for the Implementation part of the project.
