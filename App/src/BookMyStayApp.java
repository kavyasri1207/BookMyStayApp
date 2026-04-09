import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Nights: " + nights +
                ", Status: " + (isCancelled ? "Cancelled" : "Confirmed");
    }
}

// Booking History
class BookingHistory {
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public Reservation findReservation(String id) {
        for (Reservation r : reservations) {
            if (r.getReservationId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
}

// Inventory
class RoomInventory {
    private Map<String, Integer> stock = new HashMap<>();

    public RoomInventory() {
        stock.put("Standard", 2);
        stock.put("Deluxe", 2);
        stock.put("Suite", 1);
    }

    public void reserveRoom(String type) throws InvalidBookingException {
        if (!stock.containsKey(type)) {
            throw new InvalidBookingException("Invalid room type: " + type);
        }
        if (stock.get(type) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + type);
        }
        stock.put(type, stock.get(type) - 1);
    }

    public void releaseRoom(String type) {
        stock.put(type, stock.getOrDefault(type, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + stock);
    }
}

// Cancellation Service (Core Logic)
class CancellationService {

    // Stack for rollback tracking (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              RoomInventory inventory) throws InvalidBookingException {

        // Step 1: Validate existence
        Reservation res = history.findReservation(reservationId);

        if (res == null) {
            throw new InvalidBookingException("Reservation not found: " + reservationId);
        }

        // Step 2: Prevent duplicate cancellation
        if (res.isCancelled()) {
            throw new InvalidBookingException("Reservation already cancelled: " + reservationId);
        }

        // Step 3: Record rollback info (room type)
        rollbackStack.push(res.getRoomType());

        // Step 4: Restore inventory
        inventory.releaseRoom(res.getRoomType());

        // Step 5: Update booking state
        res.cancel();

        System.out.println("Cancellation successful for ID: " + reservationId);
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack (LIFO): " + rollbackStack);
    }
}

// Main App
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        RoomInventory inventory = new RoomInventory();
        CancellationService cancelService = new CancellationService();

        try {
            // Create bookings
            Reservation r1 = new Reservation("R001", "Alice", "Deluxe", 2);
            Reservation r2 = new Reservation("R002", "Bob", "Standard", 1);

            inventory.reserveRoom("Deluxe");
            inventory.reserveRoom("Standard");

            history.addReservation(r1);
            history.addReservation(r2);

            System.out.println("Bookings confirmed.\n");

            // Perform cancellations
            cancelService.cancelBooking("R001", history, inventory);

            // Invalid cancellation (duplicate)
            cancelService.cancelBooking("R001", history, inventory);

        } catch (InvalidBookingException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Final state
        System.out.println("\n--- Booking History ---");
        for (Reservation r : history.getAllReservations()) {
            System.out.println(r);
        }

        System.out.println();
        inventory.displayInventory();

        cancelService.showRollbackStack();
    }
}