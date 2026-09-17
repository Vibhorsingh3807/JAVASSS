package parking;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ParkingManager.java
 * The central controller managing the parking slots, business logic,
 * vehicle operations, and Indian number plate validation.
 * 
 * SYLLABUS CONCEPTS DEMONSTRATED:
 * - Unit 1: Procedural Logic within Methods
 * - Unit 2: 1D Arrays of Objects (ParkingSlot[] slots)
 * - Unit 2: Nested Loops (Grid generation)
 * - Unit 2: Branching and Switch/If statements
 * - Unit 3: Controller in MVC Architecture
 */
public class ParkingManager {

    // Fixed array of 12 parking slots (Rows A, B, C; Columns 1, 2, 3, 4)
    private ParkingSlot[] slots = new ParkingSlot[12];

    // List of recognized Indian State and Union Territory RTO codes
    private static final String[] VALID_STATES = {
        "AN", "AP", "AR", "AS", "BR", "CG", "CH", "DD", "DL", "DN",
        "GA", "GJ", "HP", "HR", "JH", "JK", "KA", "KL", "LA", "LD",
        "MH", "ML", "MN", "MP", "MZ", "NL", "OD", "PB", "PY", "RJ",
        "SK", "TN", "TR", "TS", "UK", "UP", "WB"
    };

    /**
     * Constructor: Initializes the 12 parking slots in a 3x4 layout.
     * Slots: A1-A4, B1-B4, C1-C4
     */
    public ParkingManager() {
        char[] rows = {'A', 'B', 'C'};
        int index = 0;
        for (int r = 0; r < rows.length; r++) {
            for (int c = 1; c <= 4; c++) {
                slots[index++] = new ParkingSlot("" + rows[r] + c);
            }
        }
    }

    public ParkingSlot[] getSlots() {
        return slots;
    }

    // =========================================================================
    // 1. SHOW PARKING MAP
    // =========================================================================
    /**
     * Prints a clean, aligned visual grid map of all 12 slots.
     */
    public void showParkingMap() {
        System.out.println("\n================================================================================");
        System.out.println("                         CURRENT PARKING AREA MAP");
        System.out.println("================================================================================");
        System.out.println("         COLUMN 1          COLUMN 2          COLUMN 3          COLUMN 4");
        System.out.println("--------------------------------------------------------------------------------");

        int index = 0;
        char[] rows = {'A', 'B', 'C'};

        for (int r = 0; r < rows.length; r++) {
            System.out.print("ROW " + rows[r] + "   ");
            for (int c = 0; c < 4; c++) {
                String slotDisplay = slots[index++].getDisplayString();
                // Format with fixed column width (18 spaces) for neat terminal alignment
                System.out.printf("%-18s", slotDisplay);
            }
            System.out.println();
        }
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Legend: [XX: FREE] = Available Slot | [XX:PLATE] = Occupied Slot");
        System.out.println("================================================================================\n");
    }

    // =========================================================================
    // 2. PARK VEHICLE
    // =========================================================================
    /**
     * Parks a vehicle into the specified slot.
     * Returns true if successful, false otherwise.
     */
    public boolean parkVehicle(String slotId, Vehicle vehicle) {
        ParkingSlot slot = findSlot(slotId);

        if (slot == null) {
            System.out.println("(!) Error: Slot '" + slotId.toUpperCase() + "' does not exist. Valid slots: A1 to C4.");
            return false;
        }

        if (slot.isOccupied()) {
            System.out.println("(!) Error: Slot '" + slotId.toUpperCase() + "' is already occupied by " 
                               + slot.getVehicle().getPlateNumber() + ".");
            return false;
        }

        // Check if vehicle with same plate is already parked elsewhere
        if (findSlotByPlate(vehicle.getPlateNumber()) != null) {
            System.out.println("(!) Error: Vehicle " + vehicle.getPlateNumber() + " is already parked in the lot!");
            return false;
        }

        slot.park(vehicle);
        System.out.println("\n[SUCCESS] " + vehicle.getVehicleType() + " [" + vehicle.getPlateNumber() 
                           + "] parked successfully in Slot " + slot.getSlotId() + ".");
        return true;
    }

