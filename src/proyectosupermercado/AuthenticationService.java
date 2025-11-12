/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
import java.io.IOException; // Importa la excepción para operaciones de E/S.

public interface AuthenticationService { // Declaración de la interfaz.
    /**
     * Registra un nuevo usuario. Devuelve true si registro exitoso, false si el usuario ya existe.
     */
    boolean register(String username, char[] password) throws IOException; // Método para registrar usuarios.

    /**
     * Intenta iniciar sesión. Devuelve true si usuario/contraseña coinciden.
     */
    boolean login(String username, char[] password) throws IOException; // Método para iniciar sesión.
}


