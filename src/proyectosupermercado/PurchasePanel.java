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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*Panel de finalización de compra.*/
public class PurchasePanel extends JPanel {
    private final JTextArea txtSummary = new JTextArea(15, 40);
    private final JButton btnConfirm = new JButton("Confirmar compra");
    private final JButton btnCancel = new JButton("Cancelar");

    public interface BackListener {
        void onBack();
    }

    // Colores y fuentes para el diseño
    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#DCC184");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 18);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);
    private static final Font FONT_MONOSPACED = new Font("Monospaced", Font.PLAIN, 12);

    public PurchasePanel(PurchaseManager purchaseManager, BackListener backListener) {
        setLayout(new BorderLayout());

        // Panel con degradado de fondo
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = PRIMARY_COLOR;
                Color color2 = SECONDARY_COLOR;
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Panel de contenido principal, transparente
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Título de la sección
        JLabel headerLabel = new JLabel("SuperMercado ONIX - Resumen de la Compra");
        headerLabel.setFont(FONT_TITLE);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Área de texto para el resumen
        txtSummary.setEditable(false);
        txtSummary.setFont(FONT_MONOSPACED);
        txtSummary.setForeground(Color.BLACK);
        txtSummary.setBackground(Color.WHITE);
        txtSummary.setOpaque(true);
        JScrollPane scrollSummary = new JScrollPane(txtSummary);
        scrollSummary.setOpaque(false);
        scrollSummary.getViewport().setOpaque(true);
        
        try {
            txtSummary.setText(purchaseManager.generateSummary());
            // Centrar el texto en el JTextArea es complicado, se puede simular con un diseño
            txtSummary.setCaretPosition(0); // Vuelve al inicio del texto
        } catch (Exception e) {
            txtSummary.setText("No hay productos en el carrito.\n" + e.getMessage());
        }

        // Panel para los botones
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        styleButton(btnConfirm);
        styleButton(btnCancel);
        buttons.add(btnConfirm);
        buttons.add(btnCancel);

        // Panel para la imagen debajo de los botones
        JPanel imagePanel = new JPanel();
        imagePanel.setOpaque(false);
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/recursos/gracias.png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            JLabel imageLabel = new JLabel(resizedIcon);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imagePanel.add(imageLabel);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }

        // Panel inferior que contiene los botones y la imagen
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(buttons, BorderLayout.NORTH);
        southPanel.add(imagePanel, BorderLayout.SOUTH);

        // Panel para el JScrollPane con el texto del ticket y centrando el texto
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        centerPanel.add(scrollSummary, gbc);


        // Añadiendo los componentes al panel de contenido
        contentPanel.add(headerLabel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);

        // Agrega el panel de contenido al panel de degradado
        gradientPanel.add(contentPanel);

        // Agrega el panel de degradado al panel principal
        add(gradientPanel, BorderLayout.CENTER);

        // Agrega las acciones de los botones
        btnConfirm.addActionListener(e -> {
            new Thread(new PurchaseThread(purchaseManager, new PurchaseThread.PurchaseListener() {
                @Override
                public void onPurchaseSuccess(String message) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(PurchasePanel.this, message, "Compra exitosa", JOptionPane.INFORMATION_MESSAGE);
                        purchaseManager.clearCart();
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

    // Método para estilizar los botones
    private void styleButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
    }
}

