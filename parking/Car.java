package parking;

/**
 * Car.java
 * Subclass representing a Car.
 * 
 * SYLLABUS CONCEPTS DEMONSTRATED:
 * - Unit 2: Inheritance (extends Vehicle)
 * - Unit 2: Method Overriding & Polymorphism (calculateFee)
 * - Unit 2: Super keyword (calling superclass constructor)
 */
public class Car extends Vehicle {

    // Hourly rate for Cars
    public static final double HOURLY_RATE = 50.0;

    // Constructor calling parent constructor via super()
    public Car(String plateNumber, long entryTime) {
        super(plateNumber, "CAR", entryTime);
    }

    /**
     * Polymorphic method implementation:
     * Calculates parking fee for a Car at ₹50/hour (minimum ₹50).
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
