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
import java.util.ArrayList; // IMPORTANTE: Agregado para corregir el error
import java.util.List;

public class PurchaseHistoryPanel extends JPanel {

    private final DefaultListModel<PurchaseRecord> listModel = new DefaultListModel<>();
    private final JList<PurchaseRecord> historyList = new JList<>(listModel);
    private final JButton btnViewDetail = new JButton("Ver Detalle / Devolución");
    private final JButton btnBack = new JButton("Regresar al Menú");
    private final MainMenuPanel.LogoutListener backListener;

    // Colores y fuentes
    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 24);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);

    public PurchaseHistoryPanel(MainMenuPanel.LogoutListener backListener) {
        this.backListener = backListener;
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

        // Panel de contenido principal
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titlePanel.setOpaque(false);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/recursos/historial.png")); // Asegúrate de que el path sea correcto
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // Escalar la imagen
            titlePanel.add(new JLabel(new ImageIcon(img)));
        } catch (Exception e) {
            System.err.println("Error cargando imagen para historial: " + e.getMessage());
        }

        JLabel titleLabel = new JLabel("Historial de Compras");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Lista de historial
        historyList.setFont(FONT_TEXT);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(historyList);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonsPanel.setOpaque(false);
        styleButton(btnViewDetail);
        styleButton(btnBack);
        
        buttonsPanel.add(btnViewDetail);
        buttonsPanel.add(btnBack);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Deshabilitar botón
        btnViewDetail.setEnabled(false);
        
        // Acción de regresar
        btnBack.addActionListener(e -> {
            if (backListener != null) {
                backListener.onLogout(); // Vuelve al menú
            }
        });
        
        // Acción de ver detalle (Conecta con ReturnPanel)
        btnViewDetail.addActionListener(e -> {
            PurchaseRecord selected = historyList.getSelectedValue();
            if (selected != null) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(PurchaseHistoryPanel.this);
                
                // Creamos el panel de devolución
                ReturnPanel returnPanel = new ReturnPanel(selected, new ReturnPanel.ReturnNavigationListener() {
                    @Override
                    public void onBackToHistory() {
                        // Volver a mostrar ESTE panel de historial y recargar datos
                        frame.setContentPane(PurchaseHistoryPanel.this);
                        loadHistoryInThread(); // Recargar por si hubo cambios (cancelaciones)
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
        
        // Habilitar el botón de detalle cuando se selecciona un ítem
        historyList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnViewDetail.setEnabled(historyList.getSelectedIndex() != -1);
            }
        });

        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);

        // Cargar los datos en un hilo separado
        loadHistoryInThread();
    }

    private void loadHistoryInThread() {
        listModel.clear();
        // CORRECCIÓN: Pasamos new ArrayList<>() en lugar de null para evitar el error
        listModel.addElement(new PurchaseRecord(new ArrayList<>(), 0, "Cargando...", "System") {
             @Override public String toString() { return "Cargando historial..."; }
        });
        
        // Hilo (Runnable) para cargar los datos
        Runnable loadTask = () -> {
            try {
                Thread.sleep(500); // Simula retraso
                
                // Obtenemos el usuario actual
                User currentUser = SessionManager.getInstance().getUser();
                // Se lo pasamos a getHistory para que filtre correctamente
                List<PurchaseRecord> history = PurchaseHistoryManager.getInstance().getHistory(currentUser);
                
                // 2. Actualiza la UI (dentro del hilo de UI)
                SwingUtilities.invokeLater(() -> {
                    listModel.clear();
                    if (history.isEmpty()) {
                         // CORRECCIÓN: Pasamos new ArrayList<>() en lugar de null
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