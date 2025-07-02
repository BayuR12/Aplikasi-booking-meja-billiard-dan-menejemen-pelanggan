package service;

public class BookingCheckerThread extends Thread {
    // volatile memastikan variabel ini selalu dibaca dari memori utama
    private volatile boolean running = true;

    
    public BookingCheckerThread() {
        // Konstruktor ini sekarang kosong dan aman untuk dipanggil.
    }

    
    public void stopThread() {
        running = false;
        // Interupsi thread jika sedang dalam kondisi sleep
        interrupt();
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Anda bisa menambahkan logika pengecekan booking di sini nanti
                System.out.println("Booking checker thread is running...");
                Thread.sleep(30000); // Cek setiap 30 detik
            } catch (InterruptedException e) {
                // Jika diinterupsi, hentikan loop
                running = false;
                System.out.println("Booking checker thread was interrupted and is stopping.");
            }
        }
        System.out.println("Booking checker thread has stopped.");
    }
}
