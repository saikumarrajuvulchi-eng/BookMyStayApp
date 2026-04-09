import java.util.*;

// ---------------- RESERVATION ----------------
class Reservation {
    String guestName;
    String roomType;

    public Reservation(String name, String type) {
        this.guestName = name;
        this.roomType = type;
    }
}

// ---------------- THREAD-SAFE QUEUE ----------------
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ---------------- INVENTORY ----------------
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single Room", 1);
        availability.put("Double Room", 1);
    }

    // 🔴 CRITICAL SECTION
    public synchronized boolean allocateRoom(String type) {
        int count = availability.getOrDefault(type, 0);

        if (count > 0) {
            availability.put(type, count - 1);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("Final Inventory: " + availability);
    }
}

// ---------------- BOOKING PROCESSOR THREAD ----------------
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue q, RoomInventory i) {
        this.queue = q;
        this.inventory = i;
    }

    @Override
    public void run() {
        while (true) {

            Reservation r;

            // 🔴 synchronized queue access
            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.getRequest();
            }

            if (r != null) {
                boolean success = inventory.allocateRoom(r.roomType);

                if (success) {
                    System.out.println(Thread.currentThread().getName() +
                            " → Booking Confirmed for " + r.guestName +
                            " (" + r.roomType + ")");
                } else {
                    System.out.println(Thread.currentThread().getName() +
                            " → Booking Failed for " + r.guestName +
                            " (No Availability)");
                }
            }
        }
    }
}

// ---------------- MAIN ----------------
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        System.out.println("===== Concurrent Booking Simulation =====");

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Multiple users (same room → conflict)
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Double Room"));
        queue.addRequest(new Reservation("David", "Double Room"));

        // Create threads
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");

        // Start threads
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.display();

        System.out.println("=========================================");
    }
}