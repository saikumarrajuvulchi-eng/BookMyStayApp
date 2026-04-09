import java.util.*;

/**
 * BookMyStayApp
 *
 * Hotel Booking Management System - Use Case 10
 * Demonstrates Booking Cancellation & Inventory Rollback
 *
 * @author Lakshmi
 * @version 10.0
 */

// ---------------- EXCEPTION ----------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String msg) {
        super(msg);
    }
}

// ---------------- INVENTORY ----------------
class RoomInventory {
    private HashMap<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single Room", 1);
        availability.put("Double Room", 1);
        availability.put("Suite Room", 1);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, -1);
    }

    public void decrement(String type) throws InvalidBookingException {
        int count = getAvailability(type);
        if (count <= 0)
            throw new InvalidBookingException("No rooms available for " + type);
        availability.put(type, count - 1);
    }

    public void increment(String type) {
        availability.put(type, getAvailability(type) + 1);
    }

    public boolean isValidRoomType(String type) {
        return availability.containsKey(type);
    }

    public void display() {
        System.out.println("\nInventory:");
        for (var e : availability.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }
    }
}

// ---------------- RESERVATION ----------------
class Reservation {
    String guestName;
    String roomType;
    String reservationId;
    boolean isCancelled = false;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public void setId(String id) {
        this.reservationId = id;
    }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType +
                (isCancelled ? " (Cancelled)" : "");
    }
}

// ---------------- HISTORY ----------------
class BookingHistory {
    private List<Reservation> list = new ArrayList<>();

    public void add(Reservation r) {
        list.add(r);
    }

    public Reservation findById(String id) {
        for (Reservation r : list) {
            if (id.equals(r.reservationId)) return r;
        }
        return null;
    }

    public List<Reservation> getAll() {
        return list;
    }
}

// ---------------- QUEUE ----------------
class BookingRequestQueue {
    Queue<Reservation> q = new LinkedList<>();

    public void add(Reservation r) {
        q.offer(r);
    }

    public Reservation next() {
        return q.poll();
    }

    public boolean isEmpty() {
        return q.isEmpty();
    }
}

// ---------------- BOOKING ----------------
class BookingService {
    private Set<String> allocatedIds = new HashSet<>();
    private int counter = 1;

    public void process(BookingRequestQueue q,
                        RoomInventory inv,
                        BookingHistory history) {

        while (!q.isEmpty()) {
            Reservation r = q.next();

            try {
                if (!inv.isValidRoomType(r.roomType))
                    throw new InvalidBookingException("Invalid room type");

                String id = r.roomType.replace(" ", "").toUpperCase() + "-" + counter++;

                if (allocatedIds.contains(id))
                    throw new InvalidBookingException("Duplicate ID");

                inv.decrement(r.roomType);

                allocatedIds.add(id);
                r.setId(id);

                history.add(r);

                System.out.println("Confirmed → " + r);

            } catch (Exception e) {
                System.out.println("Failed → " + r.guestName + " | " + e.getMessage());
            }
        }
    }
}

// ---------------- CANCELLATION ----------------
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancel(String reservationId,
                       BookingHistory history,
                       RoomInventory inventory) {

        System.out.println("\nProcessing Cancellation for: " + reservationId);

        Reservation r = history.findById(reservationId);

        try {
            if (r == null)
                throw new InvalidBookingException("Reservation not found");

            if (r.isCancelled)
                throw new InvalidBookingException("Already cancelled");

            // Push to stack (LIFO)
            rollbackStack.push(reservationId);

            // Restore inventory
            inventory.increment(r.roomType);

            // Mark cancelled
            r.isCancelled = true;

            System.out.println("Cancellation Successful → " + reservationId);

        } catch (InvalidBookingException e) {
            System.out.println("Cancellation Failed → " + e.getMessage());
        }
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

// ---------------- MAIN ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App v10.0 =====");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add bookings
        queue.add(new Reservation("Alice", "Single Room"));
        queue.add(new Reservation("Bob", "Double Room"));

        BookingService booking = new BookingService();
        booking.process(queue, inventory, history);

        // Cancel booking
        CancellationService cancel = new CancellationService();

        // Take first reservation ID
        String id = history.getAll().get(0).reservationId;

        cancel.cancel(id, history, inventory);
        cancel.cancel("INVALID-ID", history, inventory); // error case

        cancel.showRollbackStack();

        // Final state
        System.out.println("\nFinal Booking History:");
        for (Reservation r : history.getAll()) {
            System.out.println(r);
        }

        inventory.display();

        System.out.println("=================================");
    }
}