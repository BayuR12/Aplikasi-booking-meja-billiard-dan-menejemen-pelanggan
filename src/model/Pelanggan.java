package model;

public class Pelanggan {
    private int id;
    private String nama;
    private String email;
    private String password;

    public Pelanggan(int id, String nama, String email, String password) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.password = password;
    }

    public Pelanggan(int id, String nama, String email) {
        this.id = id;
        this.nama = nama;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; } // Ditambahkan
    public void setPassword(String password) { this.password = password; } // Ditambahkan
}