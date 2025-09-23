/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
import java.util.*;

/**
 * Clase que gestiona y aplica todas las reglas de descuento.
 */
public class DiscountManager {
    private final List<DiscountRule> rules = new ArrayList<>();

    public DiscountManager() {
        // Regla 1: 10% en todos los lácteos
        rules.add((product, quantity) -> {
            if ("Leche".equalsIgnoreCase(product.getCategory()) ||
                "Yogurt".equalsIgnoreCase(product.getCategory()) ||
                "Mantequilla y Margarina".equalsIgnoreCase(product.getCategory())) {
                return product.getPrice() * 0.10 * quantity;
            }
            return 0.0;
        });

        // Regla 2: 2x1 en snacks de tipo "Pastelitos"
        rules.add((product, quantity) -> {
            if ("Snacks".equalsIgnoreCase(product.getCategory()) &&
                "Pastelitos".equalsIgnoreCase(product.getSubcategory())) {
                int freeItems = quantity / 2; // 1 gratis por cada 2
                return freeItems * product.getPrice();
            }
            return 0.0;
        });

        // Regla 3: 5% en productos de limpieza si compra más de 3
        rules.add((product, quantity) -> {
            if ("Productos de Limpieza".equalsIgnoreCase(product.getCategory()) && quantity >= 3) {
                return product.getPrice() * 0.05 * quantity;
            }
            return 0.0;
        });
    }

    /**
     * Aplica todas las reglas de descuento al carrito.
     */
    public double applyDiscounts(List<Product> cart) {
        double discount = 0.0;
        Map<String, Integer> countByCode = new HashMap<>();

        for (Product p : cart) {
            countByCode.put(p.getCode(), countByCode.getOrDefault(p.getCode(), 0) + 1);
        }

        for (Product p : cart) {
            int qty = countByCode.get(p.getCode());
            for (DiscountRule rule : rules) {
                discount += rule.apply(p, qty);
            }
            countByCode.put(p.getCode(), 0); // evitar duplicar descuento
        }

        return discount;
    }
}

