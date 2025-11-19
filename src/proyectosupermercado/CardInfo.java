package proyectosupermercado;

import java.io.Serializable;

public class CardInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String cardNumber;
    private final String holderName;
    private final String expiry;
    // El CVV no se suele guardar por seguridad en sistemas reales, 
    // pero para este proyecto académico lo guardaremos o dejaremos que el usuario lo ponga.
    // Aquí guardaremos solo los datos básicos para auto-llenar.
    
    public CardInfo(String cardNumber, String holderName, String expiry) {
        this.cardNumber = cardNumber;
        this.holderName = holderName;
        this.expiry = expiry;
    }

    public String getCardNumber() { return cardNumber; }
    public String getHolderName() { return holderName; }
    public String getExpiry() { return expiry; }
}