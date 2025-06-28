package service;

public class BookingCheckerThread extends Thread {
    private final int meja;

    public BookingCheckerThread(int meja) {
        this.meja = meja;
    }

    public BookingCheckerThread() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Checking availability for meja: " + meja);
                Thread.sleep(10000); // 10 detik interval
            } catch (InterruptedException e) {
                System.out.println("Checker interrupted");
                break;
            }
        }
    }
}