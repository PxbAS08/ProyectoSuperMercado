package proyectosupermercado;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PurchaseHistoryManager {
    
    private static PurchaseHistoryManager instance;
    private final File historyFile = new File("history.dat");
    private final List<PurchaseRecord> purchaseHistory = new ArrayList<>();
    private final Object lock = new Object();

    private PurchaseHistoryManager() {
        loadHistoryFromFile();
    }

    public static synchronized PurchaseHistoryManager getInstance() {
        if (instance == null) {
            instance = new PurchaseHistoryManager();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    private void loadHistoryFromFile() {
        synchronized (lock) {
            if (!historyFile.exists()) return;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(historyFile))) {
                Object obj = ois.readObject();
                if (obj instanceof List) {
                    purchaseHistory.clear();
                    purchaseHistory.addAll((List<PurchaseRecord>) obj);
                }
            } catch (Exception e) {
                System.err.println("No se pudo leer history.dat: " + e.getMessage());
            }
        }
    }

    public void saveHistoryToFile() { // Ahora es público para guardar actualizaciones (cancelaciones)
        synchronized (lock) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(historyFile))) {
                oos.writeObject(purchaseHistory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPurchase(PurchaseRecord record) {
        synchronized (lock) {
            purchaseHistory.add(record);
            saveHistoryToFile();
        }
    }

    public List<PurchaseRecord> getHistory(User user) {
        synchronized (lock) {
            if (user.isAdmin()) {
                return new ArrayList<>(purchaseHistory); // Admin ve todo
            } else {
                // Cliente solo ve lo suyo
                return purchaseHistory.stream()
                        .filter(p -> p.getUser().equalsIgnoreCase(user.getUsername()))
                        .collect(Collectors.toList());
            }
        }
    }
}