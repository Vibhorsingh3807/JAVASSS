package parking;

/**
 * Bike.java
 * Subclass representing a Two-Wheeler / Bike.
 * 
 * SYLLABUS CONCEPTS DEMONSTRATED:
 * - Unit 2: Inheritance (extends Vehicle)
 * - Unit 2: Method Overriding & Polymorphism (calculateFee)
 * - Unit 2: Super keyword (calling superclass constructor)
 */
public class Bike extends Vehicle {

    // Hourly rate for Bikes
    public static final double HOURLY_RATE = 20.0;

    // Constructor calling parent constructor via super()
    public Bike(String plateNumber, long entryTime) {
        super(plateNumber, "BIKE", entryTime);
    }

    /**
     * Polymorphic method implementation:
     * Calculates parking fee for a Bike at ₹20/hour (minimum ₹20).
     */
    @Override
    public double calculateFee(long durationHours) {
        // Minimum charge for 1 hour even if parked for less than an hour
        if (durationHours <= 0) {
            durationHours = 1;
        }
        return durationHours * HOURLY_RATE;
    }
}
