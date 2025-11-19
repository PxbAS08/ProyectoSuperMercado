/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

import java.util.Arrays;

public class AuthThread implements Runnable {
    public enum Action { LOGIN, REGISTER }

    private final AuthenticationService authService;
    private final String username;
    private final char[] password;
    private final Action action;
    private final AuthListener listener;

    public AuthThread(AuthenticationService authService, String username, char[] password, Action action, AuthListener listener) {
        this.authService = authService;
        this.username = username;
        this.password = password != null ? password.clone() : null;
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

            boolean result = false;
            
            // --- CORRECCIÓN AQUÍ ---
            if (action == Action.REGISTER) {
                // Ahora register pide un rol, pasamos "CLIENT" por defecto
                result = authService.register(username, password, "CLIENT");
            } else {
                // Ahora login devuelve un objeto User, no un boolean
                User user = authService.login(username, password);
                result = (user != null); // Es exitoso si el usuario no es nulo
            }
            // -----------------------

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
            if (password != null) Arrays.fill(password, '\0');
            if (listener != null) listener.onAuthProgress(100);
        }
    }
}

