package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private int meja;
    private int pelangganId;
    private LocalDateTime waktu;

    public Booking(int meja, int pelangganId, LocalDateTime waktu) {
        this.meja = meja;
        this.pelangganId = pelangganId;
        this.waktu = waktu;
    }

    public Booking(String id, int meja, int pelangganId, LocalDateTime waktu) {
        this.id = id;
        this.meja = meja;
        this.pelangganId = pelangganId;
        this.waktu = waktu;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public int getMeja() { return meja; }
    public void setMeja(int meja) { this.meja = meja; }
    
    public int getPelangganId() { return pelangganId; }
    public void setPelangganId(int pelangganId) { this.pelangganId = pelangganId; }
    
    public LocalDateTime getWaktu() { return waktu; }
    public void setWaktu(LocalDateTime waktu) { this.waktu = waktu; }
}