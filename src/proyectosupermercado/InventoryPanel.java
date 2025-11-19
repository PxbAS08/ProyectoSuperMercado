/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class InventoryPanel extends JPanel {
    private final JTable inventoryTable;
    private final DefaultTableModel tableModel;
    private final JButton btnBack = new JButton("Regresar al Menú");
    private final JButton btnRefresh = new JButton("Actualizar");
    private final JLabel lblStatus = new JLabel(" ");
    
    private final MainMenuPanel.LogoutListener backListener;

    // Colores y fuentes
    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 24);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);

    public InventoryPanel(MainMenuPanel.LogoutListener backListener) {
        this.backListener = backListener;
        setLayout(new BorderLayout());

        // --- Fondo Degradado ---
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(); int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, h, SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // --- Panel Contenido ---
        JPanel contentPanel = new JPanel(new BorderLayout(30, 30));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        titlePanel.setOpaque(false);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/recursos/inventario_1.png")); // Asegúrate de que el path sea correcto
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // Escalar la imagen
            titlePanel.add(new JLabel(new ImageIcon(img)));
        } catch (Exception e) {
            System.err.println("Error cargando imagen para inventario: " + e.getMessage());
        }

        // Título
        JLabel titleLabel = new JLabel("Inventario y Mermas");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla (Ahora incluye columna de Mermas)
        String[] columnNames = {"Código", "Producto", "Categoría", "Precio", "Stock", "Dañados/Caducados"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        inventoryTable = new JTable(tableModel);
        inventoryTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        inventoryTable.setRowHeight(25);
        inventoryTable.getTableHeader().setFont(FONT_BUTTON);
        inventoryTable.setFillsViewportHeight(true);
        
        // Centrar texto
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < inventoryTable.getColumnCount(); i++) {
            inventoryTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Poner en rojo la columna de mermas para resaltar
        inventoryTable.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.RED);
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        inventoryTable.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel Inferior
        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        southPanel.setOpaque(false);
        
        lblStatus.setFont(FONT_TEXT);
        lblStatus.setForeground(Color.BLACK);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setOpaque(false);
        
        styleButton(btnRefresh);
        styleButton(btnBack);
        
        buttonsPanel.add(btnRefresh);
        buttonsPanel.add(btnBack);
        
        southPanel.add(lblStatus, BorderLayout.NORTH);
        southPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        contentPanel.add(southPanel, BorderLayout.SOUTH);

        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);

        // Acciones
        btnBack.addActionListener(e -> {
            if (backListener != null) backListener.onLogout();
        });

        btnRefresh.addActionListener(e -> loadInventoryData());

        loadInventoryData();
    }

    private void loadInventoryData() {
        tableModel.setRowCount(0);
        lblStatus.setText("Cargando inventario...");
        btnRefresh.setEnabled(false);

        new Thread(() -> {
            List<Object[]> data = InventoryManager.getInstance().getInventoryData();
            SwingUtilities.invokeLater(() -> {
                for (Object[] row : data) {
                    tableModel.addRow(row);
                }
                lblStatus.setText("Inventario actualizado.");
                btnRefresh.setEnabled(true);
            });
        }).start();
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
