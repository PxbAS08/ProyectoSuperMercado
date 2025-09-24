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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestiona el proceso de compra.
 */
public class PurchaseManager {
    private final CartManager cartManager;

    public PurchaseManager(CartManager cartManager) {
        this.cartManager = cartManager;
    }

    /**
     * Genera un resumen de la compra en formato de ticket.
     */
    public String generateSummary() {
        List<Product> items = cartManager.getCart();
        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío.");
        }

        // Agrupar productos por nombre y contar la cantidad de cada uno
        Map<Product, Long> productCounts = items.stream()
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));

        StringBuilder sb = new StringBuilder("SuperMercado ONIX\n\n");
        sb.append("Resumen de compra:\n\n");
        sb.append(String.format("%-4s %-25s %s\n", "Cant.", "Descripción", "Total"));
        sb.append("------------------------------------------\n");

        for (Map.Entry<Product, Long> entry : productCounts.entrySet()) {
            Product p = entry.getKey();
            long quantity = entry.getValue();
            double itemTotal = p.getPrice() * quantity;
            sb.append(String.format("%-4d %-25s $%.2f\n", quantity, p.getName() + " (" + p.getSize() + ")", itemTotal));
        }

        double subtotal = cartManager.getTotal() + cartManager.getDiscount();

        sb.append("------------------------------------------\n");
        sb.append(String.format("\nSubtotal: $%.2f", subtotal));
        sb.append(String.format("\nDescuento: $%.2f", cartManager.getDiscount()));
        sb.append(String.format("\nTotal a pagar: $%.2f", cartManager.getTotal()));

        // Agregar el mensaje de la dirección
        String address = SessionManager.getInstance().getUserAddress();
        if (address != null && !address.trim().isEmpty()) {
            if (address.equalsIgnoreCase("Recoger en tienda")) {
                sb.append("\n\nSu pedido estará listo para recoger en la tienda.");
            } else {
                sb.append("\n\nSu pedido se entregará en la siguiente dirección:\n").append(address);
            }
        }

        return sb.toString();
    }

    /**
     * Limpia el carrito después de la compra.
     */
    public void clearCart() {
        cartManager.clearCart();
    }
}

