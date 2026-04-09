import java.util.*;

// Custom Exception for invalid bookings
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

    public Reservation(String reservationId, String guestName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Nights: " + nights;
    }
}

// Booking History
class BookingHistory {
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations); // safe copy
    }
}

// Inventory Management (guards system state)
class RoomInventory {
    private Map<String, Integer> roomStock = new HashMap<>();

    public RoomInventory() {
        roomStock.put("Standard", 2);
        roomStock.put("Deluxe", 2);
        roomStock.put("Suite", 1);
    }

    // Validate and reserve room
    public void reserveRoom(String roomType) throws InvalidBookingException {
        if (!roomStock.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int available = roomStock.get(roomType);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        // Update inventory safely
        roomStock.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory: " + roomStock);
    }
}

// Validator (Fail-Fast Design)
class BookingValidator {

    public static void validate(String guestName, String roomType, int nights) throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type must be provided.");
        }

        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than 0.");
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        RoomInventory inventory = new RoomInventory();

        // Simulated inputs (some invalid)
        Object[][] bookingInputs = {
                {"R001", "Alice", "Deluxe", 3},
                {"R002", "", "Standard", 2},        // invalid name
                {"R003", "Bob", "Luxury", 1},       // invalid room type
                {"R004", "Charlie", "Suite", 0},    // invalid nights
                {"R005", "David", "Suite", 2},      // may fail (limited inventory)
                {"R006", "Eve", "Standard", 1}
        };

        for (Object[] input : bookingInputs) {
            try {
                String id = (String) input[0];
                String name = (String) input[1];
                String room = (String) input[2];
                int nights = (int) input[3];

                // Step 1: Validate input (Fail-Fast)
                BookingValidator.validate(name, room, nights);

                // Step 2: Validate inventory (guard state)
                inventory.reserveRoom(room);

                // Step 3: Create reservation
                Reservation res = new Reservation(id, name, room, nights);

                // Step 4: Store booking
                history.addReservation(res);

                System.out.println("Booking Successful: " + res);

            } catch (InvalidBookingException e) {
                // Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
            } catch (Exception e) {
                // Catch unexpected errors (system stability)
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }

        // System continues running safely
        System.out.println("\n--- Final Booking History ---");
        for (Reservation r : history.getAllReservations()) {
            System.out.println(r);
        }

        inventory.displayInventory();
    }
}