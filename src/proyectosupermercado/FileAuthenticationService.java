package proyectosupermercado;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FileAuthenticationService implements AuthenticationService, Serializable {
    private static final long serialVersionUID = 1L;
    private final File file = new File("users.dat");
    // Mapa: Username -> Objeto User
    private final Map<String, User> users = new HashMap<>();
    private final Object lock = new Object();

    public FileAuthenticationService() {
        loadFromFile();
        // Crear admin por defecto si no existe
        if (!users.containsKey("admin")) {
            try {
                register("admin", "admin123".toCharArray(), "ADMIN");
                System.out.println("Usuario ADMIN creado por defecto (user: admin, pass: admin123)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        synchronized (lock) {
            if (!file.exists()) return;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof Map) {
                    users.clear();
                    users.putAll((Map<String, User>) obj);
                }
            } catch (Exception e) {
                System.err.println("No se pudo leer users.dat (quizás formato antiguo): " + e.getMessage());
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
    public boolean register(String username, char[] password, String role) throws IOException {
        synchronized (lock) {
            if (users.containsKey(username.toLowerCase())) return false;
            try {
                String hashed = hashPassword(password);
                User newUser = new User(username.toLowerCase(), hashed, role);
                users.put(username.toLowerCase(), newUser);
                saveToFile();
                return true;
            } catch (Exception e) {
                throw new IOException("Error al registrar: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public User login(String username, char[] password) throws IOException {
        synchronized (lock) {
            User storedUser = users.get(username.toLowerCase());
            if (storedUser == null) return null;
            try {
                String hashed = hashPassword(password);
                if (storedUser.getPassword().equals(hashed)) {
                    return storedUser;
                }
                return null;
            } catch (Exception e) {
                throw new IOException("Error al validar login: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void updateUser(User user) throws IOException {
        synchronized (lock) {
            users.put(user.getUsername(), user);
            saveToFile();
        }
    }
}