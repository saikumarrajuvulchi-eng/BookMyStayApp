 feature/UC3-CentralisedRoomInventory
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
/**
 * BookMyStayApp
 *
 * Hotel Booking Management System - Use Case 2
 * Demonstrates Room Types using Abstraction, Inheritance, and Static Availability
 *
 * @author Lakshmi
 * @version 2.0
 */

// Abstract class
 dev
abstract class Room {
    protected String roomType;
    protected int beds;
    protected double price;

 feature/UC3-CentralisedRoomInventory

    // Constructor
 dev
    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

feature/UC3-CentralisedRoomInventory
    public abstract void displayRoomDetails();
}

// Single Room
class SingleRoom extends Room {

    // Abstract method
    public abstract void displayRoomDetails();
}

// Single Room class
class SingleRoom extends Room {

 dev
    public SingleRoom() {
        super("Single Room", 1, 1000);
    }

 feature/UC3-CentralisedRoomInventory

    @Override dev
    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

 feature/UC3-CentralisedRoomInventory
// Double Room
class DoubleRoom extends Room {

// Double Room class
class DoubleRoom extends Room {

 dev
    public DoubleRoom() {
        super("Double Room", 2, 2000);
    }

 feature/UC3-CentralisedRoomInventory
    @Override
 dev
    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

feature/UC3-CentralisedRoomInventory
// Suite Room
class SuiteRoom extends Room {

// Suite Room class
class SuiteRoom extends Room {

 dev
    public SuiteRoom() {
        super("Suite Room", 3, 5000);
    }

feature/UC3-CentralisedRoomInventory

    @Override
 dev
    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

 feature/UC3-CentralisedRoomInventory
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

// Main class (Entry Point)
 dev
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   Welcome to Book My Stay App");
 feature/UC3-CentralisedRoomInventory
        System.out.println("   Hotel Booking System v3.0");
        System.out.println("======================================");

        // Create Room Objects

        System.out.println("   Hotel Booking System v2.0");
        System.out.println("======================================");

        // Creating room objects (Polymorphism)
 dev
        Room single = new SingleRoom();
        Room doub = new DoubleRoom();
        Room suite = new SuiteRoom();

 feature/UC3-CentralisedRoomInventory
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

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display details
        System.out.println("\n--- Room Details & Availability ---\n");

        single.displayRoomDetails();
        System.out.println("Available: " + singleAvailable + "\n");

        doub.displayRoomDetails();
        System.out.println("Available: " + doubleAvailable + "\n");

        suite.displayRoomDetails();
        System.out.println("Available: " + suiteAvailable + "\n");
 dev

        System.out.println("======================================");
    }
}