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

public class MainMenuPanel extends JPanel {
    private final JLabel lblWelcome = new JLabel();
    private final JLabel lblWallet = new JLabel();
    private final JTextField txtAddress = new JTextField(25); 
    private final JButton btnLogout = new JButton("Cerrar sesión");
    private final JButton btnProducts = new JButton("Seleccionar productos");
    private final JButton btnPickup = new JButton("Recoger en tienda");
    private final JButton btnHistory = new JButton("Historial de Compras"); 
    private final JButton btnInventory = new JButton("Inventario"); 

    public interface LogoutListener {
        void onLogout();
    }

    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 24);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 16);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);

    public MainMenuPanel(LogoutListener logoutListener) {
        setLayout(new BorderLayout());

        // --- Fondo ---
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
        contentPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // --- Panel Superior ---
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Logo
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/recursos/select.png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            JLabel logoLabel = new JLabel(resizedIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(logoLabel);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        User user = SessionManager.getInstance().getUser();
        lblWelcome.setText("Bienvenido, " + user.getUsername() + "!");
        lblWelcome.setFont(FONT_TITLE);
        lblWelcome.setForeground(Color.BLACK);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblWelcome);

        // Monedero Electrónico (Solo si no es Admin)
        if (!user.isAdmin()) {
            lblWallet.setText(String.format("Dinero Electrónico: $%.2f", user.getWalletBalance()));
            lblWallet.setFont(new Font("Cascadia Code", Font.BOLD, 18));
            lblWallet.setForeground(Color.decode("#27ae60"));
            lblWallet.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            topPanel.add(lblWallet);

            topPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            JLabel lblAddr = new JLabel("Ingresa tu dirección:");
            lblAddr.setFont(FONT_TEXT);
            lblAddr.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(lblAddr);
            
            txtAddress.setFont(FONT_TEXT);
            txtAddress.setMaximumSize(new Dimension(400, 30));
            txtAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(txtAddress);
        } else {
            JLabel lblAdmin = new JLabel("Panel de Administración");
            lblAdmin.setFont(FONT_TEXT);
            lblAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);
            topPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            topPanel.add(lblAdmin);
        }

        contentPanel.add(topPanel, BorderLayout.NORTH);

        // --- Botones ---
        JPanel centerButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerButtons.setOpaque(false);
        JPanel stackPanel = new JPanel();
        stackPanel.setLayout(new BoxLayout(stackPanel, BoxLayout.Y_AXIS));
        stackPanel.setOpaque(false);
        
        styleButton(btnProducts);
        styleButton(btnPickup);
        styleButton(btnHistory);
        styleButton(btnInventory);

        // Alineación
        btnProducts.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPickup.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHistory.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnInventory.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Lógica de Roles para Botones
        if (user.isAdmin()) {
            // Admin ve Historial e Inventario
            stackPanel.add(btnHistory);
            stackPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            stackPanel.add(btnInventory);
        } else {
            // Cliente ve Compras, Recoger e Historial
            stackPanel.add(btnProducts);
            stackPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            stackPanel.add(btnPickup);
            stackPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            stackPanel.add(btnHistory);
        }

        centerButtons.add(stackPanel);
        contentPanel.add(centerButtons, BorderLayout.CENTER);

        // --- Botón Logout ---
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setOpaque(false);
        styleButton(btnLogout);
        south.add(btnLogout);
        contentPanel.add(south, BorderLayout.SOUTH);

        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);

        // --- Acciones ---
        btnLogout.addActionListener(e -> {
            SessionManager.getInstance().logout();
            logoutListener.onLogout();
        });

        btnProducts.addActionListener(e -> startShopping(logoutListener));
        btnPickup.addActionListener(e -> {
            SessionManager.getInstance().setUserAddress("Recoger en tienda");
            navigateToSelection(logoutListener);
        });

        btnHistory.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new PurchaseHistoryPanel(() -> {
                frame.setContentPane(new MainMenuPanel(logoutListener));
                frame.revalidate();
            }));
            frame.revalidate();
        });

        btnInventory.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new InventoryPanel(() -> {
                frame.setContentPane(new MainMenuPanel(logoutListener));
                frame.revalidate();
            }));
            frame.revalidate();
        });
    }

    private void startShopping(LogoutListener l) {
        String address = txtAddress.getText().trim();
        if (address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa dirección o elige Recoger.");
            return;
        }
        SessionManager.getInstance().setUserAddress(address);
        navigateToSelection(l);
    }

    private void navigateToSelection(LogoutListener l) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new ProductSelectionPanel(new CartManager(), () -> {
            frame.setContentPane(new MainMenuPanel(l));
            frame.revalidate();
        }));
        frame.revalidate();
    }

    private void styleButton(JButton b) {
        b.setFont(FONT_BUTTON); b.setBackground(Color.WHITE); b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(250, 40));
        b.setPreferredSize(new Dimension(250, 40));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(BUTTON_HOVER_COLOR); }
            public void mouseExited(MouseEvent e) { b.setBackground(Color.WHITE); }
        });
    }
}