    // =========================================================================
    // 3. REMOVE VEHICLE & CALCULATE BILL
    // =========================================================================
    /**
     * Vacates a vehicle by plate number, calculates parking fee and displays receipt.
     */
    public boolean removeVehicle(String plateNumber) {
        ParkingSlot slot = findSlotByPlate(plateNumber);

        if (slot == null) {
            System.out.println("(!) Error: Vehicle [" + plateNumber + "] was not found in any parking slot.");
            return false;
        }

        Vehicle v = slot.getVehicle();
        long exitTime = System.currentTimeMillis();
        long diffMillis = exitTime - v.getEntryTime();

        // Calculate hours (with a minimum of 1 hour for billing)
        long totalMinutes = diffMillis / (1000 * 60);
        long hours = totalMinutes / 60;
        if (totalMinutes % 60 > 0 || hours == 0) {
            hours += 1; // Round up partial hours
        }

        // Polymorphic method call: calls Car.calculateFee() or Bike.calculateFee()
        double fee = v.calculateFee(hours);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String entryStr = sdf.format(new Date(v.getEntryTime()));
        String exitStr = sdf.format(new Date(exitTime));

        // Vacate the slot
        slot.vacate();

        // Print receipt
        System.out.println("\n========================================");
        System.out.println("             PARKING RECEIPT");
        System.out.println("========================================");
        System.out.println(" Vehicle Number : " + v.getPlateNumber());
        System.out.println(" Vehicle Type   : " + v.getVehicleType());
        System.out.println(" Slot Vacated   : " + slot.getSlotId());
        System.out.println(" Entry Time     : " + entryStr);
        System.out.println(" Exit Time      : " + exitStr);
        System.out.println(" Billed Hours   : " + hours + " hr(s)");
        System.out.println(" Total Fee Due  : Rs. " + fee);
        System.out.println("========================================");
        System.out.println("[SUCCESS] Slot " + slot.getSlotId() + " is now FREE.\n");
        return true;
    }

    // =========================================================================
    // 4. SEARCH VEHICLE
    // =========================================================================
    /**
     * Searches for a vehicle by plate number and displays its location and status.
     */
    public void searchVehicle(String plateNumber) {
        ParkingSlot slot = findSlotByPlate(plateNumber);

        if (slot == null) {
            System.out.println("\n(!) No vehicle found with number plate: " + plateNumber);
            return;
        }

        Vehicle v = slot.getVehicle();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String entryStr = sdf.format(new Date(v.getEntryTime()));

        System.out.println("\n========================================");
        System.out.println("            VEHICLE DETAILS");
        System.out.println("========================================");
        System.out.println(" Number Plate   : " + v.getPlateNumber());
        System.out.println(" Vehicle Type   : " + v.getVehicleType());
        System.out.println(" Allocated Slot : " + slot.getSlotId());
        System.out.println(" Status         : PARKED");
        System.out.println(" Parked Since   : " + entryStr);
        System.out.println("========================================\n");
    }

    // =========================================================================
    // HELPER METHODS
    // =========================================================================

    /**
     * Finds a ParkingSlot by its slot ID (e.g., "A1", "c3").
     */
    public ParkingSlot findSlot(String slotId) {
        if (slotId == null) return null;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].getSlotId().equalsIgnoreCase(slotId.trim())) {
                return slots[i];
            }
        }
        return null;
    }

    /**
     * Finds the ParkingSlot containing a vehicle with the given plate number.
     */
    public ParkingSlot findSlotByPlate(String plateNumber) {
        if (plateNumber == null) return null;
        for (int i = 0; i < slots.length; i++) {
            if (slots[i].isOccupied() && slots[i].getVehicle() != null) {
                if (slots[i].getVehicle().getPlateNumber().equalsIgnoreCase(plateNumber.trim())) {
                    return slots[i];
                }
            }
        }
        return null;
    }

    /**
     * Checks if the parking lot has reached maximum capacity.
     */
    public boolean isFull() {
        for (int i = 0; i < slots.length; i++) {
            if (!slots[i].isOccupied()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Number Plate Validation Method:
     * 
     * 1. Normalization: Converts to uppercase and strips all spaces/hyphens.
     * 2. Format Checks:
     *    a) Standard Indian Series (e.g., DL01AB1234, MH12A1234, UP32CD5678):
     *       - 2 Letters: Valid State/UT Code (checked against VALID_STATES array)
     *       - 2 Digits: RTO District Code (01 to 99)
     *       - 1 or 2 Letters: Series Alphabet(s)
     *       - 4 Digits: Vehicle Registration Number (0001 to 9999)
     *    b) Bharat Series (BH) (e.g., 22BH1234AA):
     *       - 2 Digits: Year of registration
     *       - 'BH': Bharat Series designation
     *       - 4 Digits: Registration number
     *       - 1 or 2 Letters: Series Alphabet(s)
     * 
     * Returns normalized String if valid, or null if invalid.
     */
    public static String validateNumberPlate(String input) {
        if (input == null) return null;

        // Step 1: Normalize input (remove spaces, hyphens, uppercase)
        String cleaned = input.toUpperCase().replaceAll("[\\s-]+", "");

        // Step 2a: Check Bharat (BH) Series format: YY BH #### XX
        if (cleaned.matches("^[0-9]{2}BH[0-9]{4}[A-Z]{1,2}$")) {
            return cleaned;
        }

        // Step 2b: Check Standard Indian format: SS ## XX #### or SS ## X ####
        if (cleaned.matches("^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$")) {
            // Extract the 2-letter state prefix
            String statePrefix = cleaned.substring(0, 2);

            // Verify if statePrefix is a recognized Indian State/UT
            boolean validState = false;
            for (int i = 0; i < VALID_STATES.length; i++) {
                if (VALID_STATES[i].equals(statePrefix)) {
                    validState = true;
                    break;
                }
            }

            if (validState) {
                return cleaned;
            }
        }

        // If neither matched, reject
        return null;
    }
}
