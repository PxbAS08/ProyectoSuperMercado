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

// Define la clase pública 'LoginRegisterPanel' que hereda de 'JPanel', convirtiéndola en un componente de la interfaz gráfica de Swing.
public class LoginRegisterPanel extends JPanel {
    // Declara un campo de texto privado y final para que el usuario ingrese su nombre.
    private final JTextField txtUsername = new JTextField(18);
    // Declara un campo de contraseña privado y final, que oculta los caracteres a medida que se escriben.
    private final JPasswordField txtPassword = new JPasswordField(18);
    // Declara un botón privado y final para iniciar el proceso de inicio de sesión.
    private final JButton btnLogin = new JButton("Iniciar sesión");
    // Declara un botón privado y final para iniciar el proceso de registro.
    private final JButton btnRegister = new JButton("Registrarse");
    // Declara una etiqueta privada y final para mostrar mensajes de estado o error al usuario.
    private final JLabel lblStatus = new JLabel(" ");
    // Declara una barra de progreso privada y final para dar retroalimentación visual durante operaciones largas.
    private final JProgressBar progressBar = new JProgressBar(0, 100);

    // Crea una instancia privada y final del servicio de autenticación, usando la implementación basada en archivos.
    private final AuthenticationService authService = new FileAuthenticationService();

