/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PurchasePanel extends JPanel {
    private final JTextArea txtSummary = new JTextArea(15, 40);
    private final JButton btnConfirm = new JButton("Confirmar compra");
    private final JButton btnCancel = new JButton("Cancelar");
    private final JComboBox<String> cmbPaymentMethod = new JComboBox<>(new String[]{"Efectivo", "Tarjeta de Crédito/Débito"});
    private final JCheckBox chkUseWallet; 
    private final JTextField txtWalletAmount; // Nuevo campo

    private final PurchaseManager purchaseManager;
    private final BackListener backListener;

    public interface BackListener {
        void onBack();
    }

    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 18);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);
    private static final Font FONT_MONOSPACED = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14);

    public PurchasePanel(PurchaseManager purchaseManager, BackListener backListener) {
        this.purchaseManager = purchaseManager;
        this.backListener = backListener;
        setLayout(new BorderLayout());

        JPanel gradientPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, getHeight(), SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JLabel headerLabel = new JLabel("SuperMercado ONIX - Resumen de la Compra");
        headerLabel.setFont(FONT_TITLE);
        headerLabel.setForeground(Color.BLACK);
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        txtSummary.setEditable(false);
        txtSummary.setFont(FONT_MONOSPACED);
        txtSummary.setForeground(Color.BLACK);
        txtSummary.setBackground(Color.WHITE);
        txtSummary.setOpaque(true);
        txtSummary.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        JScrollPane scrollSummary = new JScrollPane(txtSummary);
        
        // --- Panel de Pago ---
        JPanel paymentOptionsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        paymentOptionsPanel.setOpaque(false);
        
        // Panel Monedero
        JPanel walletPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        walletPanel.setOpaque(false);
        
        User u = SessionManager.getInstance().getUser();
        chkUseWallet = new JCheckBox("Usar saldo ($" + String.format("%.2f", u.getWalletBalance()) + "):");
        chkUseWallet.setFont(FONT_TEXT);
        chkUseWallet.setForeground(Color.BLUE);
        chkUseWallet.setOpaque(false);
        
        txtWalletAmount = new JTextField(6);
        txtWalletAmount.setFont(FONT_TEXT);
        txtWalletAmount.setText("0.00");
        txtWalletAmount.setEnabled(false);
        
        JButton btnApplyWallet = new JButton("Aplicar");
        styleButton(btnApplyWallet);
        btnApplyWallet.setPreferredSize(new Dimension(80, 25));
        btnApplyWallet.setEnabled(false);

        walletPanel.add(chkUseWallet);
        walletPanel.add(txtWalletAmount);
        walletPanel.add(btnApplyWallet);
        
        chkUseWallet.addItemListener(e -> {
            boolean selected = chkUseWallet.isSelected();
            txtWalletAmount.setEnabled(selected);
            btnApplyWallet.setEnabled(selected);
            if (selected) {
                // Por defecto poner el máximo posible
                double max = Math.min(u.getWalletBalance(), purchaseManager.getCartManager().getTotal());
                txtWalletAmount.setText(String.format("%.2f", max).replace(",", "."));
            } else {
                purchaseManager.setUseWallet(false, 0);
                updateSummaryText();
                checkFullPayment();
            }
        });
        
        btnApplyWallet.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(txtWalletAmount.getText());
                if (amount > u.getWalletBalance()) {
                    JOptionPane.showMessageDialog(this, "Fondos insuficientes.");
                    amount = u.getWalletBalance();
                    txtWalletAmount.setText(String.format("%.2f", amount));
                }
                purchaseManager.setUseWallet(true, amount);
                updateSummaryText();
                checkFullPayment();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Monto inválido.");
            }
        });

        // Panel Método Restante
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        methodPanel.setOpaque(false);
        JLabel lblMethod = new JLabel("Pago restante: ");
        lblMethod.setFont(FONT_TEXT);
        lblMethod.setForeground(Color.RED);
        cmbPaymentMethod.setFont(FONT_TEXT);
        
        methodPanel.add(lblMethod);
        methodPanel.add(cmbPaymentMethod);
        
        paymentOptionsPanel.add(walletPanel);
        paymentOptionsPanel.add(methodPanel);

        // Botones
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.setOpaque(false);
        styleButton(btnConfirm);
        styleButton(btnCancel);
        buttons.add(btnConfirm);
        buttons.add(btnCancel);

        JPanel imagePanel = new JPanel();
        imagePanel.setOpaque(false);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/recursos/logo.png"));
            JLabel img = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
            imagePanel.add(img);
        } catch (Exception e) {}

        JPanel southContainer = new JPanel();
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
        southContainer.setOpaque(false);
        southContainer.add(paymentOptionsPanel);
        southContainer.add(buttons);
        southContainer.add(imagePanel);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        centerPanel.add(scrollSummary, gbc);

        contentPanel.add(headerLabel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);
        contentPanel.add(southContainer, BorderLayout.SOUTH);

        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);

        updateSummaryText();

        btnConfirm.addActionListener(e -> {
            if (purchaseManager.getRemainingToPay() <= 0.01) { // Margen de error decimal
                startCashPayment();
                return;
            }

            String method = (String) cmbPaymentMethod.getSelectedItem();
            if ("Tarjeta de Crédito/Débito".equals(method)) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.setContentPane(new CardPaymentPanel(purchaseManager, backListener));
                frame.revalidate();
                frame.repaint();
            } else {
                startCashPayment();
            }
        });

        btnCancel.addActionListener(e -> {
            if (backListener != null) backListener.onBack();
        });
    }
    
    private void checkFullPayment() {
        boolean coversAll = purchaseManager.getRemainingToPay() <= 0.01;
        cmbPaymentMethod.setEnabled(!coversAll);
        if (coversAll) {
            btnConfirm.setText("Pagar con Monedero");
        } else {
            btnConfirm.setText("Confirmar Compra");
        }
    }

    private void updateSummaryText() {
        try {
            txtSummary.setText(purchaseManager.generateSummary());
            txtSummary.setCaretPosition(0);
        } catch (Exception e) {
            txtSummary.setText("Error: " + e.getMessage());
        }
    }

    private void startCashPayment() {
        new Thread(new PurchaseThread(purchaseManager, new PurchaseThread.PurchaseListener() {
            @Override
            public void onPurchaseSuccess(String message) {
                try {
                    purchaseManager.processWalletDeduction();
                } catch (Exception e) { e.printStackTrace(); }

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(PurchasePanel.this, message, "Compra exitosa", JOptionPane.INFORMATION_MESSAGE);
                    purchaseManager.clearCart();
                    if (backListener != null) backListener.onBack();
                });
            }

            @Override
            public void onPurchaseFailure(String error) {
                SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(PurchasePanel.this, error, "Error", JOptionPane.ERROR_MESSAGE)
                );
            }
        }), "PurchaseThread").start();
    }

    private void styleButton(JButton b) {
        b.setFont(FONT_BUTTON); b.setBackground(Color.WHITE); b.setForeground(Color.BLACK);
        b.setFocusPainted(false); b.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(BUTTON_HOVER_COLOR); }
            public void mouseExited(MouseEvent e) { b.setBackground(Color.WHITE); }
        });
    }
}