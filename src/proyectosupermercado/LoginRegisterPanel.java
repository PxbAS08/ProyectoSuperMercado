package proyectosupermercado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;

public class LoginRegisterPanel extends JPanel {
    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JButton btnLogin = new JButton("Iniciar sesión");
    private final JButton btnRegister = new JButton("Registrarse");
    private final JLabel lblStatus = new JLabel(" ");
    private final JProgressBar progressBar = new JProgressBar(0, 100);

    // Servicio de autenticación
    private final AuthenticationService authService = new FileAuthenticationService();

    // Colores y fuentes
    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Color ERROR_COLOR = Color.decode("#e74c3c");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 24);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);

    public LoginRegisterPanel() {
        setLayout(new BorderLayout());
        
        setupUI(); // Método auxiliar para no repetir código en esta respuesta
    }

    private void setupUI() {
        JPanel gradientPanel = new JPanel() {
             @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(); int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 0, h, SECONDARY_COLOR);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        try {
            // Carga el ícono de la imagen desde la carpeta de recursos del proyecto.
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/recursos/logo.png"));
            // Obtiene el objeto Image del ícono.
            Image originalImage = originalIcon.getImage();
            // Redimensiona la imagen a 128x128 píxeles, usando un algoritmo de escalado suave (SCALE_SMOOTH).
            Image resizedImage = originalImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            // Crea un nuevo ícono a partir de la imagen redimensionada.
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            // Crea una etiqueta (JLabel) para mostrar el ícono del logo.
            JLabel logoLabel = new JLabel(resizedIcon);
            // Centra la etiqueta del logo horizontalmente dentro del headerPanel.
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Añade la etiqueta del logo al panel de la cabecera.
            headerPanel.add(logoLabel);
        // Captura cualquier excepción que pueda ocurrir durante la carga de la imagen.
        } catch (Exception e) {
            // Imprime un mensaje de error en la consola si la imagen no se pudo cargar.
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
        JLabel titleLabel = new JLabel("SuperMercado ONIX");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(titleLabel);
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;
        c.gridx=0; c.gridy=0; form.add(new JLabel("Usuario:"), c);
        c.gridx=1; c.gridy=0; form.add(txtUsername, c);
        c.gridx=0; c.gridy=1; form.add(new JLabel("Contraseña:"), c);
        c.gridx=1; c.gridy=1; form.add(txtPassword, c);
        
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setOpaque(false);
        styleButton(btnLogin); styleButton(btnRegister);
        botones.add(btnLogin); botones.add(btnRegister);

        JPanel south = new JPanel(new BorderLayout());
        south.setOpaque(false);
        south.add(botones, BorderLayout.NORTH);
        south.add(progressBar, BorderLayout.CENTER);
        south.add(lblStatus, BorderLayout.SOUTH);
        
        contentPanel.add(form, BorderLayout.CENTER);
        contentPanel.add(south, BorderLayout.SOUTH);
        gradientPanel.add(contentPanel);
        add(gradientPanel, BorderLayout.CENTER);
        
        btnLogin.addActionListener(this::onLogin);
        btnRegister.addActionListener(this::onRegister);
    }
    
    // --- LÓGICA ACTUALIZADA ---

    private void setProcessing(boolean processing) {
        btnLogin.setEnabled(!processing);
        btnRegister.setEnabled(!processing);
        txtUsername.setEnabled(!processing);
        txtPassword.setEnabled(!processing);
        progressBar.setIndeterminate(processing);
        if (!processing) progressBar.setValue(0);
    }

    private void onLogin(ActionEvent ev) {
        String user = txtUsername.getText().trim();
        char[] pass = txtPassword.getPassword();

        if (!validateInputs(user, pass)) return;

        setProcessing(true);
        lblStatus.setText("Iniciando sesión...");
        
        // Usamos un hilo simple aquí para no complicar con AuthThread modificado
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simular carga
                User loggedUser = authService.login(user, pass);
                
                SwingUtilities.invokeLater(() -> {
                    if (loggedUser != null) {
                        SessionManager.getInstance().login(loggedUser); // Guardar objeto completo
                        lblStatus.setText("Bienvenido " + loggedUser.getUsername());
                        
                        // Cambiar a MainMenu
                         JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginRegisterPanel.this);
                         // Pasamos un listener que RECREA el LoginPanel al salir
                         frame.setContentPane(new MainMenuPanel(() -> {
                             frame.setContentPane(new LoginRegisterPanel());
                             frame.revalidate();
                         }));
                         frame.revalidate();
                         frame.repaint();
                    } else {
                        lblStatus.setText("Usuario o contraseña incorrectos.");
                        lblStatus.setForeground(ERROR_COLOR);
                        setProcessing(false);
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Error: " + e.getMessage());
                    setProcessing(false);
                });
            }
        }).start();
    }

    private void onRegister(ActionEvent ev) {
        String user = txtUsername.getText().trim();
        char[] pass = txtPassword.getPassword();

        if (!validateInputs(user, pass)) return;

        setProcessing(true);
        lblStatus.setText("Registrando...");
        
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                // REGISTRAMOS COMO "CLIENT" POR DEFECTO
                boolean success = authService.register(user, pass, "CLIENT");
                
                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        lblStatus.setText("Registro exitoso. Inicia sesión.");
                        lblStatus.setForeground(Color.BLACK);
                        txtPassword.setText("");
                    } else {
                        lblStatus.setText("El usuario ya existe.");
                        lblStatus.setForeground(ERROR_COLOR);
                    }
                    setProcessing(false);
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText("Error: " + e.getMessage());
                    setProcessing(false);
                });
            }
        }).start();
    }

    private boolean validateInputs(String user, char[] pass) {
        if (user.isEmpty() || pass == null || pass.length < 4) {
            lblStatus.setText("Datos inválidos (min 4 caracteres).");
            lblStatus.setForeground(ERROR_COLOR);
            return false;
        }
        return true;
    }

    private void styleButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.setBackground(BUTTON_HOVER_COLOR); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.setBackground(Color.WHITE); }
        });
    }
}