import java.util.*;

/**
 * BookMyStayApp
 *
 * Hotel Booking Management System - Use Case 6
 * Demonstrates Room Allocation with Double Booking Prevention
 *
 * @author Lakshmi
 * @version 6.0
 */

// ---------------- ROOM CLASSES ----------------
abstract class Room {
    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2000);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }
}

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

    public void displayInventory() {
        System.out.println("\n--- Updated Inventory ---");
        for (Map.Entry<String, Integer> e : availability.entrySet()) {
            System.out.println(e.getKey() + " Available: " + e.getValue());
        }
    }
}

// ---------------- RESERVATION ----------------
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------------- QUEUE ----------------
class BookingRequestQueue {
    Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();
    private int idCounter = 1;

    public void processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        System.out.println("\n--- Processing Bookings ---");

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();
            String type = r.roomType;

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                // Generate unique room ID
                String roomId = type.replace(" ", "").toUpperCase() + "-" + idCounter++;

                // Ensure uniqueness using Set
                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    // Map room type to IDs
                    roomAllocations.putIfAbsent(type, new HashSet<>());
                    roomAllocations.get(type).add(roomId);

                    // Update inventory (IMPORTANT)
                    inventory.decrement(type);

                    System.out.println("Booking Confirmed!");
                    System.out.println("Guest: " + r.guestName);
                    System.out.println("Room Type: " + type);
                    System.out.println("Room ID: " + roomId + "\n");

                }

            } else {
                System.out.println("Booking Failed (No Availability) for " + r.guestName + " [" + type + "]");
            }
        }
    }

    public void displayAllocations() {
        System.out.println("\n--- Room Allocations ---");
        for (Map.Entry<String, Set<String>> e : roomAllocations.entrySet()) {
            System.out.println(e.getKey() + " -> " + e.getValue());
        }
    }
}

// ---------------- MAIN ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v6.0");
        System.out.println("======================================");

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Suite Room"));

        // Booking Service
        BookingService service = new BookingService();

        // Process bookings
        service.processBookings(queue, inventory);

        // Show results
        service.displayAllocations();
        inventory.displayInventory();

        System.out.println("======================================");
    }
}