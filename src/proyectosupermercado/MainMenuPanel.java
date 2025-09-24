/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-o-default.txt to change this license
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
import javax.swing.border.LineBorder;

public class MainMenuPanel extends JPanel {
    private final JLabel lblWelcome = new JLabel();
    private final JButton btnLogout = new JButton("Cerrar sesión");
    private final JButton btnProducts = new JButton("Seleccionar productos");

    public interface LogoutListener {
        void onLogout();
    }

    // Colores y fuentes
    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#DCC184");
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

        // --- INICIO DE CAMBIOS SOLICITADOS ---

        // 1. Añadiendo una imagen en la parte superior del panel
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/recursos/select.png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            JLabel logoLabel = new JLabel(resizedIcon);
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(logoLabel, BorderLayout.NORTH);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
        
        // 2. Panel con bordes redondos para la etiqueta de bienvenida
        JPanel roundedPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(Color.WHITE); // Color de fondo del panel redondo
                // Dibuja un rectángulo con bordes redondeados (30x30 es un buen radio)
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        roundedPanel.setOpaque(false); // Necesario para ver el degradado
        roundedPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Ajuste de espacio
        
        String user = SessionManager.getInstance().getCurrentUser();
        lblWelcome.setText("Bienvenido, " + user + "!");
        lblWelcome.setFont(FONT_TEXT); // Usamos un font de texto para que se ajuste mejor
        lblWelcome.setForeground(Color.BLACK); // Texto negro para el panel blanco
        lblWelcome.setBorder(new EmptyBorder(5, 15, 5, 15)); // Espacio interno
        
        roundedPanel.add(lblWelcome);
        contentPanel.add(roundedPanel, BorderLayout.CENTER);
        
        // 3. Posicionando los botones
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        styleButton(btnProducts);
        styleButton(btnLogout);

        buttonsPanel.add(btnProducts);
        buttonsPanel.add(btnLogout);

        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);
        // --- FIN DE CAMBIOS SOLICITADOS ---

        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);

        // Agrega las acciones de los botones
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

