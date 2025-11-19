/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

import java.io.*;
import java.util.*;

/**
 * Gestiona el stock de los productos (Singleton) con persistencia en disco.
 */
public class InventoryManager {
    private static InventoryManager instance;
    private final File inventoryFile = new File("inventory.dat");
    
    // Mapa: Código del producto -> Cantidad en stock
    private final Map<String, Integer> stockMap = new HashMap<>();
    // Mapa: Código del producto -> Cantidad dañada/caducada
    private final Map<String, Integer> wasteMap = new HashMap<>();
    
    private final Object lock = new Object();

    private InventoryManager() {
        // Intentar cargar del archivo. Si falla o no existe, inicializar con valores aleatorios.
        if (!loadFromDisk()) {
            initializeRandomStock();
        }
    }

    public static synchronized InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }

    /**
     * Carga los datos desde el archivo inventory.dat.
     * @return true si se cargó correctamente, false si no existía o falló.
     */
    @SuppressWarnings("unchecked")
    private boolean loadFromDisk() {
        synchronized (lock) {
            if (!inventoryFile.exists()) return false;
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inventoryFile))) {
                // Leemos los dos mapas en el mismo orden en que los guardamos
                Map<String, Integer> loadedStock = (Map<String, Integer>) ois.readObject();
                Map<String, Integer> loadedWaste = (Map<String, Integer>) ois.readObject();
                
                stockMap.clear();
                stockMap.putAll(loadedStock);
                
                wasteMap.clear();
                wasteMap.putAll(loadedWaste);
                
                return true;
            } catch (Exception e) {
                System.err.println("No se pudo cargar el inventario: " + e.getMessage());
                return false;
            }
        }
    }

    /**
     * Guarda el estado actual de los mapas en el archivo inventory.dat.
     */
    private void saveToDisk() {
        synchronized (lock) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(inventoryFile))) {
                oos.writeObject(stockMap);
                oos.writeObject(wasteMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeRandomStock() {
        // Inicializar con stock simulado solo la primera vez
        List<Product> allProducts = ProductCatalog.getAllProducts();
        Random rand = new Random();
        for (Product p : allProducts) {
            stockMap.put(p.getCode(), 20 + rand.nextInt(81));
            wasteMap.put(p.getCode(), 0); 
        }
        saveToDisk(); // Guardar el estado inicial
    }

    public int getStock(String productCode) {
        synchronized (lock) {
            return stockMap.getOrDefault(productCode, 0);
        }
    }

    /**
     * Reduce el stock (compra realizada).
     */
    public void reduceStock(String productCode, int quantity) {
        synchronized (lock) {
            int current = stockMap.getOrDefault(productCode, 0);
            if (current >= quantity) {
                stockMap.put(productCode, current - quantity);
                saveToDisk(); // Guardar cambios
            }
        }
    }

    /**
     * Reabastece el stock (devolución de producto en buen estado).
     */
    public void restock(String productCode, int quantity) {
        synchronized (lock) {
            int current = stockMap.getOrDefault(productCode, 0);
            stockMap.put(productCode, current + quantity);
            saveToDisk(); // Guardar cambios
        }
    }

    /**
     * Registra una merma (devolución de producto dañado/caducado).
     */
    public void addWaste(String productCode, int quantity) {
        synchronized (lock) {
            int current = wasteMap.getOrDefault(productCode, 0);
            wasteMap.put(productCode, current + quantity);
            saveToDisk(); // Guardar cambios
        }
    }

    public List<Object[]> getInventoryData() {
        // Simular retardo para ver el efecto del Hilo
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<Object[]> data = new ArrayList<>();
        synchronized (lock) {
            List<Product> products = ProductCatalog.getAllProducts();
            for (Product p : products) {
                int quantity = stockMap.getOrDefault(p.getCode(), 0);
                int waste = wasteMap.getOrDefault(p.getCode(), 0);
                
                // Crear fila: Código, Nombre, Categoría, Precio, Stock, Mermas
                data.add(new Object[]{
                    p.getCode(),
                    p.getName() + " (" + p.getSize() + ")",
                    p.getCategory(),
                    String.format("$%.2f", p.getPrice()),
                    quantity,
                    waste 
                });
            }
        }
        return data;
    }
}