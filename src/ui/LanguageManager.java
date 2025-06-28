package ui;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LanguageManager {
    private static LanguageManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;

    private LanguageManager() {
        // Bahasa default adalah Bahasa Indonesia
        setLanguage("id", "ID");
    }

    public static synchronized LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }
    
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setLanguage(String language, String country) {
        currentLocale = new Locale(language, country);
        // PERBAIKAN PENTING: Membersihkan cache untuk memaksa ResourceBundle memuat ulang file.
        ResourceBundle.clearCache();
        bundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
    }

    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            // Jika kunci tidak ditemukan, kembalikan kunci itu sendiri agar mudah di-debug
            return key;
        }
    }
}
