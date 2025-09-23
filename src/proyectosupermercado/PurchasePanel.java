/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
import javax.swing.*;
import java.awt.*;

/*Panel de finalización de compra.*/
public class PurchasePanel extends JPanel {
    private final JTextArea txtSummary = new JTextArea(15, 40);
    private final JButton btnConfirm = new JButton("Confirmar compra");
    private final JButton btnCancel = new JButton("Cancelar");

    public interface BackListener {
        void onBack();
    }

    public PurchasePanel(PurchaseManager purchaseManager, BackListener backListener) {
        setLayout(new BorderLayout(10, 10));

        txtSummary.setEditable(false);
        try {
            txtSummary.setText(purchaseManager.generateSummary());
        } catch (Exception e) {
            txtSummary.setText("No hay productos en el carrito.\n" + e.getMessage());
        }

        JPanel buttons = new JPanel();
        buttons.add(btnConfirm);
        buttons.add(btnCancel);

        add(new JScrollPane(txtSummary), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        btnConfirm.addActionListener(e -> {
            new Thread(new PurchaseThread(purchaseManager, new PurchaseThread.PurchaseListener() {
                @Override
                public void onPurchaseSuccess(String message) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(PurchasePanel.this, message, "Compra exitosa", JOptionPane.INFORMATION_MESSAGE);
                        
                        if (backListener != null) backListener.onBack();
                    });
                }

                @Override
                public void onPurchaseFailure(String error) {
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(PurchasePanel.this, error, "Error en la compra", JOptionPane.ERROR_MESSAGE)
                    );
                }
            }), "PurchaseThread").start();
        });

        btnCancel.addActionListener(e -> {
            if (backListener != null) backListener.onBack();
        });
    }
}

