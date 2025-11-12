/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
public interface AuthListener { // Declaración de la interfaz.
    /**
     * Progreso en porcentaje (0-100)
     */
    void onAuthProgress(int percent); // Método para notificar el progreso.

    /**
     * Llamado cuando la operación fue exitosa.
     * @param message Mensaje descriptivo
     * @param isRegister true si fue registro, false si inicio de sesión
     */
    void onAuthSuccess(String message, boolean isRegister); // Método para notificar el éxito.

    /**
     * Llamado cuando hubo error o falla.
     */
    void onAuthFailure(String message); // Método para notificar un fallo.
}


