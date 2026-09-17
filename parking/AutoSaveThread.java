package parking;

/**
 * AutoSaveThread.java
 * Demonstrates basic Multithreading & Concurrency (Unit 3).
 * A simple background daemon thread that periodically saves parking data 
 * to the file every 30 seconds.
 * 
 * SYLLABUS CONCEPTS DEMONSTRATED:
 * - Unit 3: Multithreading (extending Thread class)
 * - Unit 3: Thread Lifecycle (run, sleep, interrupt)
 * - Unit 3: Concurrency handling with volatile flag
 */
public class AutoSaveThread extends Thread {

    private ParkingSlot[] slots;
    private String filename;
    private volatile boolean running = true;

    public AutoSaveThread(ParkingSlot[] slots, String filename) {
        this.slots = slots;
        this.filename = filename;
        // Setting daemon ensures the thread won't block JVM exit when Main terminates
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Sleep for 30 seconds between auto-saves
                Thread.sleep(30000);
                if (running) {
                    FileManager.saveData(slots, filename);
                }
            } catch (InterruptedException e) {
                // Thread received interrupt signal to terminate cleanly
                break;
            }
        }
    }

    /**
     * Gracefully stops the background thread.
     */
    public void stopThread() {
        this.running = false;
        this.interrupt();
    }
}
