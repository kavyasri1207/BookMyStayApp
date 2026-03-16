import java.util.HashMap;
import java.util.Map;

/**
 * BookMyStayApp
 *
 * Demonstrates centralized room inventory management using HashMap.
 * Room availability is stored and managed through the RoomInventory class.
 *
 * @author Kavyasri
 * @version 1.0
 */

public class BookMyStayApp {

    /**
     * RoomInventory manages room availability using a HashMap.
     * It acts as a centralized inventory system.
     */
    static class RoomInventory {

        private HashMap<String, Integer> inventory;

        /**
         * Constructor initializes room availability.
         */
        public RoomInventory() {
            inventory = new HashMap<>();

            // Register room types with initial availability
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 3);
            inventory.put("Suite Room", 2);
        }

        /**
         * Get availability for a specific room type.
         */
        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        /**
         * Update availability of a room type.
         */
        public void updateAvailability(String roomType, int newCount) {
            inventory.put(roomType, newCount);
        }

        /**
         * Display full inventory state.
         */
        public void displayInventory() {
            System.out.println("===== Current Room Inventory =====");

            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
            }
        }
    }

    /**
     * Main method – application entry point
     */
    public static void main(String[] args) {

        // Initialize centralized inventory system
        RoomInventory inventory = new RoomInventory();

        // Display current inventory
        inventory.displayInventory();

        System.out.println("\nUpdating availability...\n");

        // Controlled update example
        inventory.updateAvailability("Single Room", 4);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nApplication terminated.");
    }
}