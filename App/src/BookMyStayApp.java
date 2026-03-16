/**
 * BookMyStayApp
 *
 * Entry point for the Hotel Booking application.
 * Demonstrates abstraction, inheritance, and polymorphism
 * by modeling different room types.
 *
 * @author Kavyasri
 * @version 1.0
 */

public class BookMyStayApp {

    /**
     * Abstract class representing a general Room.
     * Defines common attributes for all room types.
     */
    static abstract class Room {

        private String roomType;
        private int beds;
        private int size;
        private double price;

        public Room(String roomType, int beds, int size, double price) {
            this.roomType = roomType;
            this.beds = beds;
            this.size = size;
            this.price = price;
        }

        public String getRoomType() {
            return roomType;
        }

        public int getBeds() {
            return beds;
        }

        public int getSize() {
            return size;
        }

        public double getPrice() {
            return price;
        }

        public void displayRoomDetails() {
            System.out.println("Room Type: " + roomType);
            System.out.println("Beds: " + beds);
            System.out.println("Room Size: " + size + " sq.ft");
            System.out.println("Price per night: $" + price);
        }
    }

    /**
     * Single Room class extending Room
     */
    static class SingleRoom extends Room {
        public SingleRoom() {
            super("Single Room", 1, 200, 80.0);
        }
    }

    /**
     * Double Room class extending Room
     */
    static class DoubleRoom extends Room {
        public DoubleRoom() {
            super("Double Room", 2, 350, 120.0);
        }
    }

    /**
     * Suite Room class extending Room
     */
    static class SuiteRoom extends Room {
        public SuiteRoom() {
            super("Suite Room", 3, 600, 250.0);
        }
    }

    /**
     * Main method - program entry point
     */
    public static void main(String[] args) {

        // Create room objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Room availability stored in simple variables
        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable = 2;

        System.out.println("===== Welcome to BookMyStay =====\n");

        // Display Single Room
        singleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + singleRoomAvailable);
        System.out.println();

        // Display Double Room
        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleRoomAvailable);
        System.out.println();

        // Display Suite Room
        suiteRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteRoomAvailable);
        System.out.println();

        System.out.println("Application terminated.");
    }
}