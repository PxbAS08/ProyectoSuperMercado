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
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginRegisterPanel extends JPanel {
    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JButton btnLogin = new JButton("Iniciar sesión");
    private final JButton btnRegister = new JButton("Registrarse");
    private final JLabel lblStatus = new JLabel(" ");
    private final JProgressBar progressBar = new JProgressBar(0, 100);

    // Servicio de autenticación
    private final AuthenticationService authService = new FileAuthenticationService();

    public LoginRegisterPanel() {
        setLayout(new BorderLayout(10, 10));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0; form.add(new JLabel("Usuario:"), c);
        c.gridx = 1; c.gridy = 0; form.add(txtUsername, c);

        c.gridx = 0; c.gridy = 1; form.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; c.gridy = 1; form.add(txtPassword, c);

        JPanel botones = new JPanel();
        botones.add(btnLogin);
        botones.add(btnRegister);

        JPanel south = new JPanel(new BorderLayout(5,5));
        south.add(botones, BorderLayout.NORTH);
        south.add(progressBar, BorderLayout.CENTER);
        south.add(lblStatus, BorderLayout.SOUTH);

        add(form, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        // Acciones
        btnLogin.addActionListener(this::onLogin);
        btnRegister.addActionListener(this::onRegister);
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
                        // Guardar sesión
                        SessionManager.getInstance().login(user);

                        SwingUtilities.invokeLater(() -> {
                            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginRegisterPanel.this);
                            frame.setContentPane(new MainMenuPanel(() -> {
                                // Acción al cerrar sesión: volver al login
                                frame.setContentPane(new LoginRegisterPanel());
                                frame.revalidate();
                            }));
                            frame.revalidate();
                            frame.repaint();
                        });
                    }
                    // limpiar
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

