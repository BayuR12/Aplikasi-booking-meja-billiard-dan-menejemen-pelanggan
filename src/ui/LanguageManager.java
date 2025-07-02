// File: ui/LanguageManager.java
package ui;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LanguageManager {
    private static final LanguageManager INSTANCE = new LanguageManager();
    private ResourceBundle bundle;

    private LanguageManager() {
        // Atur bahasa default saat pertama kali dijalankan
        setLanguage("id", "ID"); 
    }

    public static LanguageManager getInstance() {
        return INSTANCE;
    }

    public void setLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        // Ini adalah baris KUNCI: Muat ulang resource bundle dengan locale yang baru
        bundle = ResourceBundle.getBundle("messages", locale); 
    }

    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            // Jika key tidak ditemukan, kembalikan key itu sendiri agar mudah di-debug
            return key;
        }
    }

    void setLocale(Locale locale) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void changeLanguage(String langCode, String countryCode) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}