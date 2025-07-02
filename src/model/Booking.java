// src/model/Booking.java
package model;

import java.time.LocalDateTime;

public class Booking {
    private String id; // Diubah dari int ke String
    private int meja;
    private int pelangganId;
    private LocalDateTime waktu;

    // Konstruktor untuk membuat booking baru (ID akan dibuat oleh DB)
    public Booking(int meja, int pelangganId, LocalDateTime waktu) {
        this.meja = meja;
        this.pelangganId = pelangganId;
        this.waktu = waktu;
    }
    
    // Konstruktor untuk booking yang sudah ada dari DB
    public Booking(String id, int meja, int pelangganId, LocalDateTime waktu) {
        this.id = id;
        this.meja = meja;
        this.pelangganId = pelangganId;
        this.waktu = waktu;
    }

    // Getters dan Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public int getMeja() { return meja; }
    public void setMeja(int meja) { this.meja = meja; }
    
    public int getPelangganId() { return pelangganId; }
    public void setPelangganId(int pelangganId) { this.pelangganId = pelangganId; }
    
    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }
}