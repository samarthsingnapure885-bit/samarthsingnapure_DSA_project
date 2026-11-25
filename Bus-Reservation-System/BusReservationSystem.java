import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** PS No. 19 - Bus Reservation System */
public class BusReservationSystem {
    private static final Path BUS_FILE = Paths.get("buses.txt");
    private static final Path TICKET_FILE = Paths.get("tickets.txt");
    private static final List<Bus> buses = new ArrayList<>();
    private static final List<Ticket> bookedTickets = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);
    private static int nextTicketNumber = 101;

    public static void main(String[] args) {
        loadBuses();
        loadTicketData();
        System.out.println("====================================");
        System.out.println("       BUS RESERVATION SYSTEM");
        System.out.println("====================================");
        boolean running = true;
        while (running) {
            showMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: viewBuses(); break;
                case 2: searchByDestination(); break;
                case 3: bookTicket(); break;
                case 4: cancelTicket(); break;
                case 5: sortBusesByFare(); break;
                case 6:
                    saveTicketData();
                    System.out.println("Reservation data saved successfully.");
                    System.out.println("Thank you for using Bus Reservation System!");
                    running = false;
                    break;
                default: System.out.println("Invalid option. Choose 1 to 6.");
            }
        }
        sc.close();
    }

    private static void showMenu() {
        System.out.println("\n--------------- MAIN MENU ---------------");
        System.out.println("1. View All Buses");
        System.out.println("2. Search Buses by Destination");
        System.out.println("3. Book Ticket");
        System.out.println("4. Cancel Ticket");
        System.out.println("5. Sort Buses by Fare");
        System.out.println("6. Save and Exit");
        System.out.println("-----------------------------------------");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Invalid input. Enter a whole number."); }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (value.isEmpty()) System.out.println("This field cannot be empty.");
            else if (value.contains("|") || value.contains("\n") || value.contains("\r"))
                System.out.println("Do not use the | character or line breaks.");
            else return value;
        }
    }

    private static Bus findBus(String id) {
        for (Bus bus : buses)
            if (bus.id.equalsIgnoreCase(id.trim())) return bus;
        return null;
    }

    private static Ticket findTicket(String id) {
        for (Ticket ticket : bookedTickets)
            if (ticket.id.equalsIgnoreCase(id.trim())) return ticket;
        return null;
    }

    private static void viewBuses() {
        System.out.println("\n---------------- ALL BUSES ----------------");
        if (buses.isEmpty()) { System.out.println("No buses loaded. Check buses.txt."); return; }
        printBusHeader();
        for (Bus bus : buses) printBus(bus);
    }

    private static void printBusHeader() {
        System.out.printf("%-10s %-24s %-12s %-12s %-12s%n", "Bus ID", "Route", "Fare", "Total Seats", "Seats Left");
        System.out.println("--------------------------------------------------------------------------");
    }

    private static void printBus(Bus b) {
        System.out.printf("%-10s %-24s Rs.%-9s %-12d %-12d%n", b.id, b.route, b.fare.toPlainString(), b.totalSeats, b.seatsLeft);
    }

    private static void searchByDestination() {
        System.out.println("\n----------- SEARCH BY DESTINATION -----------");
        String destination = readNonEmpty("Enter destination (example: Bhopal): ");
        boolean found = false;
        for (Bus bus : buses) {
            if (bus.route.toLowerCase().contains(destination.toLowerCase())) {
                if (!found) printBusHeader();
                printBus(bus);
                found = true;
            }
        }
        if (!found) System.out.println("No bus found for destination: " + destination);
    }

    private static void bookTicket() {
        System.out.println("\n---------------- BOOK TICKET ----------------");
        String busId = readNonEmpty("Enter Bus ID: ");
        Bus bus = findBus(busId);
        if (bus == null) {
            System.out.println("Bus " + busId.toUpperCase() + " not found.");
            return;
        }
        String passenger = readNonEmpty("Enter Passenger Name: ");
        if (bus.seatsLeft > 0) {
            Ticket ticket = createTicket(bus, passenger);
            System.out.println("\nTicket booked.");
            System.out.println(ticket.id + " | " + ticket.passenger + " | " + bus.id + " " + bus.route);
            System.out.println("Seats left: " + bus.seatsLeft);
        } else {
            bus.waitingQueue.offer(passenger);
            System.out.println("Bus full. " + passenger + " added to waiting queue.");
        }
        saveTicketData();
    }

    private static Ticket createTicket(Bus bus, String passenger) {
        Ticket ticket = new Ticket("TKT" + nextTicketNumber++, passenger, bus.id);
        bookedTickets.add(ticket);
        bus.seatsLeft--;
        return ticket;
    }

    private static void cancelTicket() {
        System.out.println("\n---------------- CANCEL TICKET ----------------");
        String ticketId = readNonEmpty("Enter Ticket ID to cancel: ");
        Ticket ticket = findTicket(ticketId);
        if (ticket == null) {
            System.out.println("Ticket " + ticketId.toUpperCase() + " not found.");
            return;
        }
        Bus bus = findBus(ticket.busId);
        bookedTickets.remove(ticket);
        if (bus != null) bus.seatsLeft++;
        System.out.println("Ticket " + ticket.id + " cancelled.");

        // The first person waiting for this particular bus receives the released seat.
        if (bus != null && !bus.waitingQueue.isEmpty()) {
            String passenger = bus.waitingQueue.poll();
            Ticket newTicket = createTicket(bus, passenger);
            System.out.println("Seat given to " + passenger + ": " + newTicket.id);
        }
        saveTicketData();
    }

    private static void sortBusesByFare() {
        System.out.println("\n-------------- BUSES SORTED BY FARE --------------");
        if (buses.isEmpty()) { System.out.println("No buses available to sort."); return; }
        List<Bus> sorted = new ArrayList<>(buses);
        Collections.sort(sorted, Comparator.comparing(bus -> bus.fare));
        for (Bus bus : sorted)
            System.out.println(bus.id + " | " + bus.fare.toPlainString() + " | " + bus.route);
    }

    private static void loadBuses() {
        if (!Files.exists(BUS_FILE)) {
            System.out.println("buses.txt not found. Put it in the project folder.");
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(BUS_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
                String[] p = line.split("\\|", -1); // ID|route|fare|capacity
                if (p.length != 4) { System.out.println("Skipping invalid bus record: " + line); continue; }
                try {
                    String id = p[0].trim(), route = p[1].trim();
                    BigDecimal fare = new BigDecimal(p[2].trim());
                    int seats = Integer.parseInt(p[3].trim());
                    if (id.isEmpty() || route.isEmpty() || fare.compareTo(BigDecimal.ZERO) < 0 || seats <= 0 || findBus(id) != null) {
                        System.out.println("Skipping invalid or duplicate bus: " + line);
                        continue;
                    }
                    buses.add(new Bus(id, route, fare, seats));
                } catch (NumberFormatException e) { System.out.println("Skipping invalid numeric bus record: " + line); }
            }
            System.out.println("Loaded " + buses.size() + " bus(es) from buses.txt.");
        } catch (IOException e) { System.out.println("Error reading buses.txt: " + e.getMessage()); }
    }

    private static void loadTicketData() {
        if (!Files.exists(TICKET_FILE)) return;
        try (BufferedReader reader = Files.newBufferedReader(TICKET_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;
                String[] p = line.split("\\|", -1);
                if (p.length == 4 && p[0].equalsIgnoreCase("BOOKED")) {
                    Bus bus = findBus(p[2]);
                    if (bus == null) { System.out.println("Ignoring ticket for unknown bus: " + line); continue; }
                    bookedTickets.add(new Ticket(p[1], p[3], p[2]));
                    bus.seatsLeft--;
                    if (p[1].toUpperCase().startsWith("TKT")) {
                        try { nextTicketNumber = Math.max(nextTicketNumber, Integer.parseInt(p[1].substring(3)) + 1); }
                        catch (NumberFormatException ignored) { }
                    }
                } else if (p.length == 3 && p[0].equalsIgnoreCase("WAITING")) {
                    Bus bus = findBus(p[1]);
                    if (bus != null) bus.waitingQueue.offer(p[2]);
                } else System.out.println("Skipping invalid ticket record: " + line);
            }
            System.out.println("Loaded " + bookedTickets.size() + " confirmed ticket(s).");
        } catch (IOException e) { System.out.println("Error reading tickets.txt: " + e.getMessage()); }
        for (Bus bus : buses) {
            if (bus.seatsLeft < 0) {
                System.out.println("Warning: bookings exceed capacity for " + bus.id + ". Check tickets.txt.");
                bus.seatsLeft = 0;
            }
        }
    }

    private static void saveTicketData() {
        try (BufferedWriter writer = Files.newBufferedWriter(TICKET_FILE, StandardCharsets.UTF_8)) {
            for (Ticket t : bookedTickets) {
                writer.write("BOOKED|" + t.id + "|" + t.busId + "|" + t.passenger);
                writer.newLine();
            }
            for (Bus bus : buses) {
                for (String passenger : bus.waitingQueue) {
                    writer.write("WAITING|" + bus.id + "|" + passenger);
                    writer.newLine();
                }
            }
        } catch (IOException e) { System.out.println("Error saving tickets.txt: " + e.getMessage()); }
    }

    private static class Bus {
        private final String id, route;
        private final BigDecimal fare;
        private final int totalSeats;
        private int seatsLeft;
        private final Queue<String> waitingQueue = new LinkedList<>();
        Bus(String id, String route, BigDecimal fare, int totalSeats) {
            this.id = id; this.route = route; this.fare = fare;
            this.totalSeats = totalSeats; this.seatsLeft = totalSeats;
        }
    }

    private static class Ticket {
        private final String id, passenger, busId;
        Ticket(String id, String passenger, String busId) {
            this.id = id; this.passenger = passenger; this.busId = busId;
        }
    }
}
