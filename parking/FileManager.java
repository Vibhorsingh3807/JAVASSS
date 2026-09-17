package parking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * FileManager.java
 * Handles persistent storage of parking data using simple CSV text file operations.
 * 
 * SYLLABUS CONCEPTS DEMONSTRATED:
 * - Unit 2: Exception Handling (try-catch-finally, IOException, NumberFormatException)
 * - Unit 3: File Handling Persistence (BufferedReader, BufferedWriter, FileReader, FileWriter)
 * - Unit 3: MVC Separation (Persistence Layer separated from Model/View)
 */
public class FileManager {

    /**
     * Saves the current occupied parking slots to a CSV text file.
     * File format: SlotId,VehicleType,PlateNumber,EntryTimeMillis
     * Example: A1,CAR,DL01AB1234,1710000000000
     */
    public static void saveData(ParkingSlot[] slots, String filename) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));

            for (int i = 0; i < slots.length; i++) {
                ParkingSlot slot = slots[i];
                if (slot != null && slot.isOccupied() && slot.getVehicle() != null) {
                    Vehicle v = slot.getVehicle();
                    String line = slot.getSlotId() + "," 
                                + v.getVehicleType() + "," 
                                + v.getPlateNumber() + "," 
                                + v.getEntryTime();
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("(!) Error saving parking data to file: " + e.getMessage());
        } finally {
            // Ensure writer is closed to flush buffers and release system resources
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println("(!) Error closing file writer: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Loads previously saved parking data from the CSV text file into the slots array.
     */
    public static void loadData(ParkingSlot[] slots, String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            // First time running; file doesn't exist yet, which is completely normal
            return;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Split CSV columns
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String slotId = parts[0].trim();
                    String vehicleType = parts[1].trim();
                    String plateNumber = parts[2].trim();
                    long entryTime = Long.parseLong(parts[3].trim());

                    // Find corresponding slot in memory
                    for (int i = 0; i < slots.length; i++) {
                        if (slots[i] != null && slots[i].getSlotId().equalsIgnoreCase(slotId)) {
                            // Recreate vehicle object using polymorphism
                            Vehicle vehicle;
                            if (vehicleType.equalsIgnoreCase("CAR")) {
                                vehicle = new Car(plateNumber, entryTime);
                            } else {
                                vehicle = new Bike(plateNumber, entryTime);
                            }
                            slots[i].park(vehicle);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("(!) Error loading parking data from file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("(!) Corrupt timestamp in data file: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("(!) Error closing file reader: " + e.getMessage());
                }
            }
        }
    }
}
