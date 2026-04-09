import java.util.HashMap;
import java.util.Map;

/**
 * BookMyStayApp
 *
 * Hotel Booking Management System - Use Case 3
 * Demonstrates Centralized Room Inventory using HashMap
 *
 * @author Lakshmi
 * @version 3.0
 */

// Abstract Room class
abstract class Room {
    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public abstract void displayRoomDetails();
}

// Single Room
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

// Double Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2000);
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

// Suite Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

// Inventory Management Class
class RoomInventory {

    private HashMap<String, Integer> availability;

    // Constructor - initialize inventory
    public RoomInventory() {
        availability = new HashMap<>();
        availability.put("Single Room", 5);
        availability.put("Double Room", 3);
        availability.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int count) {
        availability.put(roomType, count);
    }

    // Display all inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.println(entry.getKey() + " Available: " + entry.getValue());
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking System v3.0");
        System.out.println("======================================");

        // Create Room Objects
        Room single = new SingleRoom();
        Room doub = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize Inventory
        RoomInventory inventory = new RoomInventory();

        // Display Room Details + Inventory
        System.out.println("\n--- Room Details ---\n");

        single.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability("Single Room") + "\n");

        doub.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability("Double Room") + "\n");

        suite.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability("Suite Room") + "\n");

        // Display full inventory
        inventory.displayInventory();

        System.out.println("======================================");
    }
}