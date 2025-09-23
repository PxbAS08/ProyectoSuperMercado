/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
import java.util.List;

/**
 * Gestiona el proceso de compra.
 */
public class PurchaseManager {
    private final CartManager cartManager;

    public PurchaseManager(CartManager cartManager) {
        this.cartManager = cartManager;
    }

    /**
     * Genera un resumen de la compra.
     */
    public String generateSummary() {
        List<Product> items = cartManager.getCart();
        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío.");
        }

        StringBuilder sb = new StringBuilder("Resumen de compra:\n");
        for (Product p : items) {
            sb.append("- ").append(p.toString()).append("\n");
        }
        sb.append("\nSubtotal: $").append(String.format("%.2f", cartManager.getTotal() + cartManager.getDiscount()));
        sb.append("\nDescuento: $").append(String.format("%.2f", cartManager.getDiscount()));
        sb.append("\nTotal a pagar: $").append(String.format("%.2f", cartManager.getTotal()));

        return sb.toString();
    }

    /**
     * Limpia el carrito después de la compra.
     */
    public void clearCart() {
        cartManager.clearCart();
    }
}

