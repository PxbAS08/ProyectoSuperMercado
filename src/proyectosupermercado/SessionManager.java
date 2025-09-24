/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
public class SessionManager {
    private static SessionManager instance;
    private String currentUser;
    private String userAddress; // Nuevo campo para la dirección

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(String username) {
        this.currentUser = username;
        this.userAddress = null; // Limpiar dirección al iniciar sesión
    }

    public void logout() {
        this.currentUser = null;
        this.userAddress = null; // Limpiar dirección al cerrar sesión
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getCurrentUser() {
        return currentUser;
    }
    
    // Métodos para la dirección
    public void setUserAddress(String address) {
        this.userAddress = address;
    }
    
    public String getUserAddress() {
        return userAddress;
    }
}

