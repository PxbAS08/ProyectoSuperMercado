/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
public class CartCalculatorThread implements Runnable { // Implementa la interfaz Runnable para poder ejecutarse en un hilo.
    private final CartManager cartManager; // Referencia al gestor del carrito.
    private final TotalUpdateListener listener; // "Listener" para notificar cuando el cálculo esté completo.

    public interface TotalUpdateListener { // Interfaz para el callback.
        void onTotalUpdated(double newTotal, double discount); // Método que se llamará con el resultado.
    }

    // Constructor que recibe el carrito y el listener.
    public CartCalculatorThread(CartManager cartManager, TotalUpdateListener listener) {
        this.cartManager = cartManager;
        this.listener = listener;
    }

    @Override
    public void run() { // El código que se ejecutará en el nuevo hilo.
        try {
            Thread.sleep(100); // Simula un pequeño retardo, como si fuera una operación costosa.
            double total = cartManager.calculateTotal(); // Realiza el cálculo del total.
            double discount = cartManager.getDiscount(); // Obtiene el descuento calculado.
            if (listener != null) { // Si hay un listener registrado...
                // ...llama a su método para actualizar la interfaz de usuario con los nuevos valores.
                listener.onTotalUpdated(total, discount);
            }
        } catch (InterruptedException e) { // Captura si el hilo es interrumpido.
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción.
            System.err.println("Cálculo interrumpido: " + e.getMessage()); // Imprime un error.
        } catch (Exception ex) { // Captura cualquier otra excepción.
            System.err.println("Error en el cálculo del total: " + ex.getMessage());
        }
    }
}

