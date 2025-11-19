/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PurchaseManager {
    private final CartManager cartManager;
    private boolean useWallet = false; 
    private double walletAmountToUse = 0.0; // Nueva variable para la cantidad específica

    public PurchaseManager(CartManager cartManager) {
        this.cartManager = cartManager;
    }

    public CartManager getCartManager() {
        return cartManager;
    }
    
    public void setUseWallet(boolean use, double amount) {
        this.useWallet = use;
        this.walletAmountToUse = amount;
    }
    
    public boolean isUsingWallet() {
        return useWallet;
    }

    public double getWalletToUse() {
        if (!useWallet) return 0.0;
        // Validar que no use más de lo que tiene ni más del total
        User u = SessionManager.getInstance().getUser();
        if (u == null) return 0.0;
        
        double maxPossible = Math.min(u.getWalletBalance(), cartManager.getTotal());
        return Math.min(walletAmountToUse, maxPossible);
    }

    public double getRemainingToPay() {
        return Math.max(0, cartManager.getTotal() - getWalletToUse());
    }

    public void processWalletDeduction() throws IOException {
        double toDeduct = getWalletToUse();
        if (toDeduct > 0) {
            User u = SessionManager.getInstance().getUser();
            u.addBalance(-toDeduct); 
            new FileAuthenticationService().updateUser(u); 
        }
    }

    public void updateInventory() {
        List<Product> cart = cartManager.getCart();
        Map<String, Integer> productCounts = new HashMap<>();
        for (Product p : cart) {
            productCounts.put(p.getCode(), productCounts.getOrDefault(p.getCode(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : productCounts.entrySet()) {
            InventoryManager.getInstance().reduceStock(entry.getKey(), entry.getValue());
        }
    }

    public String generateSummary() {
        List<Product> items = cartManager.getCart();
        if (items.isEmpty()) throw new IllegalStateException("El carrito está vacío.");

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
        double walletUsed = getWalletToUse();
        double finalPay = getRemainingToPay();

        sb.append("------------------------------------------\n");
        sb.append(String.format("\nSubtotal:      $%.2f", subtotal));
        sb.append(String.format("\nDescuento:    -$%.2f", cartManager.getDiscount()));
        sb.append(String.format("\nTotal Global:  $%.2f", cartManager.getTotal()));
        
        if (walletUsed > 0) {
            sb.append(String.format("\nMonedero:     -$%.2f", walletUsed));
            sb.append(String.format("\nRestante:      $%.2f", finalPay));
        } else {
            sb.append(String.format("\nTotal a Pagar: $%.2f", finalPay));
        }

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

    public void clearCart() {
        cartManager.clearCart();
        useWallet = false; 
        walletAmountToUse = 0.0;
    }
}

