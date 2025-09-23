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

/**
 * Muestra el ticket en pantalla como texto.
 */
public class TicketPanel extends JPanel {
    private final JTextArea txtTicket = new JTextArea(20, 40);

    public interface BackListener {
        void onBack();
    }

    public TicketPanel(String ticketText, BackListener backListener) {
        setLayout(new BorderLayout(10, 10));
        txtTicket.setEditable(false);
        txtTicket.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtTicket.setText(ticketText);

        JButton btnBack = new JButton("Volver al menú");
        btnBack.addActionListener(e -> {
            if (backListener != null) backListener.onBack();
        });

        add(new JScrollPane(txtTicket), BorderLayout.CENTER);
        add(btnBack, BorderLayout.SOUTH);
    }
}

