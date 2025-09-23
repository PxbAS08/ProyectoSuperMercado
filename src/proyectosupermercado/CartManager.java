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
 * Clase que gestiona el carrito y calcula total con descuentos.
 */
public class CartManager {
    private final List<Product> cart = Collections.synchronizedList(new ArrayList<>());
    private double total = 0.0;
    private double discount = 0.0;

    private final DiscountManager discountManager = new DiscountManager();

    public void addToCart(Product product) {
        if (product == null) throw new IllegalArgumentException("El producto no puede ser nulo.");
        synchronized (cart) {
            cart.add(product);
        }
    }

    public List<Product> getCart() {
        synchronized (cart) {
            return new ArrayList<>(cart);
        }
    }

    public synchronized double calculateTotal() {
        double sum = 0.0;
        synchronized (cart) {
            for (Product p : cart) {
                sum += p.getPrice();
            }
        }
        discount = discountManager.applyDiscounts(getCart());
        total = sum - discount;
        return total;
    }

    public synchronized double getTotal() {
        return total;
    }

    public synchronized double getDiscount() {
        return discount;
    }

    public synchronized void clearCart() {
        synchronized (cart) {
            cart.clear();
        }
        total = 0.0;
        discount = 0.0;
    }
}



