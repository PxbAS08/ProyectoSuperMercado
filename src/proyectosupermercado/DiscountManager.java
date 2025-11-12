/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
import java.util.*; // Importa todas las clases del paquete java.util.

/**
 * Clase que gestiona y aplica todas las reglas de descuento.
 */
public class DiscountManager { // Declaración de la clase.
    // Declara una lista para almacenar las reglas de descuento.
    private final List<DiscountRule> rules = new ArrayList<>();

    public DiscountManager() { // Constructor de la clase.
        // Regla 1: 10% en todos los lácteos (usando una expresión lambda para implementar la interfaz DiscountRule).
        rules.add((product, quantity) -> {
            // Comprueba si la categoría del producto es un lácteo.
            if ("Leche".equalsIgnoreCase(product.getCategory()) ||
                "Yogurt".equalsIgnoreCase(product.getCategory()) ||
                "Mantequilla y Margarina".equalsIgnoreCase(product.getCategory())) {
                // Si es lácteo, devuelve el 10% del precio total de esos productos.
                return product.getPrice() * 0.10 * quantity;
            }
            return 0.0; // Si no, no hay descuento.
        });

        // Regla 2: 2x1 en snacks de tipo "Pastelitos".
        rules.add((product, quantity) -> {
            // Comprueba si el producto es un pastelito.
            if ("Snacks".equalsIgnoreCase(product.getCategory()) &&
                "Pastelitos".equalsIgnoreCase(product.getSubcategory())) {
                int freeItems = quantity / 2; // Calcula cuántos productos son gratis (uno por cada dos).
                return freeItems * product.getPrice(); // Devuelve el monto a descontar.
            }
            return 0.0; // Si no, no hay descuento.
        });

        // Regla 3: 5% en productos de limpieza si se compran 3 o más.
        rules.add((product, quantity) -> {
            // Comprueba si es un producto de limpieza y la cantidad es 3 o más.
            if ("Productos de Limpieza".equalsIgnoreCase(product.getCategory()) && quantity >= 3) {
                return product.getPrice() * 0.05 * quantity; // Devuelve el 5% de descuento.
            }
            return 0.0; // Si no, no hay descuento.
        });
    }

    /**
     * Aplica todas las reglas de descuento al carrito.
     */
    public double applyDiscounts(List<Product> cart) { // Método para aplicar los descuentos.
        double discount = 0.0; // Inicializa el descuento total.
        Map<String, Integer> countByCode = new HashMap<>(); // Crea un mapa para contar productos por código.

        // Itera sobre el carrito para contar cuántos productos hay de cada tipo.
        for (Product p : cart) {
            countByCode.put(p.getCode(), countByCode.getOrDefault(p.getCode(), 0) + 1);
        }

        // Itera nuevamente sobre el carrito para aplicar las reglas.
        for (Product p : cart) {
            int qty = countByCode.get(p.getCode()); // Obtiene la cantidad de este producto.
            // Itera sobre todas las reglas de descuento.
            for (DiscountRule rule : rules) {
                discount += rule.apply(p, qty); // Acumula el descuento calculado por cada regla.
            }
            // Pone a 0 la cantidad para evitar aplicar el descuento varias veces al mismo grupo de productos.
            countByCode.put(p.getCode(), 0);
        }

        return discount; // Devuelve el descuento total.
    }
}

