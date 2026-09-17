package parking;

/**
 * ParkingSlot.java
 * Represents an individual parking slot in the parking lot.
 * 
 * SYLLABUS CONCEPTS DEMONSTRATED:
 * - Unit 1: Dynamic Object Creation
 * - Unit 2: Encapsulation (Private fields with getters/setters)
 * - Unit 2: Association/Composition (Slot holds a Vehicle reference)
 */
public class ParkingSlot {

    // Encapsulation: Private fields
    private String slotId;
    private boolean occupied;
    private Vehicle vehicle;

    // Constructor to initialize slot with an ID
    public ParkingSlot(String slotId) {
        this.slotId = slotId;
        this.occupied = false;
        this.vehicle = null;
    }

    // Public Getters
    public String getSlotId() {
        return slotId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    // Method to park a vehicle in this slot
    public void park(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.occupied = true;
    }

    // Method to vacate this slot
    public void vacate() {
        this.vehicle = null;
        this.occupied = false;
    }

    /**
     * Formats slot state for grid display.
     * Examples:
     * - Free: "[A1: FREE]"
     * - Occupied: "[A1:DL01AB1234]"
     */
    public String getDisplayString() {
        if (!occupied || vehicle == null) {
            return String.format("[%s: FREE]", slotId);
        } else {
            return String.format("[%s:%s]", slotId, vehicle.getPlateNumber());
        }
    }
}
