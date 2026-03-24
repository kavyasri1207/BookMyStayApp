import java.util.*;

// Reservation (Represents booking request)
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

    public void display() {
        System.out.println("Guest: " + guestName + ", Room Type: " + roomType);
    }
}

// Booking Request Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View all requests (without removing)
    public void viewRequests() {
        System.out.println("\n===== Booking Requests in Queue =====");
        for (Reservation r : queue) {
            r.display();
        }
    }

    // Get next request (for future processing)
    public Reservation getNextRequest() {
        return queue.peek(); // does NOT remove
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulating multiple guest requests
        Reservation r1 = new Reservation("Kavya", "Single");
        Reservation r2 = new Reservation("Rahul", "Double");
        Reservation r3 = new Reservation("Anita", "Suite");

        // Guest submits requests (FIFO order)
        requestQueue.addRequest(r1);
        requestQueue.addRequest(r2);
        requestQueue.addRequest(r3);

        // View queued requests
        requestQueue.viewRequests();

        // Show next request (without removing)
        System.out.println("\nNext request to process:");
        Reservation next = requestQueue.getNextRequest();
        if (next != null) {
            next.display();
        }
    }
}