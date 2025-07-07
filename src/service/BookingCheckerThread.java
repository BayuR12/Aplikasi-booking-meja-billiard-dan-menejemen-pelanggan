package service;

public class BookingCheckerThread extends Thread {
    private volatile boolean running = true;

    
    public BookingCheckerThread() {
    }

    
    public void stopThread() {
        running = false;
        interrupt();
    }

    @Override
    public void run() {
        while (running) {
            try {
                System.out.println("Booking checker thread is running...");
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                running = false;
                System.out.println("Booking checker thread was interrupted and is stopping.");
            }
        }
        System.out.println("Booking checker thread has stopped.");
    }
}
