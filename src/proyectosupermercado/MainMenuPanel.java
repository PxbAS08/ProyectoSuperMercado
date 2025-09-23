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

public class MainMenuPanel extends JPanel {
    private final JLabel lblWelcome = new JLabel();
    private final JButton btnLogout = new JButton("Cerrar sesión");
    private final JButton btnProducts = new JButton("Seleccionar productos");

    public interface LogoutListener {
        void onLogout();
    }

    public MainMenuPanel(LogoutListener logoutListener) {
        setLayout(new BorderLayout(10, 10));
        String user = SessionManager.getInstance().getCurrentUser();
        lblWelcome.setText("Bienvenido al Supermercado, " + user + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 16));
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(btnProducts);
        buttons.add(btnLogout);

        btnLogout.addActionListener(e -> {
            SessionManager.getInstance().logout();
            if (logoutListener != null) {
                logoutListener.onLogout();
            }
        });

        btnProducts.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(MainMenuPanel.this);
            frame.setContentPane(new ProductSelectionPanel(new CartManager(), () -> {
                frame.setContentPane(new MainMenuPanel(logoutListener));
                frame.revalidate();
                frame.repaint();
            }));
            frame.revalidate();
            frame.repaint();
        });

        add(lblWelcome, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }
}


