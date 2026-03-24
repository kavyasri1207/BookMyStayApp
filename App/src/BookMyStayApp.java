import java.util.*;

// Reservation (Booking Request)
class Reservation {
    private String guestName;
    private String roomType;
    private String reservationId;

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

    public void setReservationId(String id) {
        this.reservationId = id;
    }

    public String getReservationId() {
        return reservationId;
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
        return queue.poll();
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

    public void decrementRoom(String type) {
        availability.put(type, availability.get(type) - 1);
    }
}

// Booking Service
class BookingService {
    private Set<String> allocatedRoomIds = new HashSet<>();
    private HashMap<String, Set<String>> roomAllocations = new HashMap<>();

    public void processBookings(BookingRequestQueue queue, InventoryService inventory) {

        int idCounter = 1;

        while (!queue.isEmpty()) {
            Reservation req = queue.getNextRequest();

            System.out.println("\nProcessing: " + req.getGuestName());

            String type = req.getRoomType();

            if (inventory.getAvailability(type) > 0) {

                String roomId;
                do {
                    roomId = type.substring(0, 2).toUpperCase() + "-" + idCounter++;
                } while (allocatedRoomIds.contains(roomId));

                allocatedRoomIds.add(roomId);

                roomAllocations.putIfAbsent(type, new HashSet<>());
                roomAllocations.get(type).add(roomId);

                inventory.decrementRoom(type);

                // Assign reservation ID (same as room ID here)
                req.setReservationId(roomId);

                System.out.println("Booking CONFIRMED → " + roomId);

            } else {
                System.out.println("Booking FAILED (No availability)");
            }
        }
    }
}

// Add-On Service (Optional Feature)
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationID, List<Services>>
    private HashMap<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service: " + service.getServiceName() +
                " to Reservation: " + reservationId);
    }

    // Calculate total cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;

        List<AddOnService> services = serviceMap.get(reservationId);
        if (services != null) {
            for (AddOnService s : services) {
                total += s.getCost();
            }
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation " + reservationId + ":");

        List<AddOnService> services = serviceMap.get(reservationId);
        if (services != null) {
            for (AddOnService s : services) {
                System.out.println("- " + s.getServiceName() + " : " + s.getCost());
            }
        } else {
            System.out.println("No services added.");
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Inventory setup
        InventoryService inventory = new InventoryService();
        inventory.addRoomType("Single", 2);

        // Booking queue
        BookingRequestQueue queue = new BookingRequestQueue();
        Reservation r1 = new Reservation("Kavya", "Single");
        Reservation r2 = new Reservation("Rahul", "Single");

        queue.addRequest(r1);
        queue.addRequest(r2);

        // Process bookings
        BookingService bookingService = new BookingService();
        bookingService.processBookings(queue, inventory);

        // Add-On Services
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Create services
        AddOnService wifi = new AddOnService("Premium WiFi", 500);
        AddOnService breakfast = new AddOnService("Breakfast", 300);

        // Attach services to reservation
        serviceManager.addService(r1.getReservationId(), wifi);
        serviceManager.addService(r1.getReservationId(), breakfast);

        // Display services
        serviceManager.displayServices(r1.getReservationId());

        // Total cost
        double total = serviceManager.calculateTotalCost(r1.getReservationId());
        System.out.println("Total Add-On Cost: " + total);
    }
}