package proyectosupermercado;

import java.io.Serializable;

public class PurchaseItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Product product;
    private boolean returned;
    private String returnReason;

    public PurchaseItem(Product product) {
        this.product = product;
        this.returned = false;
        this.returnReason = "";
    }

    public Product getProduct() {
        return product;
    }

    public boolean isReturned() {
        return returned;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void markAsReturned(String reason) {
        this.returned = true;
        this.returnReason = reason;
    }

    @Override
    public String toString() {
        String base = product.getName() + " (" + product.getSize() + ") - $" + product.getPrice();
        if (returned) {
            return "<html><font color='red'><strike>" + base + "</strike> (Devuelto: " + returnReason + ")</font></html>";
        }
        return base;
    }
}
