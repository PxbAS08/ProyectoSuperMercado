package proyectosupermercado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PurchaseRecord implements Serializable {
    
    private static final long serialVersionUID = 4L; // Versión actualizada
    
    private final String folio;
    private final Date purchaseDate;
    private final List<PurchaseItem> items; // Cambiado de Product a PurchaseItem
    private final double totalAmount;
    private final String shippingAddress;
    private final String user;
    private String status; 

    public PurchaseRecord(List<Product> products, double totalAmount, String shippingAddress, String user) {
        this.folio = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.purchaseDate = new Date();
        // Convertimos Productos a Items de Compra
        this.items = products.stream().map(PurchaseItem::new).collect(Collectors.toList());
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.user = user;
        this.status = "COMPLETADO";
    }

    public String getFolio() { return folio; }
    public Date getPurchaseDate() { return purchaseDate; }
    public List<PurchaseItem> getItems() { return items; } // Getter actualizado
    // Método helper para compatibilidad si se necesita la lista de productos puros
    public List<Product> getProducts() { 
        return items.stream().map(PurchaseItem::getProduct).collect(Collectors.toList()); 
    }
    public double getTotalAmount() { return totalAmount; }
    public String getShippingAddress() { return shippingAddress; }
    public String getUser() { return user; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public boolean isFullyReturned() {
        return items.stream().allMatch(PurchaseItem::isReturned);
    }

    @Override
    public String toString() {
        return String.format("[%s] Folio: %s - Total: $%.2f - %s", 
                status, folio, totalAmount, purchaseDate.toString());
    }
}