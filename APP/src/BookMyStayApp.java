import java.util.*;

/**
 * BookMyStayApp
 *
 * Hotel Booking Management System - Use Case 9
 * Demonstrates Error Handling & Validation
 *
 * @author Lakshmi
 * @version 9.0
 */

// ---------------- CUSTOM EXCEPTION ----------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ---------------- INVENTORY ----------------
class RoomInventory {
    private HashMap<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single Room", 1);
        availability.put("Double Room", 1);
        availability.put("Suite Room", 0);
    }

    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, -1);
    }

    public void decrement(String roomType) throws InvalidBookingException {
        int count = getAvailability(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }

        availability.put(roomType, count - 1);
    }

    public boolean isValidRoomType(String roomType) {
        return availability.containsKey(roomType);
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

// ---------------- HISTORY ----------------
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAllReservations() {
        return history;
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

// ---------------- VALIDATOR ----------------
class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate room type
        if (!inventory.isValidRoomType(r.roomType)) {
            throw new InvalidBookingException("Invalid Room Type: " + r.roomType);
        }

        // Validate guest name
        if (r.guestName == null || r.guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }
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

            try {
                // 🔹 Validation (Fail Fast)
                BookingValidator.validate(r, inventory);

                // 🔹 Check availability
                if (inventory.getAvailability(r.roomType) <= 0) {
                    throw new InvalidBookingException("No availability for " + r.roomType);
                }

                // 🔹 Generate ID
                String id = r.roomType.replace(" ", "").toUpperCase() + "-" + counter++;

                if (allocatedIds.contains(id)) {
                    throw new InvalidBookingException("Duplicate Room ID detected!");
                }

                allocatedIds.add(id);

                // 🔹 Update inventory
                inventory.decrement(r.roomType);

                r.setReservationId(id);

                // 🔹 Add to history
                history.addReservation(r);

                System.out.println("Booking Confirmed → " + r);

            } catch (InvalidBookingException e) {
                // 🔹 Graceful error handling
                System.out.println("Booking Failed → " + r.guestName + " | Reason: " + e.getMessage());
            }
        }
    }
}

// ---------------- MAIN ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App v9.0 =====");

        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingHistory history = new BookingHistory();

        // Valid + Invalid Inputs
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("", "Double Room"));          // Invalid name
        queue.addRequest(new Reservation("Bob", "Invalid Room"));      // Invalid type
        queue.addRequest(new Reservation("Charlie", "Suite Room"));    // No availability

        BookingService service = new BookingService();
        service.processBookings(queue, inventory, history);

        System.out.println("\n--- Final Booking History ---");
        for (Reservation r : history.getAllReservations()) {
            System.out.println(r);
        }

        System.out.println("=================================");
    }
}