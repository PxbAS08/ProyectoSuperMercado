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
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

public class PurchaseHistoryPanel extends JPanel {

    private final DefaultListModel<PurchaseRecord> listModel = new DefaultListModel<>();
    private final JList<PurchaseRecord> historyList = new JList<>(listModel);
    private final JButton btnViewDetail = new JButton("Ver Detalle / Devolución");
    private final JButton btnBack = new JButton("Regresar al Menú");
    private final MainMenuPanel.LogoutListener backListener;

    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 24);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);

    public PurchaseHistoryPanel(MainMenuPanel.LogoutListener backListener) {
        this.backListener = backListener;
        setLayout(new BorderLayout());

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

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- TÍTULO CON CARGA DE IMAGEN ROBUSTA ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titlePanel.setOpaque(false);
        
        // Intentar cargar imagen con o sin _1
        ImageIcon icon = loadIcon("historial");
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            titlePanel.add(new JLabel(new ImageIcon(img)));
        }
        
        JLabel titleLabel = new JLabel("Historial de Compras");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);
        contentPanel.add(titlePanel, BorderLayout.NORTH);
        // ------------------------------------------

        historyList.setFont(FONT_TEXT);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(historyList);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setOpaque(false);
        styleButton(btnViewDetail);
        styleButton(btnBack);
        
        buttonsPanel.add(btnViewDetail);
        buttonsPanel.add(btnBack);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

        btnViewDetail.setEnabled(false);
        
        btnBack.addActionListener(e -> {
            if (backListener != null) {
                backListener.onLogout(); 
            }
        });
        
        btnViewDetail.addActionListener(e -> {
            PurchaseRecord selected = historyList.getSelectedValue();
            if (selected != null) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PurchaseHistoryPanel.this);
                
                ReturnPanel returnPanel = new ReturnPanel(selected, new ReturnPanel.ReturnNavigationListener() {
                    @Override
                    public void onBackToHistory() {
                        frame.setContentPane(PurchaseHistoryPanel.this);
                        loadHistoryInThread(); 
                        frame.revalidate();
                        frame.repaint();
                    }

                    @Override
                    public void onLogout() {
                        if (backListener != null) backListener.onLogout();
                    }
                });
                
                frame.setContentPane(returnPanel);
                frame.revalidate();
                frame.repaint();
            }
        });
        
        historyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnViewDetail.setEnabled(historyList.getSelectedIndex() != -1);
            }
        });

        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);

        loadHistoryInThread();
    }
    
    // Método auxiliar para cargar imágenes probando nombres
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

    private void loadHistoryInThread() {
        listModel.clear();
        listModel.addElement(new PurchaseRecord(new ArrayList<>(), 0, "Cargando...", "System") {
             @Override public String toString() { return "Cargando historial..."; }
        });
        
        Runnable loadTask = () -> {
            try {
                Thread.sleep(500); 
                User currentUser = SessionManager.getInstance().getUser();
                List<PurchaseRecord> history = PurchaseHistoryManager.getInstance().getHistory(currentUser);
                
                SwingUtilities.invokeLater(() -> {
                    listModel.clear();
                    if (history.isEmpty()) {
                         listModel.addElement(new PurchaseRecord(new ArrayList<>(), 0, "", "System") {
                             @Override public String toString() { return "No hay compras registradas."; }
                        });
                    } else {
                        for (PurchaseRecord record : history) {
                            listModel.addElement(record);
                        }
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                SwingUtilities.invokeLater(() -> listModel.clear());
            }
        };
        
        new Thread(loadTask).start();
    }

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