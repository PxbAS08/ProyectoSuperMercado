/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
import java.util.Arrays; // Importa la clase Arrays para operaciones con arrays.

public class AuthThread implements Runnable { // Implementa Runnable.
    public enum Action { LOGIN, REGISTER } // Enumeración para definir si la acción es de login o registro.

    private final AuthenticationService authService; // El servicio de autenticación a usar.
    private final String username; // Nombre de usuario.
    private final char[] password; // Contraseña (como array de caracteres por seguridad).
    private final Action action; // La acción a realizar.
    private final AuthListener listener; // El listener para notificar los resultados.

    // Constructor que inicializa todos los atributos.
    public AuthThread(AuthenticationService authService, String username, char[] password, Action action, AuthListener listener) {
        this.authService = authService;
        this.username = username;
        // Clona el array de contraseña para evitar que se modifique externamente.
        this.password = password != null ? password.clone() : null;
        this.action = action;
        this.listener = listener;
    }

    @Override
    public void run() { // El código que se ejecuta en el hilo.
        try {
            // Simula un progreso para dar feedback visual al usuario.
            for (int p = 0; p <= 80; p += 20) {
                Thread.sleep(120); // Pequeña pausa.
                if (listener != null) listener.onAuthProgress(p); // Notifica el progreso.
            }

            boolean result; // Variable para guardar el resultado de la operación.
            if (action == Action.REGISTER) { // Si la acción es registrar...
                result = authService.register(username, password); // ...llama al método de registro.
            } else { // Si no...
                result = authService.login(username, password); // ...llama al método de login.
            }

            if (result) { // Si la operación fue exitosa...
                if (listener != null) listener.onAuthSuccess( // ...notifica el éxito.
                        action == Action.REGISTER ? "Registro exitoso." : "Inicio de sesión exitoso.",
                        action == Action.REGISTER);
            } else { // Si falló...
                if (listener != null) listener.onAuthFailure( // ...notifica el fallo.
                        action == Action.REGISTER ? "No fue posible registrar: usuario ya existe." : "Usuario o contraseña inválidos.");
            }
        } catch (Exception e) { // Captura cualquier excepción.
            if (listener != null) listener.onAuthFailure("Error: " + e.getMessage()); // Notifica el error.
        } finally { // Bloque que se ejecuta siempre, haya error o no.
            // Limpia el array de la contraseña de la memoria para mayor seguridad.
            if (password != null) Arrays.fill(password, '\0');
            if (listener != null) listener.onAuthProgress(100); // Asegura que la barra de progreso llegue al 100%.
        }
    }
}

