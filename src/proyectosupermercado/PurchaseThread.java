/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
/**
 * Hilo que simula el proceso de pago.
 */
// Define la clase pública 'PurchaseThread' que implementa la interfaz 'Runnable', permitiendo que su código se ejecute en un hilo separado.
public class PurchaseThread implements Runnable {
    // Declara un campo privado y final para almacenar una instancia de PurchaseManager, que contiene la lógica de la compra.
    private final PurchaseManager purchaseManager;
    // Declara un campo privado y final para un "listener", que notificará a la interfaz de usuario sobre el resultado de la compra.
    private final PurchaseListener listener;

    // Define una interfaz pública anidada 'PurchaseListener' que establece un contrato para los callbacks (notificaciones).
    public interface PurchaseListener {
        // Declara un método que se llamará cuando la compra se complete con éxito.
        void onPurchaseSuccess(String message);
        // Declara un método que se llamará si la compra falla.
        void onPurchaseFailure(String error);
    }

    // Constructor público para la clase PurchaseThread.
    public PurchaseThread(PurchaseManager purchaseManager, PurchaseListener listener) {
        // Asigna la instancia de PurchaseManager recibida al campo 'purchaseManager' de la clase.
        this.purchaseManager = purchaseManager;
        // Asigna la instancia de PurchaseListener recibida al campo 'listener' de la clase.
        this.listener = listener;
    }

    // Sobrescribe el método 'run' de la interfaz Runnable. Este es el código que se ejecutará cuando se inicie el hilo.
    @Override
    public void run() {
        // Inicia un bloque 'try' para manejar posibles excepciones que puedan ocurrir durante la ejecución del hilo.
        try {
            // Llama al método 'generateSummary' para obtener un resumen de texto de la compra antes de procesarla.
            String summary = purchaseManager.generateSummary();
            // Pausa la ejecución del hilo durante 1500 milisegundos (1.5 segundos) para simular el tiempo de procesamiento de un pago.
            Thread.sleep(1500);

            // Indica que la simulación del pago fue exitosa.
            // Llama al método para vaciar el carrito de compras.
            purchaseManager.clearCart();
            // Comprueba si se ha proporcionado un listener para notificar el resultado.
            if (listener != null) listener.onPurchaseSuccess(summary + "\n\nCompra realizada con éxito."); // Llama al método de éxito, pasando el resumen y un mensaje de confirmación.
        // Captura específicamente la excepción 'IllegalStateException', que se lanza si se intenta generar un resumen de un carrito vacío.
        } catch (IllegalStateException e) {
            // Si hay un listener, le notifica del error específico.
            if (listener != null) listener.onPurchaseFailure("Error: " + e.getMessage());
        // Captura la excepción 'InterruptedException', que ocurre si el hilo es interrumpido mientras está en 'sleep'.
        } catch (InterruptedException e) {
            // Vuelve a establecer el estado de interrupción del hilo, una buena práctica al capturar esta excepción.
            Thread.currentThread().interrupt();
            // Si hay un listener, le notifica que el pago fue interrumpido.
            if (listener != null) listener.onPurchaseFailure("Pago interrumpido.");
        // Captura cualquier otra excepción genérica que no haya sido manejada por los bloques 'catch' anteriores.
        } catch (Exception ex) {
            // Si hay un listener, le notifica de un error inesperado, incluyendo el mensaje del error.
            if (listener != null) listener.onPurchaseFailure("Error inesperado: " + ex.getMessage());
        }
    }
}

