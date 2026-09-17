package parking;

/**
 * Vehicle.java
 * Base class representing a general vehicle in the parking system.
 * 
 * SYLLABUS CONCEPTS DEMONSTRATED:
 * - Unit 1: Object-Oriented Paradigm (Entity modeling)
 * - Unit 2: Encapsulation (Private fields with public getters)
 * - Unit 2: Abstraction (Abstract class and abstract method)
 */
public abstract class Vehicle {

    // Encapsulation: Private instance variables
    private String plateNumber;
    private String vehicleType;
    private long entryTime; // Stored in epoch milliseconds for easy duration calculation

    // Parameterized Constructor
    public Vehicle(String plateNumber, String vehicleType, long entryTime) {
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.entryTime = entryTime;
    }

    // Public Getters (Encapsulation)
    public String getPlateNumber() {
        return plateNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public long getEntryTime() {
        return entryTime;
    }

    /**
     * Polymorphism Demonstration:
     * Abstract method to be implemented by child classes (Car, Bike)
     * according to their respective hourly rates.
     */
    public abstract double calculateFee(long durationHours);

    @Override
    public String toString() {
        return vehicleType + " [" + plateNumber + "]";
    }
}
