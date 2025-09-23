/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {
    private static final List<Product> products = new ArrayList<>();

    static {
        // Leche
        products.add(new Product("L1", "Lala", "Leche", "Entera", "1L", 28.50));
        products.add(new Product("L2", "Santa Clara", "Leche", "Entera", "1L (6 piezas)", 230.00));
        products.add(new Product("L3", "Alpura", "Leche", "Entera", "1L (6 piezas)", 180.00));
        products.add(new Product("L4", "Nutrileche", "Leche", "Entera", "1L", 25.00));

        products.add(new Product("L5", "Lala", "Leche", "Deslactosada", "1L (6 piezas)", 159.00));
        products.add(new Product("L6", "Alpura", "Leche", "Deslactosada", "1L", 30.00));
        products.add(new Product("L7", "Santa Clara", "Leche", "Deslactosada", "1L", 40.00));

        products.add(new Product("L8", "Lala Yomi Vainilla", "Leche", "Saborizada", "180ml", 10.00));
        products.add(new Product("L9", "Lala Yomi Chocolate", "Leche", "Saborizada", "180ml", 10.00));
        products.add(new Product("L10", "Lala Yomi Fresa", "Leche", "Saborizada", "180ml", 10.00));
        products.add(new Product("L11", "Alpura Vainilla", "Leche", "Saborizada", "180ml", 11.00));
        products.add(new Product("L12", "Alpura Fresa", "Leche", "Saborizada", "180ml", 11.00));
        products.add(new Product("L13", "Alpura Chocolate", "Leche", "Saborizada", "180ml", 11.00));
        products.add(new Product("L14", "Santa Clara Vainilla", "Leche", "Saborizada", "180ml", 13.00));
        products.add(new Product("L15", "Santa Clara Chocolate", "Leche", "Saborizada", "180ml", 13.00));
        products.add(new Product("L16", "Santa Clara Fresa", "Leche", "Saborizada", "180ml", 13.00));

        // Yogurt
        products.add(new Product("Y1", "Lala Fresa", "Yogurt", "Bebible", "220g (8 piezas)", 70.00));
        products.add(new Product("Y2", "Alpura Natural", "Yogurt", "Natural", "1kg", 42.00));
        products.add(new Product("Y3", "Danone Griego", "Yogurt", "Griego", "150g", 18.00));

        // Mantequilla y Margarina
        products.add(new Product("M1", "Lala sin sal", "Mantequilla y Margarina", "Mantequilla", "90g", 24.00));
        products.add(new Product("M2", "Primavera", "Mantequilla y Margarina", "Margarina", "225g", 18.00));

        // 🍪 Snacks – Galletas
        products.add(new Product("S1", "Marinela Canelitas", "Snacks", "Galletas", "300g", 37.90));
        products.add(new Product("S2", "Chokiees", "Snacks", "Galletas", "300g", 107.00));
        products.add(new Product("S3", "Sponch", "Snacks", "Galletas", "700g (4 paquetes)", 79.50));
        products.add(new Product("S4", "Pasticetas", "Snacks", "Galletas", "400g", 65.90));
        products.add(new Product("S5", "Surtido Marinela", "Snacks", "Galletas", "450g", 73.50));

        // 🍟 Snacks – Botanas
        products.add(new Product("S6", "Sabritas Clasicas", "Snacks", "Botanas", "45g", 20.00));
        products.add(new Product("S6", "Sabritas Flaming Hot", "Snacks", "Botanas", "45g", 20.00));
        products.add(new Product("S6", "Sabritas Adobadas", "Snacks", "Botanas", "45g", 20.00));
        products.add(new Product("S7", "Doritos Nacho", "Snacks", "Botanas", "75g", 18.00));
        products.add(new Product("S8", "Doritos Pizzerola", "Snacks", "Botanas", "35g", 18.00));
        products.add(new Product("S9", "Cheetos Torciditos", "Snacks", "Botanas", "80g", 15.00));
        products.add(new Product("S9", "Cheetos Poffs", "Snacks", "Botanas", "80g", 15.00));
        products.add(new Product("S9", "Cheetos Flaming Hot", "Snacks", "Botanas", "80g", 15.00));
        products.add(new Product("S10", "Cacahuates Japoneses", "Snacks", "Botanas", "70g", 20.00));

        // 🍰 Snacks – Pastelitos
        products.add(new Product("S11", "Gansito Marinela", "Snacks", "Pastelitos", "50g", 20.90));
        products.add(new Product("S12", "Pingüinos Marinela", "Snacks", "Pastelitos", "80g", 27.90));
        products.add(new Product("S13", "Choco Roles Marinela", "Snacks", "Pastelitos", "122g (2 piezas)", 27.90));
        products.add(new Product("S14", "Gansito Marinela 3 Piezas", "Snacks", "Pastelitos", "Paquete", 50.90));

        // 🧴 Productos de Limpieza
        products.add(new Product("PL1", "Pinol El Original", "Productos de Limpieza", "Multiusos", "5.1L", 179.00));
        products.add(new Product("PL2", "Fabuloso", "Productos de Limpieza", "Multiusos", "6L", 199.00));
        products.add(new Product("PL3", "Cloralex", "Productos de Limpieza", "Cloro", "1L", 68.00));
        products.add(new Product("PL4", "Clorox", "Productos de Limpieza", "Cloro", "1L", 65.00));
        products.add(new Product("PL5", "Vanish", "Productos de Limpieza", "Quitamanchas", "1L", 90.00));

        // Detergentes
        products.add(new Product("D1", "Ariel Líquido Poder y Cuidado", "Productos de Limpieza", "Detergentes", "8.5L", 374.25));
        products.add(new Product("D2", "Persil en Polvo Ropa Color", "Productos de Limpieza", "Detergentes", "9kg", 439.00));
        products.add(new Product("D3", "Ariel Líquido Color", "Productos de Limpieza", "Detergentes", "2.8L (45 lavadas)", 149.00));
        products.add(new Product("D4", "Ariel en Polvo con Downy", "Productos de Limpieza", "Detergentes", "750g", 35.00));
        products.add(new Product("D5", "Ariel Expert Líquido", "Productos de Limpieza", "Detergentes", "5L (80 lavadas)", 194.90));

        // Lavatrastes
        products.add(new Product("LT1", "Salvo Limón Líquido", "Productos de Limpieza", "Lavatrastes", "1.4L", 69.00));
        products.add(new Product("LT2", "Salvo Polvo", "Productos de Limpieza", "Lavatrastes", "1kg", 39.00));
        products.add(new Product("LT3", "Salvo Lavatrastes Limón", "Productos de Limpieza", "Lavatrastes", "900ml", 55.00));
        products.add(new Product("LT4", "Salvo Lavatrastes Limón", "Productos de Limpieza", "Lavatrastes", "500ml", 32.90));

        // 🥤 Bebidas (categoría extra)
        products.add(new Product("B1", "Coca-Cola", "Bebidas", "Refresco", "355ml lata", 14.00));
        products.add(new Product("B2", "Coca-Cola", "Bebidas", "Refresco", "600ml botella", 20.00));
        products.add(new Product("B3", "Pepsi", "Bebidas", "Refresco", "2L botella", 32.00));
        products.add(new Product("B4", "Agua Bonafont", "Bebidas", "Agua", "1.5L botella", 18.00));
        products.add(new Product("B5", "Jugo Del Valle Naranja", "Bebidas", "Jugo", "1L tetrapack", 28.00));
    }

    public static List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
}

