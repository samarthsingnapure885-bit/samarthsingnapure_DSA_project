# PROJECT REPORT
## Bus Reservation System — PS No. 19

**University:** Rungta International Skills University  
**School:** School of Computer Science and Engineering  
**Student Name:** ____________________  
**Roll Number:** ____________________  
**Course / Semester:** ____________________  
**Submission Date:** ____________________

---

## 1. Introduction
The Bus Reservation System is a Java console application that allows users to view buses, search routes by destination, book tickets, cancel tickets and sort buses by fare. If a bus is full, the passenger is placed in that bus's waiting queue. When a confirmed ticket is cancelled, the first passenger in that bus's waiting queue receives the released seat.

## 2. Problem Statement
Develop a console bus reservation application. Search buses by destination, book and cancel tickets, and sort buses by fare. If a bus is full, the passenger joins a waiting queue and gets the seat when a ticket is cancelled.

## 3. Objectives
- Load bus information from `buses.txt`.
- Store buses in an `ArrayList`.
- Maintain a separate FIFO waiting queue for every bus.
- Search buses by destination.
- Book and cancel tickets.
- Assign a cancelled seat to the first waiting passenger for that bus.
- Sort buses by fare in ascending order.
- Save booking and waiting-list information in a file.

## 4. Tools and Technologies
- Programming language: Java
- IDE / editor: Visual Studio Code
- JDK: JDK 8 or later (JDK 17 or 21 recommended)
- Data files: `buses.txt` and `tickets.txt`
- Repository: GitHub

## 5. Concepts Used
- **ArrayList:** Stores bus objects and confirmed tickets.
- **Queue:** Each Bus object contains its own `LinkedList`-based waiting queue.
- **FIFO:** The first passenger who joins a bus's waiting queue gets the next available seat.
- **Searching:** Bus IDs, ticket IDs and destination text are searched.
- **Sorting:** Buses are sorted by fare in ascending order using a Comparator.
- **Methods, classes and objects:** The program is divided into methods and Bus/Ticket classes.
- **File handling:** Bus data is loaded from `buses.txt`; bookings and waiting passengers are loaded from and saved to `tickets.txt`.

## 6. Menu Options
1. View All Buses
2. Search Buses by Destination
3. Book Ticket
4. Cancel Ticket
5. Sort Buses by Fare
6. Save and Exit

## 7. Working of the Application
1. The application loads buses from `buses.txt` when it starts.
2. Previously confirmed bookings and waiting passengers are restored from `tickets.txt`.
3. When a customer selects a bus and books a ticket, the system checks the bus ID.
4. If a seat is available, the passenger receives a ticket ID and the number of available seats decreases.
5. If the bus is full, the passenger is placed in that bus's own waiting queue.
6. When a ticket is cancelled, its seat is released. The first passenger in that bus's waiting queue is assigned the seat and receives a new ticket ID.
7. The sort option displays buses from lowest fare to highest fare.
8. The program saves confirmed tickets and waiting passengers to `tickets.txt` after changes and on exit.

## 8. Sample Bus Data
The format in `buses.txt` is `BusID|Route|Fare|TotalSeats`.

```text
B101|Raipur-Bhopal|700|2
B102|Raipur-Nagpur|450|2
```

## 9. Ticket Data Format
Confirmed ticket: `BOOKED|TicketID|BusID|PassengerName`  
Waiting passenger: `WAITING|BusID|PassengerName`

The first generated ticket ID is TKT101. A waiting passenger receives a ticket ID only after a seat is assigned.

## 10. Assignment Test Cases

### Test Case 1 — Normal Input
Input: Menu option 3; Bus B101; Passenger Riya.  
Expected: Ticket booked. TKT101 | Riya | B101 Raipur-Bhopal; Seats left: 1.

### Test Case 2 — Boundary / Minimum Input
Precondition: B101 has 1 seat left.  
Input: Menu option 3; Bus B101; Passenger Aman.  
Expected: Ticket booked. TKT102 | Aman | B101 Raipur-Bhopal; Seats left: 0.

### Test Case 3 — Invalid Input
Input: Menu option 3; Bus B999.  
Expected: Bus B999 not found.

### Test Case 4 — Different Valid Scenario
Precondition: B101 is full with TKT101 (Riya) and TKT102 (Aman).

(a) Book B101 for Dev: expected `Bus full. Dev added to waiting queue.`  
(b) Cancel TKT101: expected `Ticket TKT101 cancelled.` and `Seat given to Dev: TKT103`.  
(c) Sort by fare: B102 | 450 | Raipur-Nagpur appears before B101 | 700 | Raipur-Bhopal.

## 11. Conclusion
The Bus Reservation System demonstrates how Java collections and file handling can be used in a practical reservation scenario. It uses an ArrayList to manage buses, a separate FIFO queue for each bus, searching and sorting, ticket cancellation, automatic waiting-list promotion and persistent file storage.

## 12. Screenshots to Add
Insert screenshots of your actual outputs for:
1. Main menu and bus list
2. Searching by destination
3. Successful booking
4. Bus full and passenger added to waiting queue
5. Ticket cancellation and waiting passenger promotion
6. Buses sorted by fare
7. Invalid bus ID

## 13. Compile and Run
```bash
javac BusReservationSystem.java
java BusReservationSystem
```
Run both commands from the project folder so the program can locate the two text files.
