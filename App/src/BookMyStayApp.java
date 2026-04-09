import java.util.*;

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

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
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

// Booking History (stores confirmed bookings)
class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        reservations.add(reservation); // maintains insertion order
    }

    // Retrieve all reservations (read-only copy)
    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations);
    }
}

// Reporting Service
class BookingReportService {

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        int totalBookings = reservations.size();
        int totalNights = 0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            totalNights += r.getNights();

            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Nights Booked: " + totalNights);

        System.out.println("\nRoom Type Distribution:");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + ": " + roomTypeCount.get(type));
        }
    }

    // Detailed report
    public void generateDetailedReport(List<Reservation> reservations) {
        System.out.println("\n--- Detailed Booking Report ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation("R001", "Alice", "Deluxe", 3);
        Reservation r2 = new Reservation("R002", "Bob", "Standard", 2);
        Reservation r3 = new Reservation("R003", "Charlie", "Suite", 5);

        // Add to booking history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin retrieves data
        List<Reservation> storedReservations = history.getAllReservations();

        // Generate reports (read-only)
        reportService.generateDetailedReport(storedReservations);
        reportService.generateSummary(storedReservations);
    }
}