public class Counter {

    private final Object lock = new Object();
    private boolean isCountUpDone = false; // Shared flag to coordinate threads

    public static void main(String[] args) {
        Counter app = new Counter();

        // Assign functions to threads
        Thread counterUpThread = new Thread(app::countUp);
        Thread counterDownThread = new Thread(app::countDown);

        // Start the threads
        counterUpThread.start();
        counterDownThread.start();
    }

    private void countUp() {
        synchronized (lock) {
            // Count up loop
            for (int i = 1; i < 21; i++) {
                System.out.println("Thread 1 (Counting Up): " + i);
                sleep(300); // Simulates pause
            }

            isCountUpDone = true; // Set the flag indicating completion
            lock.notify(); // Notify the second thread

            System.out.println("Thread 1 is complete!");
        } // Lock is released here
    }

    private void countDown() {
        synchronized (lock) {
            while (!isCountUpDone) { // Ensure proper handover
                try {
                    lock.wait(); // Wait for the first thread to finish
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread 2 interrupted.");
                }
            }
            // Countdown loop
            for (int i = 20; i > 0; i--) {
                System.out.println("Thread 2 (Counting Down): " + i);
                sleep(300); // Simulates work
            }

            System.out.println("Thread 2 is complete!");
        } // Lock is released here
    }

    // Method to make counting visisble
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted during sleep.");
        }
    }
}
