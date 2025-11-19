/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
import java.io.IOException;

public interface AuthenticationService {
    boolean register(String username, char[] password, String role) throws IOException;
    User login(String username, char[] password) throws IOException;
    void updateUser(User user) throws IOException; // Para guardar el saldo nuevo
}


