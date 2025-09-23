/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
public class CartCalculatorThread implements Runnable {
    private final CartManager cartManager;
    private final TotalUpdateListener listener;

    public interface TotalUpdateListener {
        void onTotalUpdated(double newTotal, double discount);
    }

    public CartCalculatorThread(CartManager cartManager, TotalUpdateListener listener) {
        this.cartManager = cartManager;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(100); // simular retardo
            double total = cartManager.calculateTotal();
            double discount = cartManager.getDiscount();
            if (listener != null) {
                listener.onTotalUpdated(total, discount);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Cálculo interrumpido: " + e.getMessage());
        } catch (Exception ex) {
            System.err.println("Error en el cálculo del total: " + ex.getMessage());
        }
    }
}


