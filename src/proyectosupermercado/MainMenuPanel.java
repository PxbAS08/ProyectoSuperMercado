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
    private final JTextField txtAddress = new JTextField(25); // Nuevo campo para la dirección
    private final JButton btnLogout = new JButton("Cerrar sesión");
    private final JButton btnProducts = new JButton("Seleccionar productos");
    private final JButton btnPickup = new JButton("Recoger en tienda");

    public interface LogoutListener {
        void onLogout();
    }

    // Colores y fuentes
    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 24);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 16);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);

    public MainMenuPanel(LogoutListener logoutListener) {
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
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Panel superior para la imagen, bienvenida y la dirección
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Agregar la imagen
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

        String user = SessionManager.getInstance().getCurrentUser();
        lblWelcome.setText("Bienvenido, " + user + "!");
        lblWelcome.setFont(FONT_TITLE);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblWelcome);
        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel lblAddress = new JLabel("Ingresa tu dirección:");
        lblAddress.setFont(FONT_TEXT);
        lblAddress.setForeground(Color.BLACK);
        lblAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(lblAddress);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        txtAddress.setFont(FONT_TEXT);
        txtAddress.setMaximumSize(new Dimension(400, 30));
        txtAddress.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(txtAddress);

        contentPanel.add(topPanel, BorderLayout.NORTH);

        // Panel de botones
        JPanel centerButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        centerButtons.setOpaque(false);
        
        styleButton(btnProducts);
        styleButton(btnPickup);

        centerButtons.add(btnProducts);
        centerButtons.add(btnPickup);
        
        contentPanel.add(centerButtons, BorderLayout.CENTER);

        // Panel inferior para el botón de cerrar sesión
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        logoutPanel.setOpaque(false);
        styleButton(btnLogout);
        logoutPanel.add(btnLogout);
        
        contentPanel.add(logoutPanel, BorderLayout.SOUTH);

        // Agrega el panel de contenido al panel de degradado
        gradientPanel.add(contentPanel);

        // Agrega el panel de degradado al panel principal
        add(gradientPanel, BorderLayout.CENTER);

        // Acciones
        btnLogout.addActionListener(e -> {
            SessionManager.getInstance().logout();
            if (logoutListener != null) {
                logoutListener.onLogout();
            }
        });

        btnProducts.addActionListener(e -> {
            String address = txtAddress.getText().trim();
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa una dirección para continuar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            SessionManager.getInstance().setUserAddress(address);
            
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(MainMenuPanel.this);
            frame.setContentPane(new ProductSelectionPanel(new CartManager(), () -> {
                frame.setContentPane(new MainMenuPanel(logoutListener));
                frame.revalidate();
                frame.repaint();
            }));
            frame.revalidate();
            frame.repaint();
        });

        btnPickup.addActionListener(e -> {
            SessionManager.getInstance().setUserAddress("Recoger en tienda");

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(MainMenuPanel.this);
            frame.setContentPane(new ProductSelectionPanel(new CartManager(), () -> {
                frame.setContentPane(new MainMenuPanel(logoutListener));
                frame.revalidate();
                frame.repaint();
            }));
            frame.revalidate();
            frame.repaint();
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

