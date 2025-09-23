/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FileAuthenticationService implements AuthenticationService, Serializable {
    private static final long serialVersionUID = 1L;
    private final File file = new File("users.dat");
    // username -> hashedPassword (Base64)
    private final Map<String, String> users = new HashMap<>();
    private final Object lock = new Object();

    public FileAuthenticationService() {
        loadFromFile();
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        synchronized (lock) {
            if (!file.exists()) return;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof Map) {
                    users.clear();
                    users.putAll((Map<String, String>) obj);
                }
            } catch (Exception e) {
                System.err.println("No se pudo leer users.dat: " + e.getMessage());
            }
        }
    }

    private void saveToFile() throws IOException {
        synchronized (lock) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(users);
            }
        }
    }

    private String hashPassword(char[] password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = new String(password).getBytes(StandardCharsets.UTF_8);
        byte[] digest = md.digest(bytes);
        return Base64.getEncoder().encodeToString(digest);
    }

    @Override
    public boolean register(String username, char[] password) throws IOException {
        synchronized (lock) {
            if (users.containsKey(username)) return false;
            try {
                String hashed = hashPassword(password);
                users.put(username, hashed);
                saveToFile();
                return true;
            } catch (Exception e) {
                throw new IOException("Error al registrar: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public boolean login(String username, char[] password) throws IOException {
        synchronized (lock) {
            String stored = users.get(username);
            if (stored == null) return false;
            try {
                String hashed = hashPassword(password);
                return stored.equals(hashed);
            } catch (Exception e) {
                throw new IOException("Error al validar login: " + e.getMessage(), e);
            }
        }
    }
}

