import java.util.*;

// Reservation (Booking Request)
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("Request added: " + r.getGuestName());
    }

    public Reservation getNextRequest() {
        return queue.poll(); // removes (FIFO)
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory Service
class InventoryService {
    private HashMap<String, Integer> availability = new HashMap<>();

    public void addRoomType(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    // Update after allocation
    public void decrementRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// Booking Service (Core Logic)
class BookingService {

    // Track allocated room IDs globally
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> assigned room IDs
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    // Generate unique room ID
    private String generateRoomId(String roomType, int counter) {
        return roomType.substring(0, 2).toUpperCase() + "-" + counter;
    }

    public void processBookings(BookingRequestQueue queue, InventoryService inventory) {

        int idCounter = 1;

        while (!queue.isEmpty()) {
            Reservation req = queue.getNextRequest();

            System.out.println("\nProcessing request for: " + req.getGuestName());

            String type = req.getRoomType();

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                // Generate unique ID
                String roomId;
                do {
                    roomId = generateRoomId(type, idCounter++);
                } while (allocatedRoomIds.contains(roomId));

                // Add to global set (uniqueness)
                allocatedRoomIds.add(roomId);

                // Map room type to IDs
                roomAllocations.putIfAbsent(type, new HashSet<>());
                roomAllocations.get(type).add(roomId);

                // Decrement inventory (atomic step)
                inventory.decrementRoom(type);

                // Confirm booking
                System.out.println("Booking CONFIRMED for " + req.getGuestName());
                System.out.println("Room Type: " + type);
                System.out.println("Assigned Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + req.getGuestName() + " (No availability)");
            }
        }
    }

    // Display allocation summary
    public void displayAllocations() {
        System.out.println("\n===== Room Allocations =====");
        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " -> " + roomAllocations.get(type));
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Setup inventory
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Double", 1);

        // Setup booking queue
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.addRequest(new Reservation("Kavya", "Single"));
        queue.addRequest(new Reservation("Rahul", "Single"));
        queue.addRequest(new Reservation("Anita", "Single")); // will fail
        queue.addRequest(new Reservation("Arjun", "Double"));

        // Process bookings
        BookingService bookingService = new BookingService();
        bookingService.processBookings(queue, inventory);

        // Show final allocation
        bookingService.displayAllocations();
    }
}