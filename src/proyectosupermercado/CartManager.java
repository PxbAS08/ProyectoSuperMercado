/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
import java.util.*; // Importa las utilidades de Java.

/**
 * Clase que gestiona el carrito y calcula total con descuentos.
 */
public class CartManager { // Declaración de la clase.
    // Lista sincronizada para el carrito, lo que la hace segura para el acceso concurrente desde múltiples hilos.
    private final List<Product> cart = Collections.synchronizedList(new ArrayList<>());
    private double total = 0.0; // Almacena el total de la compra.
    private double discount = 0.0; // Almacena el descuento total.

    // Crea una instancia del gestor de descuentos.
    private final DiscountManager discountManager = new DiscountManager();

    public void addToCart(Product product) { // Método para agregar un producto al carrito.
        // Lanza una excepción si el producto es nulo.
        if (product == null) throw new IllegalArgumentException("El producto no puede ser nulo.");
        // Bloque sincronizado para garantizar que solo un hilo a la vez modifique el carrito.
        synchronized (cart) {
            cart.add(product); // Agrega el producto a la lista.
        }
    }
    public void removeProduct(Product product) {
        synchronized (cart) {
            if (cart.remove(product)) {
                calculateTotal();
            }
        }
    }
    
    public List<Product> getCart() { // Método para obtener el contenido del carrito.
        synchronized (cart) { // Bloque sincronizado para una lectura segura.
            return new ArrayList<>(cart); // Devuelve una copia para evitar modificaciones externas.
        }
    }

    public synchronized double calculateTotal() { // Método sincronizado para calcular el total.
        double sum = 0.0; // Inicializa la suma.
        synchronized (cart) { // Bloque sincronizado para iterar sobre el carrito.
            for (Product p : cart) {
                sum += p.getPrice(); // Suma el precio de cada producto.
            }
        }
        // Aplica los descuentos usando el DiscountManager.
        discount = discountManager.applyDiscounts(getCart());
        total = sum - discount; // Calcula el total final restando el descuento.
        return total; // Devuelve el total.
    }

    public synchronized double getTotal() { // Método sincronizado para obtener el total ya calculado.
        return total;
    }

    public synchronized double getDiscount() { // Método sincronizado para obtener el descuento ya calculado.
        return discount;
    }

    public synchronized void clearCart() { // Método sincronizado para vaciar el carrito.
        synchronized (cart) {
            cart.clear(); // Limpia la lista.
        }
        total = 0.0; // Reinicia el total.
        discount = 0.0; // Reinicia el descuento.
    }
}



