/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
// Importa la interfaz 'List' para poder usar listas (colecciones ordenadas de elementos).
import java.util.List;
// Importa la interfaz 'Map' para usar estructuras de clave-valor.
import java.util.Map;
// Importa la clase 'Collectors' que proporciona métodos para reducir o resumir elementos de un Stream.
import java.util.stream.Collectors;

/**
 * Gestiona el proceso de compra.
 */
// Declaración de la clase pública 'PurchaseManager'.
public class PurchaseManager {
    // Declara un campo privado y final para almacenar una instancia de CartManager. 'final' significa que solo se puede asignar una vez.
    private final CartManager cartManager;

    // Constructor público de la clase. Recibe una instancia de CartManager para poder interactuar con el carrito.
    public PurchaseManager(CartManager cartManager) {
        // Asigna la instancia de CartManager recibida al campo 'cartManager' de esta clase.
        this.cartManager = cartManager;
    }

    /**
     * Genera un resumen de la compra en formato de ticket.
     */
    // Método público que devuelve un String con el resumen de la compra.
    public String generateSummary() {
        // Obtiene la lista de todos los productos que están actualmente en el carrito.
        List<Product> items = cartManager.getCart();
        // Comprueba si la lista de productos está vacía.
        if (items.isEmpty()) {
            // Si está vacía, lanza una excepción para indicar que no se puede generar un resumen sin productos.
            throw new IllegalStateException("El carrito está vacío.");
        }

        // --- Inicio del bloque para agrupar productos ---
        // Convierte la lista de productos en un 'Stream' para procesarla de forma funcional.
        Map<Product, Long> productCounts = items.stream()
                // Utiliza el colector 'groupingBy' para agrupar los productos idénticos y 'counting' para contar cuántos hay de cada uno.
                .collect(Collectors.groupingBy(product -> product, Collectors.counting()));
        // El resultado es un Mapa donde la clave es el Producto y el valor es la cantidad de veces que aparece.

        // Crea un objeto StringBuilder para construir el texto del ticket de forma eficiente. Inicia con un encabezado.
        StringBuilder sb = new StringBuilder("SuperMercado ONIX\n\n");
        // Añade el título del resumen.
        sb.append("Resumen de compra:\n\n");
        // Añade una cabecera de tabla formateada para alinear las columnas: Cantidad, Descripción y Total.
        sb.append(String.format("%-4s %-25s %s\n", "Cant.", "Descripción", "Total"));
        // Añade una línea separadora.
        sb.append("------------------------------------------\n");

        // Itera sobre cada entrada (par producto-cantidad) en el mapa de productos agrupados.
        for (Map.Entry<Product, Long> entry : productCounts.entrySet()) {
            // Obtiene el objeto 'Product' de la entrada actual del mapa.
            Product p = entry.getKey();
            // Obtiene la cantidad (el conteo) de ese producto.
            long quantity = entry.getValue();
            // Calcula el precio total para este grupo de productos (precio unitario * cantidad).
            double itemTotal = p.getPrice() * quantity;
            // Añade una línea formateada al ticket con la cantidad, el nombre del producto, su tamaño y el total del artículo.
            sb.append(String.format("%-4d %-25s $%.2f\n", quantity, p.getName() + " (" + p.getSize() + ")", itemTotal));
        }

        // Calcula el subtotal sumando el total neto y el descuento (ya que el total del cartManager ya tiene el descuento restado).
        double subtotal = cartManager.getTotal() + cartManager.getDiscount();

        // Añade otra línea separadora.
        sb.append("------------------------------------------\n");
        // Añade el subtotal formateado.
        sb.append(String.format("\nSubtotal: $%.2f", subtotal));
        // Añade el descuento total formateado.
        sb.append(String.format("\nDescuento: $%.2f", cartManager.getDiscount()));
        // Añade el total final a pagar formateado.
        sb.append(String.format("\nTotal a pagar: $%.2f", cartManager.getTotal()));

        // --- Bloque para agregar la dirección de entrega ---
        // Obtiene la dirección del usuario desde el SessionManager.
        String address = SessionManager.getInstance().getUserAddress();
        // Comprueba si la dirección existe y no está vacía.
        if (address != null && !address.trim().isEmpty()) {
            // Comprueba si el cliente eligió recoger el pedido en la tienda.
            if (address.equalsIgnoreCase("Recoger en tienda")) {
                // Si es así, añade un mensaje indicándolo.
                sb.append("\n\nSu pedido estará listo para recoger en la tienda.");
            } else {
                // Si no, añade un mensaje indicando que el pedido será entregado en la dirección proporcionada.
                sb.append("\n\nSu pedido se entregará en la siguiente dirección:\n").append(address);
            }
        }

        // Convierte el contenido del StringBuilder a un String y lo devuelve.
        return sb.toString();
    }

    /**
     * Limpia el carrito después de la compra.
     */
    // Método público que no devuelve nada (void).
    public void clearCart() {
        // Llama al método clearCart() del objeto cartManager para vaciar el carrito.
        cartManager.clearCart();
    }
}


