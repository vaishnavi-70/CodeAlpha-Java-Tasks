import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Room {
    private int roomNumber;
    private String category;
    private double price;
    private boolean isAvailable;

    public Room(int roomNumber, String category, double price, boolean isAvailable) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public int getRoomNumber() { return roomNumber; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}

class Reservation {
    private String bookingId;
    private String guestName;
    private int roomNumber;
    private int days;
    private double totalAmount;

    public Reservation(String bookingId, String guestName, int roomNumber, int days, double totalAmount) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.days = days;
        this.totalAmount = totalAmount;
    }

    public String toFileString() {
        return bookingId + "," + guestName + "," + roomNumber + "," + days + "," + totalAmount;
    }

    public String getBookingId() { return bookingId; }
    public int getRoomNumber() { return roomNumber; }
    public String getGuestName() { return guestName; }
    public double getTotalAmount() { return totalAmount; }
}

public class HotelSystem {
    private static ArrayList<Room> rooms = new ArrayList<>();
    private static ArrayList<Reservation> bookings = new ArrayList<>();
    private static final String DATA_FILE = "bookings.txt";

    public static void main(String[] args) {
        initializeRooms();
        loadBookingsFromFile();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Hotel Reservation System ===");

        while (true) {
            System.out.println("\n1. View Available Rooms\n2. Book a Room\n3. Cancel Reservation\n4. View Booking Summary\n5. Exit");
            System.out.print("Select an option (1-5): ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    viewAvailableRooms();
                    break;
                case "2":
                    bookRoom(scanner);
                    break;
                case "3":
                    cancelBooking(scanner);
                    break;
                case "4":
                    viewSummaryReport();
                    break;
                case "5":
                    System.out.println("Exiting program. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select from options 1-5.");
            }
        }
    }

    private static void initializeRooms() {
        rooms.add(new Room(101, "Standard", 1200.00, true));
        rooms.add(new Room(102, "Standard", 1200.00, true));
        rooms.add(new Room(201, "Deluxe", 2500.00, true));
        rooms.add(new Room(202, "Deluxe", 2500.00, true));
        rooms.add(new Room(301, "Suite", 5000.00, true));
    }

    private static void viewAvailableRooms() {
        System.out.println("\n------------------------------------------------");
        System.out.printf("%-10s | %-12s | %-10s | %-10s\n", "Room No", "Category", "Price/Night", "Status");
        System.out.println("------------------------------------------------");
        for (Room r : rooms) {
            String status = r.isAvailable() ? "Available" : "Booked";
            System.out.printf("%-10d | %-12s | %-10.2f | %-10s\n", r.getRoomNumber(), r.getCategory(), r.getPrice(), status);
        }
        System.out.println("------------------------------------------------");
    }

    private static void bookRoom(Scanner scanner) {
        System.out.print("\nEnter Guest Full Name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) return;

        viewAvailableRooms();
        System.out.print("Enter Room Number: ");
        int roomNum = Integer.parseInt(scanner.nextLine().trim());

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNum && r.isAvailable()) {
                selectedRoom = r;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room is either invalid or already booked.");
            return;
        }

        System.out.print("Enter total nights: ");
        int nights = Integer.parseInt(scanner.nextLine().trim());
        double totalBill = selectedRoom.getPrice() * nights;

        System.out.printf("Total Payable Amount: INR %.2f\n", totalBill);
        System.out.print("Process Payment? (yes/no): ");
        String payConfirm = scanner.nextLine().trim();

        if (payConfirm.equalsIgnoreCase("yes")) {
            String bookingId = "BK" + (1000 + bookings.size() + 1);
            selectedRoom.setAvailable(false);
            
            Reservation res = new Reservation(bookingId, name, roomNum, nights, totalBill);
            bookings.add(res);
            saveBookingsToFile();

            System.out.println("\n🎉 Booking Confirmed! ID: " + bookingId);
        } else {
            System.out.println("Booking cancelled.");
        }
    }

    private static void cancelBooking(Scanner scanner) {
        System.out.print("\nEnter Booking ID to cancel: ");
        String id = scanner.nextLine().trim();
        Reservation target = null;

        for (Reservation res : bookings) {
            if (res.getBookingId().equalsIgnoreCase(id)) {
                target = res;
                break;
            }
        }

        if (target != null) {
            bookings.remove(target);
            for (Room r : rooms) {
                if (r.getRoomNumber() == target.getRoomNumber()) {
                    r.setAvailable(true);
                    break;
                }
            }
            saveBookingsToFile();
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("No matching reservation found.");
        }
    }

    private static void viewSummaryReport() {
        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings found.");
            return;
        }
        System.out.println("\n========================================================");
        System.out.printf("%-10s | %-15s | %-8s | %-10s\n", "Booking ID", "Guest Name", "Room No", "Total Paid");
        System.out.println("========================================================");
        for (Reservation res : bookings) {
            System.out.printf("%-10s | %-15s | %-8d | INR %.2f\n", res.getBookingId(), res.getGuestName(), res.getRoomNumber(), res.getTotalAmount());
        }
        System.out.println("========================================================");
    }

    private static void saveBookingsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Reservation res : bookings) {
                writer.println(res.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving booking data.");
        }
    }

    private static void loadBookingsFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 5) {
                    Reservation res = new Reservation(tokens[0], tokens[1], Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Double.parseDouble(tokens[4]));
                    bookings.add(res);
                    for (Room r : rooms) {
                        if (r.getRoomNumber() == res.getRoomNumber()) {
                            r.setAvailable(false);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading booking data.");
        }
    }
}