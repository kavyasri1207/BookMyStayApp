import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}

// Booking History
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public List<Reservation> getAll() {
        return reservations;
    }
}

// Inventory (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> stock = new HashMap<>();

    public RoomInventory() {
        stock.put("Standard", 2);
        stock.put("Deluxe", 2);
        stock.put("Suite", 1);
    }

    public void reserve(String type) {
        stock.put(type, stock.get(type) - 1);
    }

    public Map<String, Integer> getStock() {
        return stock;
    }

    public void display() {
        System.out.println("Inventory: " + stock);
    }
}

// Wrapper class (Snapshot of system state)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    BookingHistory history;
    RoomInventory inventory;

    public SystemState(BookingHistory history, RoomInventory inventory) {
        this.history = history;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // SAVE STATE
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("State saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // LOAD STATE
    public static SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("State loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (Exception e) {
            System.out.println("Error loading state (corrupted file). Starting fresh.");
            return null;
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        // STEP 1: Load previous state
        SystemState state = PersistenceService.load();

        BookingHistory history;
        RoomInventory inventory;

        if (state != null) {
            history = state.history;
            inventory = state.inventory;
        } else {
            history = new BookingHistory();
            inventory = new RoomInventory();
        }

        // STEP 2: Simulate operations
        System.out.println("\n--- Current State ---");
        inventory.display();

        // Add new booking
        Reservation r = new Reservation("R" + new Random().nextInt(100),
                "Guest", "Deluxe");

        history.addReservation(r);
        inventory.reserve("Deluxe");

        System.out.println("New booking added: " + r);

        // STEP 3: Save state before shutdown
        SystemState newState = new SystemState(history, inventory);
        PersistenceService.save(newState);

        // STEP 4: Display final state
        System.out.println("\n--- Final State ---");
        for (Reservation res : history.getAll()) {
            System.out.println(res);
        }

        inventory.display();
    }
}