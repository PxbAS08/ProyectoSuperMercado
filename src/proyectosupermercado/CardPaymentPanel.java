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
import java.net.URL;

public class CardPaymentPanel extends JPanel {
    private final JTextField txtCardNumber = new JTextField(16);
    private final JTextField txtHolderName = new JTextField(20);
    private final JTextField txtExpiry = new JTextField(5); 
    private final JPasswordField txtCvv = new JPasswordField(3);
    private final JCheckBox chkSaveCard = new JCheckBox("Guardar tarjeta para futuras compras");
    
    private final JButton btnPay = new JButton("Pagar");
    private final JButton btnBack = new JButton("Cancelar");
    private final JLabel lblStatus = new JLabel(" ");
    private final JProgressBar progressBar = new JProgressBar(0, 100);

    private final PurchaseManager purchaseManager;
    private final PurchasePanel.BackListener backListener;

    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Color ERROR_COLOR = Color.decode("#e74c3c");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 24);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);

    public CardPaymentPanel(PurchaseManager purchaseManager, PurchasePanel.BackListener backListener) {
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

        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // --- TÍTULO CON CARGA DE IMAGEN ROBUSTA ---
        double toPay = purchaseManager.getRemainingToPay();
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titlePanel.setOpaque(false);
        
        ImageIcon icon = loadIcon("pagotarjeta");
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            titlePanel.add(new JLabel(new ImageIcon(img)));
        }
        
        JLabel titleLabel = new JLabel("Pago con Tarjeta: $" + String.format("%.2f", toPay));
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        // ------------------------------------------

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;

        txtCardNumber.setFont(FONT_TEXT);
        txtHolderName.setFont(FONT_TEXT);
        txtExpiry.setFont(FONT_TEXT);
        txtCvv.setFont(FONT_TEXT);
        
        chkSaveCard.setFont(new Font("Cascadia Code", Font.PLAIN, 12));
        chkSaveCard.setOpaque(false);
        chkSaveCard.setForeground(Color.BLACK); 

        addFormField(form, c, 0, "Número de Tarjeta:", txtCardNumber);
        addFormField(form, c, 1, "Nombre del Titular:", txtHolderName);
        addFormField(form, c, 2, "Vencimiento (MM/YY):", txtExpiry);
        addFormField(form, c, 3, "CVV:", txtCvv);
        
        c.gridx = 1; c.gridy = 4;
        form.add(chkSaveCard, c);
        
        contentPanel.add(form, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setOpaque(false);
        
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        botones.setOpaque(false);
        styleButton(btnPay);
        styleButton(btnBack);
        botones.add(btnPay);
        botones.add(btnBack);
        
        lblStatus.setFont(FONT_TEXT);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setForeground(Color.BLACK);

        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressBar.setBorderPainted(false);
        progressBar.setForeground(Color.WHITE);
        progressBar.setBackground(new Color(255, 255, 255, 128));

        south.add(botones, BorderLayout.NORTH);
        south.add(progressBar, BorderLayout.CENTER);
        south.add(lblStatus, BorderLayout.SOUTH);

        contentPanel.add(south, BorderLayout.SOUTH);
        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);

        loadSavedCard();

        btnPay.addActionListener(e -> processPayment());
        btnBack.addActionListener(e -> {
            if (backListener != null) backListener.onBack();
        });
    }
    
    private ImageIcon loadIcon(String baseName) {
        String[] options = {"/recursos/" + baseName + ".png", "/recursos/" + baseName + "_1.png"};
        for (String path : options) {
            URL url = getClass().getResource(path);
            if (url != null) {
                return new ImageIcon(url);
            }
        }
        System.err.println("No se encontró la imagen: " + baseName);
        return null;
    }
    
    private void loadSavedCard() {
        User currentUser = SessionManager.getInstance().getUser();
        if (currentUser != null && currentUser.getSavedCard() != null) {
            CardInfo card = currentUser.getSavedCard();
            txtCardNumber.setText(card.getCardNumber());
            txtHolderName.setText(card.getHolderName());
            txtExpiry.setText(card.getExpiry());
            chkSaveCard.setSelected(true);
            lblStatus.setText("Tarjeta guardada cargada.");
        }
    }

    private void addFormField(JPanel panel, GridBagConstraints c, int y, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_TEXT);
        label.setForeground(Color.BLACK);
        c.gridx = 0; c.gridy = y;
        panel.add(label, c);
        c.gridx = 1; c.gridy = y;
        panel.add(field, c);
    }

    private void processPayment() {
        String num = txtCardNumber.getText().trim();
        String name = txtHolderName.getText().trim();
        String exp = txtExpiry.getText().trim();

        if (num.length() < 16) {
            lblStatus.setText("Número de tarjeta inválido.");
            lblStatus.setForeground(ERROR_COLOR); return;
        }
        if (name.isEmpty()) {
            lblStatus.setText("Ingrese el nombre del titular.");
            lblStatus.setForeground(ERROR_COLOR); return;
        }
        if (txtCvv.getPassword().length < 3) {
            lblStatus.setText("CVV inválido.");
            lblStatus.setForeground(ERROR_COLOR); return;
        }

        setFieldsEnabled(false);
        lblStatus.setText("Conectando con el banco...");
        lblStatus.setForeground(Color.BLACK);
        progressBar.setIndeterminate(false); 
        progressBar.setValue(10);

        new Thread(() -> {
            try {
                Thread.sleep(1000); 
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Verificando fondos...");
                    progressBar.setValue(40);
                });
                
                Thread.sleep(1500); 
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Autorizando transacción...");
                    progressBar.setValue(75);
                });
                
                if (chkSaveCard.isSelected()) {
                    User user = SessionManager.getInstance().getUser();
                    CardInfo newCard = new CardInfo(num, name, exp);
                    user.setSavedCard(newCard);
                    new FileAuthenticationService().updateUser(user);
                }
                
                Thread.sleep(1000); 

                String user = SessionManager.getInstance().getCurrentUser();
                String address = SessionManager.getInstance().getUserAddress();
                
                PurchaseRecord record = new PurchaseRecord(
                        purchaseManager.getCartManager().getCart(),
                        purchaseManager.getCartManager().getTotal(),
                        address,
                        user
                );
                PurchaseHistoryManager.getInstance().addPurchase(record);
                purchaseManager.updateInventory();
                purchaseManager.processWalletDeduction();

                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(100);
                    lblStatus.setText("¡Pago Aprobado!");
                    
                    JOptionPane.showMessageDialog(this, 
                            "Transacción Exitosa.\nFolio: " + record.getFolio() + "\nGracias por su compra.", 
                            "Pago con Tarjeta", JOptionPane.INFORMATION_MESSAGE);
                    
                    purchaseManager.clearCart();
                    if (backListener != null) backListener.onBack(); 
                });

            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    progressBar.setValue(0);
                    lblStatus.setText("Error en el pago.");
                    lblStatus.setForeground(ERROR_COLOR);
                    setFieldsEnabled(true);
                });
            }
        }).start();
    }

    private void setFieldsEnabled(boolean enabled) {
        txtCardNumber.setEnabled(enabled);
        txtHolderName.setEnabled(enabled);
        txtExpiry.setEnabled(enabled);
        txtCvv.setEnabled(enabled);
        btnPay.setEnabled(enabled);
        btnBack.setEnabled(enabled);
        chkSaveCard.setEnabled(enabled);
    }

    private void styleButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { button.setBackground(BUTTON_HOVER_COLOR); }
            public void mouseExited(MouseEvent evt) { button.setBackground(Color.WHITE); }
        });
    }
}