package parking;

import java.util.Scanner;

/**
 * Main.java
 * The View and Entry Point for the Smart Parking Management System.
 * 
 * SYLLABUS CONCEPTS DEMONSTRATED:
 * - Unit 1: Procedural Execution, Static Main Method, Method Invocations
 * - Unit 2: Scanner class for Console Input, Switch-Case branching, Exception Handling
 * - Unit 3: View Layer in MVC Architecture, Lifecycle management of background thread
 */
public class Main {

    // Persistence file path
    private static final String DATA_FILE = "parking_data.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ParkingManager manager = new ParkingManager();

        // Load existing parking records from file at startup
        FileManager.loadData(manager.getSlots(), DATA_FILE);
        System.out.println(">> Parking data loaded successfully from '" + DATA_FILE + "'.");

        // Start background auto-save thread (Unit 3 - Multithreading)
        AutoSaveThread autoSaver = new AutoSaveThread(manager.getSlots(), DATA_FILE);
        autoSaver.start();

        boolean running = true;

        while (running) {
            System.out.println("========================================");
            System.out.println("       SMART PARKING MANAGEMENT");
            System.out.println("========================================");
            System.out.println(" 1. Show Parking Map");
            System.out.println(" 2. Park Vehicle");
            System.out.println(" 3. Remove Vehicle");
            System.out.println(" 4. Search Vehicle");
            System.out.println(" 5. Exit");
            System.out.println("========================================");
            System.out.print("Enter your choice (1-5): ");

            String input = scanner.nextLine().trim();

            int choice = -1;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n(!) Invalid input. Please enter a number between 1 and 5.\n");
                continue;
            }

            switch (choice) {
                case 1:
                    // 1. SHOW MAP
                    manager.showParkingMap();
                    break;

                case 2:
                    // 2. PARK VEHICLE
                    handleParkVehicle(scanner, manager);
                    break;

                case 3:
                    // 3. REMOVE VEHICLE
                    handleRemoveVehicle(scanner, manager);
                    break;

                case 4:
                    // 4. SEARCH VEHICLE
                    handleSearchVehicle(scanner, manager);
                    break;

                case 5:
                    // 5. EXIT
                    System.out.println("\nSaving data and shutting down...");
                    FileManager.saveData(manager.getSlots(), DATA_FILE);
                    autoSaver.stopThread();
                    System.out.println("Data saved successfully.");
                    System.out.println("Thank you for using Smart Parking Management System. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("\n(!) Invalid choice! Please select an option from 1 to 5.\n");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Helper to handle vehicle parking interaction.
     */
    private static void handleParkVehicle(Scanner scanner, ParkingManager manager) {
        if (manager.isFull()) {
            System.out.println("\n(!) Parking lot is completely FULL! Cannot park more vehicles.\n");
            return;
        }

        System.out.println("\n--- PARK VEHICLE ---");
        System.out.println("Select Vehicle Type:");
        System.out.println("  1. Car  (Rate: Rs. 50/hour)");
        System.out.println("  2. Bike (Rate: Rs. 20/hour)");
        System.out.print("Enter choice (1 or 2): ");
        String typeChoice = scanner.nextLine().trim();

        if (!typeChoice.equals("1") && !typeChoice.equals("2")) {
            System.out.println("(!) Invalid vehicle type selected. Aborting.\n");
            return;
        }

        System.out.print("Enter Indian Vehicle Number Plate: ");
        String rawPlate = scanner.nextLine();

        // Validate and normalize registration number
        String validPlate = ParkingManager.validateNumberPlate(rawPlate);

        if (validPlate == null) {
            System.out.println("\n(!) REJECTED: Invalid Indian Number Plate format!");
            System.out.println("    Accepted Formats:");
            System.out.println("    - Standard: DL01AB1234, UP32CD5678, MH12A1234");
            System.out.println("    - Bharat Series: 22BH1234AA");
            System.out.println("    (Prefix must be a valid Indian State/UT code)\n");
            return;
        }

        System.out.print("Enter Parking Slot ID (e.g., A1, B3, C4): ");
        String slotId = scanner.nextLine().trim();

        long now = System.currentTimeMillis();
        Vehicle vehicle;
        if (typeChoice.equals("1")) {
            vehicle = new Car(validPlate, now);
        } else {
            vehicle = new Bike(validPlate, now);
        }

        // Attempt parking
        boolean success = manager.parkVehicle(slotId, vehicle);
        if (success) {
            // Save state immediately upon successful parking
            FileManager.saveData(manager.getSlots(), DATA_FILE);
        }
        System.out.println();
    }

    /**
     * Helper to handle vehicle removal interaction.
     */
    private static void handleRemoveVehicle(Scanner scanner, ParkingManager manager) {
        System.out.println("\n--- REMOVE VEHICLE ---");
        System.out.print("Enter Vehicle Number Plate to remove: ");
        String plateInput = scanner.nextLine().trim();

        // Normalize plate representation for searching
        String normalizedPlate = plateInput.toUpperCase().replaceAll("[\\s-]+", "");

        boolean success = manager.removeVehicle(normalizedPlate);
        if (success) {
            // Save state immediately upon vacancy
            FileManager.saveData(manager.getSlots(), DATA_FILE);
        }
        System.out.println();
    }

    /**
     * Helper to handle vehicle search interaction.
     */
    private static void handleSearchVehicle(Scanner scanner, ParkingManager manager) {
        System.out.println("\n--- SEARCH VEHICLE ---");
        System.out.print("Enter Vehicle Number Plate to search: ");
        String plateInput = scanner.nextLine().trim();

        String normalizedPlate = plateInput.toUpperCase().replaceAll("[\\s-]+", "");
        manager.searchVehicle(normalizedPlate);
    }
}
