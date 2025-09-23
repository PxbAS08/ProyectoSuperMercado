/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
import java.util.Arrays;

public class AuthThread implements Runnable {
    public enum Action { LOGIN, REGISTER }

    private final AuthenticationService authService;
    private final String username;
    private final char[] password; // se clonó al crear
    private final Action action;
    private final AuthListener listener;

    public AuthThread(AuthenticationService authService, String username, char[] password, Action action, AuthListener listener) {
        this.authService = authService;
        this.username = username;
        this.password = password != null ? password.clone() : null; // clonar por seguridad
        this.action = action;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            // Simular progreso
            for (int p = 0; p <= 80; p += 20) {
                Thread.sleep(120);
                if (listener != null) listener.onAuthProgress(p);
            }

            boolean result;
            if (action == Action.REGISTER) {
                result = authService.register(username, password);
            } else {
                result = authService.login(username, password);
            }

            if (result) {
                if (listener != null) listener.onAuthSuccess(
                        action == Action.REGISTER ? "Registro exitoso." : "Inicio de sesión exitoso.",
                        action == Action.REGISTER);
            } else {
                if (listener != null) listener.onAuthFailure(
                        action == Action.REGISTER ? "No fue posible registrar: usuario ya existe." : "Usuario o contraseña inválidos.");
            }
        } catch (Exception e) {
            if (listener != null) listener.onAuthFailure("Error: " + e.getMessage());
        } finally {
            // Limpiar contraseña del array
            if (password != null) Arrays.fill(password, '\0');
            if (listener != null) listener.onAuthProgress(100);
        }
    }
}

