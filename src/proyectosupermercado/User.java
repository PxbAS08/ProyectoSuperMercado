package proyectosupermercado;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 2L; // Actualizamos versión
    private final String username;
    private final String password;
    private final String role;
    private double walletBalance;
    private CardInfo savedCard; // Nuevo campo para la tarjeta

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.walletBalance = 0.0;
        this.savedCard = null;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public double getWalletBalance() { return walletBalance; }

    public void addBalance(double amount) {
        this.walletBalance += amount;
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    // Métodos para la tarjeta
    public CardInfo getSavedCard() { return savedCard; }
    public void setSavedCard(CardInfo savedCard) { this.savedCard = savedCard; }
}