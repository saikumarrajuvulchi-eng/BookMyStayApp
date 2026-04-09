import java.util.*;

/**
 * BookMyStayApp
 *
 * Hotel Booking Management System - Use Case 8
 * Demonstrates Booking History & Reporting
 *
 * @author Lakshmi
 * @version 8.0
 */

// ---------------- INVENTORY ----------------
class RoomInventory {
    private HashMap<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single Room", 2);
        availability.put("Double Room", 1);
        availability.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        availability.put(roomType, getAvailability(roomType) - 1);
    }
}

// ---------------- RESERVATION ----------------
class Reservation {
    String guestName;
    String roomType;
    String reservationId;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void setReservationId(String id) {
        this.reservationId = id;
    }

    public String toString() {
        return "ID: " + reservationId + " | Guest: " + guestName + " | Room: " + roomType;
    }
}

// ---------------- BOOKING HISTORY ----------------
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r); // preserves order
    }

    public List<Reservation> getAllReservations() {
        return history; // read-only usage expected
    }
}

// ---------------- REPORT SERVICE ----------------
class BookingReportService {

    public void displayAllBookings(List<Reservation> history) {
        System.out.println("\n--- Booking History ---");

        for (Reservation r : history) {
            System.out.println(r);
        }
    }

    public void generateSummary(List<Reservation> history) {

        System.out.println("\n--- Booking Summary Report ---");

        HashMap<String, Integer> countMap = new HashMap<>();

        for (Reservation r : history) {
            countMap.put(r.roomType,
                    countMap.getOrDefault(r.roomType, 0) + 1);
        }

        for (Map.Entry<String, Integer> e : countMap.entrySet()) {
            System.out.println(e.getKey() + " Booked: " + e.getValue());
        }
    }
}

// ---------------- QUEUE ----------------
class BookingRequestQueue {
    Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {

    private Set<String> allocatedIds = new HashSet<>();
    private int counter = 1;

    public void processBookings(BookingRequestQueue queue,
                                RoomInventory inventory,
                                BookingHistory history) {

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();

            if (inventory.getAvailability(r.roomType) > 0) {

                String id = r.roomType.replace(" ", "").toUpperCase() + "-" + counter++;

                if (!allocatedIds.contains(id)) {

                    allocatedIds.add(id);
                    inventory.decrement(r.roomType);

                    r.setReservationId(id);

                    // Add to history
                    history.addReservation(r);

                    System.out.println("Confirmed: " + r);
                }

            } else {
                System.out.println("Failed: " + r.guestName + " (" + r.roomType + ")");
            }
        }
    }
}

// ---------------- MAIN ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App v8.0 =====");

        // Setup
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingHistory history = new BookingHistory();

        // Requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));

        // Process bookings
        BookingService service = new BookingService();
        service.processBookings(queue, inventory, history);

        // Reporting
        BookingReportService report = new BookingReportService();

        report.displayAllBookings(history.getAllReservations());
        report.generateSummary(history.getAllReservations());

        System.out.println("=================================");
    }
}