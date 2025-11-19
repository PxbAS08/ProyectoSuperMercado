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

public class ReturnPanel extends JPanel {
    
    public interface ReturnNavigationListener extends MainMenuPanel.LogoutListener {
        void onBackToHistory();
    }

    private final PurchaseRecord record;
    private final ReturnNavigationListener navigationListener;
    private final JList<PurchaseItem> productList;
    private final JButton btnReturn;
    private final JButton btnCancelTicket;
    private final JButton btnBack;
    private final JComboBox<String> cmbReason;

    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 20);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);

    public ReturnPanel(PurchaseRecord record, ReturnNavigationListener navigationListener) {
        this.record = record;
        this.navigationListener = navigationListener;
        setLayout(new BorderLayout());
        
        btnReturn = new JButton("Devolver Producto");
        btnCancelTicket = new JButton("Cancelar Ticket Completo");
        btnBack = new JButton("Volver");
        cmbReason = new JComboBox<>(new String[]{"Dañado", "Caducado", "Cobro Erróneo", "Otro"});
        
        styleButton(btnReturn); 
        styleButton(btnCancelTicket); 
        styleButton(btnBack);
        cmbReason.setFont(FONT_TEXT);

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

        JPanel header = new JPanel(new GridLayout(0, 1));
        header.setOpaque(false);
        
        if (record.isFullyReturned() && !record.getStatus().equals("CANCELADO")) {
             record.setStatus("DEVOLUCIÓN TOTAL");
        }

        // --- TÍTULO CON CARGA DE IMAGEN ROBUSTA ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titlePanel.setOpaque(false);
        
        ImageIcon icon = loadIcon("devolucion");
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            titlePanel.add(new JLabel(new ImageIcon(img)));
        }

        JLabel title = new JLabel("Gestionar Compra: " + record.getStatus());
        title.setFont(FONT_TITLE);
        titlePanel.add(title);
        
        header.add(titlePanel);
        // ------------------------------------------
        
        JLabel lblFolio = new JLabel("Folio: " + record.getFolio() + " | Cliente: " + record.getUser());
        lblFolio.setFont(FONT_TEXT);
        JLabel lblTotal = new JLabel(String.format("Total: $%.2f | Fecha: %s", record.getTotalAmount(), record.getPurchaseDate()));
        lblTotal.setFont(FONT_TEXT);
        
        header.add(lblFolio);
        header.add(lblTotal);
        contentPanel.add(header, BorderLayout.NORTH);

        DefaultListModel<PurchaseItem> model = new DefaultListModel<>();
        record.getItems().forEach(model::addElement);
        productList = new JList<>(model);
        productList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        productList.addListSelectionListener(e -> {
            PurchaseItem selected = productList.getSelectedValue();
            if (!e.getValueIsAdjusting() && selected != null) {
                User u = SessionManager.getInstance().getUser();
                btnReturn.setEnabled(!selected.isReturned() && !u.isAdmin() && !"CANCELADO".equals(record.getStatus()));
            } else if (!e.getValueIsAdjusting()) {
                 btnReturn.setEnabled(false);
            }
        });
        
        contentPanel.add(new JScrollPane(productList), BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(0, 1, 5, 5));
        actions.setOpaque(false);
        
        JPanel reasonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reasonPanel.setOpaque(false);
        JLabel lblReason = new JLabel("Motivo:");
        lblReason.setFont(FONT_TEXT);
        
        reasonPanel.add(lblReason);
        reasonPanel.add(cmbReason);
        actions.add(reasonPanel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        
        buttons.add(btnReturn);
        buttons.add(btnCancelTicket);
        buttons.add(btnBack);
        actions.add(buttons);
        
        contentPanel.add(actions, BorderLayout.SOUTH);
        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);

        User currentUser = SessionManager.getInstance().getUser();
        
        if (currentUser.isAdmin() || "CANCELADO".equals(record.getStatus())) {
            btnReturn.setEnabled(false);
            btnCancelTicket.setEnabled(false);
            cmbReason.setEnabled(false);
            if (currentUser.isAdmin()) {
                title.setText("Detalle de Compra (Vista Admin)");
            }
        } else {
            btnReturn.setEnabled(false);
        }

        btnReturn.addActionListener(e -> processRefund(false));
        btnCancelTicket.addActionListener(e -> processRefund(true));
        
        btnBack.addActionListener(e -> {
            if (navigationListener != null) navigationListener.onBackToHistory();
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

    private void processRefund(boolean fullTicket) {
        PurchaseItem selected = productList.getSelectedValue();
        if (!fullTicket && selected == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.");
            return;
        }

        Object[] options = {"Efectivo (En tienda)", "Dinero Electrónico"};
        int n = JOptionPane.showOptionDialog(this,
            "¿Cómo desea recibir su reembolso?",
            "Método de Devolución",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[1]);

        double amount = fullTicket ? record.getTotalAmount() : selected.getProduct().getPrice();
        boolean electronic = (n == 1);
        String motivo = (String) cmbReason.getSelectedItem();

        new Thread(() -> {
            try {
                Thread.sleep(1000); 
                
                if (electronic) {
                    User currentUser = SessionManager.getInstance().getUser();
                    currentUser.addBalance(amount);
                    new FileAuthenticationService().updateUser(currentUser);
                }

                boolean esMerma = motivo.equals("Dañado") || motivo.equals("Caducado");
                
                if (fullTicket) {
                    for (PurchaseItem item : record.getItems()) {
                        if (!item.isReturned()) { 
                            item.markAsReturned(motivo);
                            if (esMerma) InventoryManager.getInstance().addWaste(item.getProduct().getCode(), 1);
                            else InventoryManager.getInstance().restock(item.getProduct().getCode(), 1);
                        }
                    }
                    record.setStatus("CANCELADO");
                } else {
                    selected.markAsReturned(motivo);
                    if (esMerma) InventoryManager.getInstance().addWaste(selected.getProduct().getCode(), 1);
                    else InventoryManager.getInstance().restock(selected.getProduct().getCode(), 1);
                }
                
                PurchaseHistoryManager.getInstance().saveHistoryToFile();

                SwingUtilities.invokeLater(() -> {
                    String msg = fullTicket ? "Ticket Cancelado." : "Producto devuelto.";
                    //msg += esMerma ? "\nRegistrado como merma." : "\nRegresado al inventario.";
                    msg += electronic ? "\nSaldo abonado." : "\nAcude por efectivo.";
                    
                    JOptionPane.showMessageDialog(this, msg);
                    productList.repaint(); 
                    
                    if (record.isFullyReturned() && !"CANCELADO".equals(record.getStatus())) {
                        record.setStatus("DEVOLUCIÓN TOTAL");
                    }
                    
                    btnReturn.setEnabled(false);
                    if (fullTicket) btnCancelTicket.setEnabled(false);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void styleButton(JButton b) {
        b.setBackground(Color.WHITE);
        b.setFont(FONT_BUTTON);
        b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}