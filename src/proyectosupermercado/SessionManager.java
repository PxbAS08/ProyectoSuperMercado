/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
public class SessionManager { // Declaración de la clase.
    private static SessionManager instance; // La única instancia de la clase (patrón Singleton).
    private String currentUser; // Almacena el nombre del usuario que ha iniciado sesión.
    private Object userAddress;

    private SessionManager() {} // Constructor privado para evitar que se creen nuevas instancias desde fuera.

    public static synchronized SessionManager getInstance() { // Método público para obtener la única instancia.
        if (instance == null) { // Si la instancia aún no ha sido creada...
            instance = new SessionManager(); // ...se crea.
        }
        return instance; // Devuelve la instancia.
    }

    public void login(String username) { // Método para establecer el usuario actual.
        this.currentUser = username;
        this.userAddress = null; // Limpiar dirección al iniciar sesión
    }

    public void logout() { // Método para cerrar la sesión.
        this.currentUser = null;
        this.userAddress = null; // Limpiar dirección al cerrar sesión
    }

    public boolean isLoggedIn() { // Método para comprobar si hay una sesión activa.
        return currentUser != null;
    }

    public String getCurrentUser() { // Método para obtener el nombre del usuario actual.
        return currentUser;
    }
       
    // Métodos para la dirección
    public void setUserAddress(String address) {
        this.userAddress = address;
    }
    
    public String getUserAddress() {
        return (String) userAddress;
    }
}




