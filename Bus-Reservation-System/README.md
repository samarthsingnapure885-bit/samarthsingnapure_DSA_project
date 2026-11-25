# Bus Reservation System — PS No. 19

A Java console application for bus reservations, prepared according to the supplied project description.

## Features
1. View all buses and available seats.
2. Search buses by destination (case-insensitive, partial match).
3. Book tickets.
4. Cancel tickets and automatically assign a released seat to the first passenger in that bus's FIFO waiting queue.
5. Sort buses by fare, lowest to highest.
6. Load and save reservation data using text files.

## Java concepts used
- `ArrayList` for buses and confirmed tickets.
- A separate `Queue` (`LinkedList`) inside each Bus object.
- FIFO waiting queue.
- Searching, sorting with `Comparator`, methods, classes and objects.
- File handling with `BufferedReader` and `BufferedWriter`.
- Input validation and exception handling.

## Files
- `BusReservationSystem.java` — the complete Java solution in one source file.
- `buses.txt` — bus ID, route, fare and total seat capacity.
- `tickets.txt` — confirmed tickets and waiting-list passengers; updated by the program.
- `Project-Report.md` — report draft.
- `screenshots/` — add screenshots of actual program outputs here.

## Bus file format
Each bus is one line:
`BusID|Route|Fare|TotalSeats`

Sample records provided in `buses.txt`:
```text
B101|Raipur-Bhopal|700|2
B102|Raipur-Nagpur|450|2
```

## Ticket file format
The program stores confirmed bookings as:
`BOOKED|TicketID|BusID|PassengerName`

Waiting-list passengers are stored as:
`WAITING|BusID|PassengerName`

Remaining seats are calculated on startup from total capacity minus confirmed tickets.

## Run in VS Code
1. Install a JDK (JDK 17 or 21 recommended).
2. Install Microsoft's **Extension Pack for Java** in VS Code.
3. Open the extracted `Bus-Reservation-System` folder in VS Code.
4. Open Terminal → New Terminal. Confirm the terminal is in the project folder.
5. Compile:
   ```bash
   javac BusReservationSystem.java
   ```
6. Run:
   ```bash
   java BusReservationSystem
   ```

Keep both `.txt` files beside the `.java` file and run from this project folder.

## Menu options
1. View All Buses
2. Search Buses by Destination
3. Book Ticket
4. Cancel Ticket
5. Sort Buses by Fare
6. Save and Exit

The first ticket ID is `TKT101`.

## Assignment test cases
1. Book B101 for Riya: `TKT101`, 1 seat left.
2. With one seat left, book B101 for Aman: `TKT102`, 0 seats left.
3. Try to book B999: `Bus B999 not found.`
4. With B101 full, add Dev to its waiting queue; cancel TKT101; Dev receives TKT103. Sorting should list B102 (450) before B101 (700).

**For a fresh test of the sample sequence:** close the program and reset `tickets.txt` to its two initial comment lines before starting. Do not delete or modify `buses.txt`.
