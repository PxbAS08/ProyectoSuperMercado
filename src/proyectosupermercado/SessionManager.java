/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser; // Ahora guardamos el objeto completo
    private String userAddress;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
        this.userAddress = null;
    }

    public void logout() {
        this.currentUser = null;
        this.userAddress = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUser() {
        return currentUser != null ? currentUser.getUsername() : null;
    }
    
    public User getUser() {
        return currentUser;
    }
       
    public void setUserAddress(String address) {
        this.userAddress = address;
    }
    
    public String getUserAddress() {
        return userAddress;
    }
}




