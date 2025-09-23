/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
import java.io.IOException;

public interface AuthenticationService {
    /**
     * Registra un nuevo usuario. Devuelve true si registro exitoso, false si el usuario ya existe.
     */
    boolean register(String username, char[] password) throws IOException;

    /**
     * Intenta iniciar sesión. Devuelve true si usuario/contraseña coinciden.
     */
    boolean login(String username, char[] password) throws IOException;
}

