package model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int meja;
    private int pelangganId;
    private LocalDateTime waktu;

    public Booking(int id, int meja, int pelangganId, LocalDateTime waktu) {
        this.id = id;
        this.meja = meja;
        this.pelangganId = pelangganId;
        this.waktu = waktu;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMeja() { return meja; }
    public void setMeja(int meja) { this.meja = meja; }
    public int getPelangganId() { return pelangganId; }
    public void setPelangganId(int pelangganId) { this.pelangganId = pelangganId; }
    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }
}