/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
import java.io.*; // Importa todas las clases de E/S.
import java.nio.charset.StandardCharsets; // Importa los conjuntos de caracteres estándar.
import java.security.MessageDigest; // Importa la clase para crear resúmenes de mensajes (hashes).
import java.util.Base64; // Importa la clase para codificar en Base64.
import java.util.HashMap; // Importa la clase HashMap.
import java.util.Map; // Importa la interfaz Map.

// Implementa las interfaces AuthenticationService (para la lógica) y Serializable (para poder guardar el objeto).
public class FileAuthenticationService implements AuthenticationService, Serializable {
    private static final long serialVersionUID = 1L; // ID de versión para la serialización.
    private final File file = new File("users.dat"); // Objeto File que representa el archivo de usuarios.
    // Mapa para almacenar usuarios: el nombre de usuario es la clave y la contraseña hasheada es el valor.
    private final Map<String, String> users = new HashMap<>();
    private final Object lock = new Object(); // Objeto para controlar la concurrencia (bloqueo).

    public FileAuthenticationService() { // Constructor.
        loadFromFile(); // Carga los usuarios del archivo al crear una instancia.
    }

    @SuppressWarnings("unchecked") // Suprime la advertencia de casting no comprobado.
    private void loadFromFile() { // Método para cargar usuarios desde el archivo.
        synchronized (lock) { // Bloque sincronizado para evitar problemas de concurrencia.
            if (!file.exists()) return; // Si el archivo no existe, no hace nada.
            // Bloque try-with-resources para asegurar que el stream se cierre automáticamente.
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject(); // Lee el objeto del archivo.
                if (obj instanceof Map) { // Comprueba si el objeto es un Mapa.
                    users.clear(); // Limpia el mapa actual.
                    users.putAll((Map<String, String>) obj); // Carga los usuarios leídos.
                }
            } catch (Exception e) { // Captura cualquier error durante la carga.
                System.err.println("No se pudo leer users.dat: " + e.getMessage());
            }
        }
    }

    private void saveToFile() throws IOException { // Método para guardar el mapa de usuarios en el archivo.
        synchronized (lock) { // Bloque sincronizado.
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(users); // Escribe el objeto mapa en el archivo.
            }
        }
    }

    private String hashPassword(char[] password) throws Exception { // Método para hashear una contraseña.
        MessageDigest md = MessageDigest.getInstance("SHA-256"); // Obtiene una instancia del algoritmo SHA-256.
        byte[] bytes = new String(password).getBytes(StandardCharsets.UTF_8); // Convierte la contraseña a bytes en UTF-8.
        byte[] digest = md.digest(bytes); // Calcula el hash.
        return Base64.getEncoder().encodeToString(digest); // Codifica el hash en Base64 para guardarlo como texto.
    }

    @Override
    public boolean register(String username, char[] password) throws IOException { // Implementación del método de registro.
        synchronized (lock) {
            if (users.containsKey(username.toLowerCase())) return false; // Si el usuario ya existe, devuelve falso.
            try {
                String hashed = hashPassword(password); // Hashea la contraseña.
                users.put(username.toLowerCase(), hashed); // Agrega el nuevo usuario al mapa.
                saveToFile(); // Guarda el mapa actualizado en el archivo.
                return true; // Devuelve verdadero indicando éxito.
            } catch (Exception e) { // Captura errores durante el hasheo o guardado.
                throw new IOException("Error al registrar: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean login(String username, char[] password) throws IOException { // Implementación del método de login.
        synchronized (lock) {
            String stored = users.get(username.toLowerCase()); // Obtiene la contraseña hasheada guardada.
            if (stored == null) return false; // Si el usuario no existe, devuelve falso.
            try {
                String hashed = hashPassword(password); // Hashea la contraseña proporcionada.
                return stored.equals(hashed); // Compara si los hashes coinciden.
            } catch (Exception e) { // Captura errores durante el hasheo.
                throw new IOException("Error al validar login: " + e.getMessage(), e);
            }
        }
    }
}
