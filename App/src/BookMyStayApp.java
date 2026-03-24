import java.util.*;

// Domain Model
class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: " + price);
        System.out.println("Amenities: " + amenities);
    }
}

// Inventory (State Holder)
class Inventory {
    private HashMap<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    // Read-only method
    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }
}

// Search Service (Read-only logic)
class SearchService {
    public void searchAvailableRooms(Inventory inventory, List<Room> rooms) {
        System.out.println("===== Available Rooms =====\n");

        for (Room room : rooms) {
            int count = inventory.getAvailability(room.getType());

            // Show only available rooms
            if (count > 0) {
                room.displayDetails();
                System.out.println("Available Count: " + count);
                System.out.println("--------------------------");
            }
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Create room objects
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room("Single", 2000, "WiFi, AC"));
        rooms.add(new Room("Double", 3500, "WiFi, AC, TV"));
        rooms.add(new Room("Suite", 6000, "WiFi, AC, TV, Pool"));

        // Setup inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0); // Will not be shown
        inventory.addRoom("Suite", 2);

        // Perform search (read-only)
        SearchService searchService = new SearchService();
        searchService.searchAvailableRooms(inventory, rooms);
    }
}