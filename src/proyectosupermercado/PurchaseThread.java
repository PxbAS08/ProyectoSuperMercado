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
public class PurchaseThread implements Runnable {
    private final PurchaseManager purchaseManager;
    private final PurchaseListener listener;

    public interface PurchaseListener {
        void onPurchaseSuccess(String message);
        void onPurchaseFailure(String error);
    }

    public PurchaseThread(PurchaseManager purchaseManager, PurchaseListener listener) {
        this.purchaseManager = purchaseManager;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            String summary = purchaseManager.generateSummary();
            // Simular procesamiento de pago
            Thread.sleep(1500);

            // Confirmación exitosa
            purchaseManager.clearCart();
            if (listener != null) listener.onPurchaseSuccess(summary + "\n\nCompra realizada con éxito.");
        } catch (IllegalStateException e) {
            if (listener != null) listener.onPurchaseFailure("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (listener != null) listener.onPurchaseFailure("Pago interrumpido.");
        } catch (Exception ex) {
            if (listener != null) listener.onPurchaseFailure("Error inesperado: " + ex.getMessage());
        }
    }
}

