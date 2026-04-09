import java.util.*;

/**
 * BookMyStayApp
 *
 * Hotel Booking Management System - Use Case 7
 * Demonstrates Add-On Services with Booking System
 *
 * @author Lakshmi
 * @version 7.0
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
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------------- BOOKING SERVICE ----------------
class BookingService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();
    private List<String> confirmedReservationIds = new ArrayList<>();
    private int idCounter = 1;

    public List<String> processBookings(BookingRequestQueue queue, RoomInventory inventory) {

        while (!queue.isEmpty()) {

            Reservation r = queue.getNextRequest();
            String type = r.roomType;

            if (inventory.getAvailability(type) > 0) {

                String roomId = type.replace(" ", "").toUpperCase() + "-" + idCounter++;

                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    roomAllocations.putIfAbsent(type, new HashSet<>());
                    roomAllocations.get(type).add(roomId);

                    inventory.decrement(type);

                    confirmedReservationIds.add(roomId);

                    System.out.println("Booking Confirmed → " + r.guestName + " | " + roomId);
                }

            } else {
                System.out.println("Booking Failed → " + r.guestName + " (" + type + ")");
            }
        }

        return confirmedReservationIds;
    }
}

// ---------------- ADD-ON SERVICE ----------------
class AddOnService {
    String serviceName;
    double price;

    public AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String toString() {
        return serviceName + " (₹" + price + ")";
    }
}

// ---------------- ADD-ON MANAGER ----------------
class AddOnServiceManager {

    private HashMap<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add services to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    // Display services
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation " + reservationId + ":");

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        double total = 0;

        for (AddOnService s : services) {
            System.out.println("- " + s);
            total += s.price;
        }

        System.out.println("Total Add-On Cost: ₹" + total);
    }
}

// ---------------- MAIN ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App v7.0 =====");

        // Setup
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();

        // Requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Double Room"));

        // Process bookings
        BookingService bookingService = new BookingService();
        List<String> reservationIds = bookingService.processBookings(queue, inventory);

        // Add-On Services
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Add services to first reservation
        if (!reservationIds.isEmpty()) {
            String resId = reservationIds.get(0);

            serviceManager.addService(resId, new AddOnService("Breakfast", 200));
            serviceManager.addService(resId, new AddOnService("Airport Pickup", 500));

            serviceManager.displayServices(resId);
        }

        System.out.println("=================================");
    }
}