/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
public interface AuthListener {
    /**
     * Progreso en porcentaje (0-100)
     */
    void onAuthProgress(int percent);

    /**
     * Llamado cuando la operación fue exitosa.
     * @param message Mensaje descriptivo
     * @param isRegister true si fue registro, false si inicio de sesión
     */
    void onAuthSuccess(String message, boolean isRegister);

    /**
     * Llamado cuando hubo error o falla.
     */
    void onAuthFailure(String message);
}