    // Declara constantes estáticas y finales para definir la paleta de colores y las fuentes de la interfaz.
    private static final Color PRIMARY_COLOR = Color.decode("#72E872"); // Color principal verde.
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF"); // Color secundario blanco.
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0"); // Color para cuando el cursor está sobre un botón.
    private static final Color ERROR_COLOR = Color.decode("#e74c3c"); // Color rojo para mensajes de error.
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 24); // Fuente para el título principal.
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14); // Fuente para texto normal.
    private static final Font FONT_BUTTON = new Font("Cascadia Code", Font.BOLD, 14); // Fuente para el texto de los botones.


    // Constructor público de la clase, se ejecuta al crear una nueva instancia del panel.
    public LoginRegisterPanel() {
        // Establece el administrador de diseño del panel principal como BorderLayout.
        setLayout(new BorderLayout());

        // Crea un panel anónimo que sobrescribe su método de pintado para crear un fondo con degradado.
        JPanel gradientPanel = new JPanel() {
            // Sobrescribe el método que se encarga de dibujar el componente.
            @Override
            protected void paintComponent(Graphics g) {
                // Llama al método original de la clase padre para asegurar que las operaciones de pintado base se realicen.
                super.paintComponent(g);
                // Convierte el objeto Graphics a Graphics2D para acceder a funciones de dibujo avanzadas.
                Graphics2D g2d = (Graphics2D) g;
                // Activa una pista de renderizado para mejorar la calidad del dibujo.
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // Obtiene el ancho actual del panel.
                int w = getWidth();
                // Obtiene la altura actual del panel.
                int h = getHeight();
                // Asigna el color primario definido.
                Color color1 = PRIMARY_COLOR;
                // Asigna el color secundario definido.
                Color color2 = SECONDARY_COLOR;
                // Crea un objeto GradientPaint que define un degradado lineal desde el color1 (arriba) hasta el color2 (abajo).
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                // Establece el degradado como el "pincel" de dibujo actual.
                g2d.setPaint(gp);
                // Dibuja un rectángulo relleno que cubre todo el panel con el degradado.
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Crea un panel para el contenido principal con un BorderLayout y márgenes.
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        // Hace que el panel de contenido sea transparente para que se vea el degradado del panel de fondo.
        contentPanel.setOpaque(false);
        // Establece un borde vacío alrededor del panel de contenido para crear un margen interior de 40 píxeles.
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Crea un panel para la cabecera (logo y título).
        JPanel headerPanel = new JPanel();
        // Establece un BoxLayout que organiza los componentes verticalmente (en el eje Y).
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        // Hace que el panel de la cabecera sea transparente.
        headerPanel.setOpaque(false);

        // Inicia un bloque try-catch para manejar posibles errores al cargar la imagen del logo.
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

        // Crea la etiqueta para el título del supermercado.
        JLabel titleLabel = new JLabel("SuperMercado ONIX");
        // Establece la fuente del título.
        titleLabel.setFont(FONT_TITLE);
        // Establece el color del texto del título a negro.
        titleLabel.setForeground(Color.BLACK);
        // Centra la etiqueta del título horizontalmente.
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Añade un espacio vertical rígido de 10 píxeles debajo del logo.
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        // Añade la etiqueta del título al panel de la cabecera.
        headerPanel.add(titleLabel);

        // Añade el panel de la cabecera a la parte superior (NORTH) del panel de contenido.
        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Crea un panel para el formulario de entrada (usuario y contraseña) usando GridBagLayout para un posicionamiento preciso.
        JPanel form = new JPanel(new GridBagLayout());
        // Hace que el panel del formulario sea transparente.
        form.setOpaque(false);
        // Crea un objeto GridBagConstraints para configurar la posición y el comportamiento de los componentes.
        GridBagConstraints c = new GridBagConstraints();
        // Establece un margen de 10 píxeles alrededor de cada componente en el formulario.
        c.insets = new Insets(10, 10, 10, 10);
        // Alinea los componentes a la izquierda (oeste) dentro de su celda.
        c.anchor = GridBagConstraints.WEST;

        // Crea la etiqueta "Usuario:".
        JLabel userLabel = new JLabel("Usuario:");
        // Establece la fuente de la etiqueta.
        userLabel.setFont(FONT_TEXT);
        // Establece el color del texto de la etiqueta a negro.
        userLabel.setForeground(Color.BLACK);
        // Define la posición de la etiqueta en la primera columna (0) y primera fila (0) del grid.
        c.gridx = 0; c.gridy = 0; form.add(userLabel, c);
        
        // Establece la fuente para el campo de texto del nombre de usuario.
        txtUsername.setFont(FONT_TEXT);
        // Define la posición del campo de texto en la segunda columna (1) y primera fila (0).
        c.gridx = 1; c.gridy = 0; form.add(txtUsername, c);

        // Crea la etiqueta "Contraseña:".
        JLabel passwordLabel = new JLabel("Contraseña:");
        // Establece la fuente de la etiqueta.
        passwordLabel.setFont(FONT_TEXT);
        // Establece el color del texto de la etiqueta a negro.
        passwordLabel.setForeground(Color.BLACK);
        // Define la posición de la etiqueta en la primera columna (0) y segunda fila (1).
        c.gridx = 0; c.gridy = 1; form.add(passwordLabel, c);

        // Establece la fuente para el campo de texto de la contraseña.
        txtPassword.setFont(FONT_TEXT);
        // Define la posición del campo de contraseña en la segunda columna (1) y segunda fila (1).
        c.gridx = 1; c.gridy = 1; form.add(txtPassword, c);

        // Crea un panel para contener los botones de login y registro.
        JPanel botones = new JPanel();
        // Hace que el panel de botones sea transparente.
        botones.setOpaque(false);
        // Establece un FlowLayout que centra los botones y les da un espacio horizontal de 10 píxeles.
        botones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        // Llama al método 'styleButton' para aplicar un estilo personalizado al botón de login.
        styleButton(btnLogin);
        // Llama al método 'styleButton' para aplicar el mismo estilo al botón de registro.
        styleButton(btnRegister);
        // Añade el botón de login al panel de botones.
        botones.add(btnLogin);
        // Añade el botón de registro al panel de botones.
        botones.add(btnRegister);

        // Crea un panel para la sección inferior de la interfaz (botones, barra de progreso y estado).
        JPanel south = new JPanel(new BorderLayout(5,5));
        // Hace que este panel sea transparente.
        south.setOpaque(false);
        // Añade el panel de botones a la parte superior (NORTH) de la sección inferior.
        south.add(botones, BorderLayout.NORTH);
        // Añade la barra de progreso al centro de la sección inferior.
        south.add(progressBar, BorderLayout.CENTER);
        
        // Establece la fuente para la etiqueta de estado.
        lblStatus.setFont(FONT_TEXT);
        // Centra el texto de la etiqueta de estado horizontalmente.
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        // Establece el color del texto de la etiqueta a blanco.
        lblStatus.setForeground(Color.WHITE);
        // Añade la etiqueta de estado a la parte inferior (SOUTH) de la sección inferior.
        south.add(lblStatus, BorderLayout.SOUTH);

        // Añade el panel del formulario al centro del panel de contenido.
        contentPanel.add(form, BorderLayout.CENTER);
        // Añade la sección inferior (south) a la parte inferior del panel de contenido.
        contentPanel.add(south, BorderLayout.SOUTH);

        // Configura la barra de progreso para que muestre el porcentaje como texto (ej. "50%").
        progressBar.setStringPainted(true);
        // Inicializa el valor de la barra de progreso en 0.
        progressBar.setValue(0);
        // Desactiva el pintado del borde de la barra de progreso para un look más limpio.
        progressBar.setBorderPainted(false);
        // Establece el color de la barra de progreso (la parte que se llena) a blanco.
        progressBar.setForeground(Color.WHITE);
        // Establece el color de fondo de la barra de progreso a un blanco semitransparente.
        progressBar.setBackground(new Color(255, 255, 255, 128));

        // Añade el panel de contenido (que es transparente) sobre el panel con el degradado.
        gradientPanel.add(contentPanel);
        // Añade el panel del degradado (que ahora contiene todo) al centro del panel principal 'LoginRegisterPanel'.
        add(gradientPanel, BorderLayout.CENTER);

        // Asocia el método 'onLogin' como el manejador de eventos para el botón de login.
        btnLogin.addActionListener(this::onLogin);
        // Asocia el método 'onRegister' como el manejador de eventos para el botón de registro.
        btnRegister.addActionListener(this::onRegister);
    }
    
    // Método privado para habilitar o deshabilitar los componentes de la interfaz durante el procesamiento.
    private void setProcessing(boolean processing) {
        // Habilita o deshabilita el botón de login.
        btnLogin.setEnabled(!processing);
        // Habilita o deshabilita el botón de registro.
        btnRegister.setEnabled(!processing);
        // Habilita o deshabilita el campo de usuario.
        txtUsername.setEnabled(!processing);
        // Habilita o deshabilita el campo de contraseña.
        txtPassword.setEnabled(!processing);
        // Pone la barra de progreso en modo indeterminado (animación de ida y vuelta) si se está procesando.
        progressBar.setIndeterminate(processing);
        // Si no se está procesando, resetea el valor de la barra de progreso a 0.
        if (!processing) progressBar.setValue(0);
    }

    // Método que se ejecuta cuando se hace clic en el botón de login.
    private void onLogin(ActionEvent ev) {
        // Obtiene el texto del campo de usuario y elimina espacios en blanco al principio y al final.
        String user = txtUsername.getText().trim();
        // Obtiene la contraseña del campo de contraseña como un array de caracteres.
        char[] pass = txtPassword.getPassword();

        // Llama al método de validación; si los datos no son válidos...
        if (!validateInputs(user, pass)) {
            // ...establece el color del texto de estado a rojo de error...
            lblStatus.setForeground(ERROR_COLOR);
            // ...y termina la ejecución del método.
            return;
        }

        // Deshabilita los controles de la interfaz para indicar que se está procesando la solicitud.
        setProcessing(true);
        // Actualiza la etiqueta de estado.
        lblStatus.setText("Iniciando sesión...");
        // Establece el color del texto de estado a negro.
        lblStatus.setForeground(Color.BLACK);
        // Crea una nueva instancia de AuthThread para realizar el login en un hilo separado.
        AuthThread authTask = new AuthThread(authService, user, pass, AuthThread.Action.LOGIN, new AuthListener() {
            // Implementación del método que se llama para actualizar el progreso.
            @Override
            public void onAuthProgress(int percent) {
                // Usa SwingUtilities.invokeLater para asegurar que la actualización de la GUI se haga en el hilo correcto.
                SwingUtilities.invokeLater(() -> {
                    // Saca la barra de progreso del modo indeterminado.
                    progressBar.setIndeterminate(false);
                    // Actualiza el valor de la barra de progreso.
                    progressBar.setValue(percent);
                });
            }

            // Implementación del método que se llama cuando la autenticación es exitosa.
            @Override
            public void onAuthSuccess(String message, boolean isRegister) {
                SwingUtilities.invokeLater(() -> {
                    // Muestra el mensaje de éxito en la etiqueta de estado.
                    lblStatus.setText(message);
                    // Establece el color del texto de estado a blanco.
                    lblStatus.setForeground(Color.WHITE);
                    // Vuelve a habilitar los controles de la interfaz.
                    setProcessing(false);
                    // Si la acción fue un login (y no un registro)...
                    if (!isRegister) {
                        // ...inicia la sesión del usuario en el SessionManager.
                        SessionManager.getInstance().login(user);

                        // Usa invokeLater para realizar el cambio de panel en el hilo de Swing.
                        SwingUtilities.invokeLater(() -> {
                            // Obtiene la ventana (JFrame) que contiene este panel.
                            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginRegisterPanel.this);
                            // Cambia el contenido de la ventana al panel del menú principal.
                            frame.setContentPane(new MainMenuPanel(() -> {
                                // Define la acción de logout: volver a mostrar el panel de login.
                                frame.setContentPane(new LoginRegisterPanel());
                                // Valida de nuevo el layout del frame.
                                frame.revalidate();
                            }));
                            // Vuelve a validar y repintar el frame para mostrar los cambios.
                            frame.revalidate();
                            frame.repaint();
                        });
                    }
                    // Limpia el campo de la contraseña por seguridad.
                    txtPassword.setText("");
                });
            }

            // Implementación del método que se llama cuando la autenticación falla.
            @Override
            public void onAuthFailure(String message) {
                SwingUtilities.invokeLater(() -> {
                    // Muestra el mensaje de error.
                    lblStatus.setText(message);
                    // Establece el color del texto a rojo de error.
                    lblStatus.setForeground(ERROR_COLOR);
                    // Vuelve a habilitar los controles.
                    setProcessing(false);
                });
            }
        });
        // Inicia el hilo de autenticación que acabamos de crear.
        new Thread(authTask, "AuthThread-Login").start();
    }

    // Método que se ejecuta cuando se hace clic en el botón de registro (la lógica es muy similar a onLogin).
    private void onRegister(ActionEvent ev) {
        // Obtiene el usuario y la contraseña de los campos de texto.
        String user = txtUsername.getText().trim();
        char[] pass = txtPassword.getPassword();

        // Valida los datos de entrada.
        if (!validateInputs(user, pass)) {
            // Si son inválidos, muestra un error y termina.
            lblStatus.setForeground(ERROR_COLOR);
            return;
        }

        // Deshabilita los controles e informa al usuario que se está registrando.
        setProcessing(true);
        lblStatus.setText("Registrando usuario...");
        lblStatus.setForeground(Color.BLACK);
        // Crea un hilo de autenticación, pero esta vez con la acción de REGISTRO.
        AuthThread authTask = new AuthThread(authService, user, pass, AuthThread.Action.REGISTER, new AuthListener() {
            // Implementación del callback de progreso.
            @Override
            public void onAuthProgress(int percent) {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(percent);
                });
            }

            // Implementación del callback de éxito.
            @Override
            public void onAuthSuccess(String message, boolean isRegister) {
                SwingUtilities.invokeLater(() -> {
                    // Muestra el mensaje de éxito.
                    lblStatus.setText(message);
                    lblStatus.setForeground(Color.WHITE);
                    // Habilita los controles.
                    setProcessing(false);
                    // Limpia el campo de contraseña.
                    txtPassword.setText("");
                });
            }

            // Implementación del callback de fallo.
            @Override
            public void onAuthFailure(String message) {
                SwingUtilities.invokeLater(() -> {
                    // Muestra el mensaje de error.
                    lblStatus.setText(message);
                    lblStatus.setForeground(ERROR_COLOR);
                    // Habilita los controles.
                    setProcessing(false);
                });
            }
        });
        // Inicia el hilo de registro.
        new Thread(authTask, "AuthThread-Register").start();
    }

    // Método privado para validar que los campos de entrada no estén vacíos o sean inválidos.
    private boolean validateInputs(String user, char[] pass) {
        // Comprueba si el nombre de usuario está vacío.
        if (user.isEmpty()) {
            // Si lo está, muestra un mensaje de error.
            lblStatus.setText("El usuario no puede estar vacío.");
            // Devuelve falso para indicar que la validación falló.
            return false;
        }
        // Comprueba si la contraseña es nula o tiene menos de 4 caracteres.
        if (pass == null || pass.length < 4) {
            // Si es así, muestra un mensaje de error.
            lblStatus.setText("La contraseña debe tener al menos 4 caracteres.");
            // Devuelve falso.
            return false;
        }
        // Si todas las validaciones pasan, devuelve verdadero.
        return true;
    }

    // Método privado para aplicar un estilo visual consistente a un botón.
    private void styleButton(JButton button) {
        // Establece la fuente del botón.
        button.setFont(FONT_BUTTON);
        // Establece el color de fondo del botón a blanco.
        button.setBackground(Color.WHITE);
        // Establece el color del texto del botón a negro.
        button.setForeground(Color.BLACK);
        // Evita que se dibuje un recuadro de enfoque alrededor del texto del botón cuando se selecciona.
        button.setFocusPainted(false);
        // Establece un borde vacío para crear un padding (relleno) interior.
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        // Cambia el cursor a una mano cuando el ratón pasa por encima del botón.
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Añade un listener para los eventos del ratón (entrar y salir del área del botón).
        button.addMouseListener(new MouseAdapter() {
            // Método que se ejecuta cuando el cursor entra en el área del botón.
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Cambia el color de fondo del botón al color de 'hover'.
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            // Método que se ejecuta cuando el cursor sale del área del botón.
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Restaura el color de fondo original del botón (blanco).
                button.setBackground(Color.WHITE);
            }
        });
    }
}