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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private static final Color PRIMARY_COLOR = Color.decode("#72E872"); // Verde del degradado
    private static final Color SECONDARY_COLOR = Color.decode("#DCC184"); // Color del degradado
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Impact", Font.BOLD, 24);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14);

    public LoginRegisterPanel() {
        // Establece el diseño para el panel principal
        setLayout(new BorderLayout());

        // Se crea el panel para el degradado de fondo
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
        
        // El contenido del panel se agrega a un panel secundario
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false); // Hace el panel transparente para ver el degradado
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Panel para el logo e imagen
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Agregando la imagen del logo
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/recursos/logo.png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            JLabel logoLabel = new JLabel(resizedIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(logoLabel);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }

        // Título estilizado
        JLabel titleLabel = new JLabel("SuperMercado ONIX");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.WHITE); // Cambiado a blanco para mejor contraste
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(titleLabel);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.WEST;

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(FONT_TEXT);
        userLabel.setForeground(Color.WHITE);
        c.gridx = 0; c.gridy = 0; form.add(userLabel, c);
        
        txtUsername.setFont(FONT_TEXT);
        c.gridx = 1; c.gridy = 0; form.add(txtUsername, c);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(FONT_TEXT);
        passwordLabel.setForeground(Color.WHITE);
        c.gridx = 0; c.gridy = 1; form.add(passwordLabel, c);

        txtPassword.setFont(FONT_TEXT);
        c.gridx = 1; c.gridy = 1; form.add(txtPassword, c);

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.setOpaque(false);

        JPanel botones = new JPanel();
        botones.setOpaque(false);
        botones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        styleButton(btnLogin);
        styleButton(btnRegister);
        botones.add(btnLogin);
        botones.add(btnRegister);
        south.add(botones, BorderLayout.NORTH);

        south.add(progressBar, BorderLayout.CENTER);
        
        lblStatus.setFont(FONT_TEXT);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatus.setForeground(Color.WHITE);
        south.add(lblStatus, BorderLayout.SOUTH);

        contentPanel.add(form, BorderLayout.CENTER);
        contentPanel.add(south, BorderLayout.SOUTH);

        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressBar.setBorderPainted(false);
        progressBar.setForeground(Color.WHITE);
        progressBar.setBackground(new Color(255, 255, 255, 128)); // Semi-transparente
        progressBar.setOpaque(false);

        // Agrega el panel de contenido al panel de degradado
        gradientPanel.add(contentPanel);

        // Agrega el panel de degradado al panel principal
        add(gradientPanel, BorderLayout.CENTER);

        // Acciones
        btnLogin.addActionListener(this::onLogin);
        btnRegister.addActionListener(this::onRegister);
    }
    
    // Método para estilizar los botones
    private void styleButton(JButton button) {
        button.setFont(FONT_BUTTON);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK); // Cambiado a negro como solicitaste
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
        AuthThread authTask = new AuthThread(authService, user, pass, AuthThread.Action.LOGIN, new AuthListener() {
            @Override
            public void onAuthProgress(int percent) {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(percent);
                });
            }

            @Override
            public void onAuthSuccess(String message, boolean isRegister) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText(message);
                    setProcessing(false);
                    if (!isRegister) {
                        SessionManager.getInstance().login(user);

                        SwingUtilities.invokeLater(() -> {
                            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginRegisterPanel.this);
                            frame.setContentPane(new MainMenuPanel(() -> {
                                frame.setContentPane(new LoginRegisterPanel());
                                frame.revalidate();
                            }));
                            frame.revalidate();
                            frame.repaint();
                        });
                    }
                    txtPassword.setText("");
                });
            }

            @Override
            public void onAuthFailure(String message) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText(message);
                    setProcessing(false);
                });
            }
        });
        new Thread(authTask, "AuthThread-Login").start();
    }

    private void onRegister(ActionEvent ev) {
        String user = txtUsername.getText().trim();
        char[] pass = txtPassword.getPassword();

        if (!validateInputs(user, pass)) return;

        setProcessing(true);
        lblStatus.setText("Registrando usuario...");
        AuthThread authTask = new AuthThread(authService, user, pass, AuthThread.Action.REGISTER, new AuthListener() {
            @Override
            public void onAuthProgress(int percent) {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(percent);
                });
            }

            @Override
            public void onAuthSuccess(String message, boolean isRegister) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText(message);
                    setProcessing(false);
                    txtPassword.setText("");
                });
            }

            @Override
            public void onAuthFailure(String message) {
                SwingUtilities.invokeLater(() -> {
                    lblStatus.setText(message);
                    setProcessing(false);
                });
            }
        });
        new Thread(authTask, "AuthThread-Register").start();
    }

    private boolean validateInputs(String user, char[] pass) {
        if (user.isEmpty()) {
            lblStatus.setText("El usuario no puede estar vacío.");
            return false;
        }
        if (pass == null || pass.length < 4) {
            lblStatus.setText("La contraseña debe tener al menos 4 caracteres.");
            return false;
        }
        return true;
    }
}