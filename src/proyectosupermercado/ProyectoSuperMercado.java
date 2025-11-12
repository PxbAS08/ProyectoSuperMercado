/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectosupermercado; // Define el paquete.

/**
 *
 * @author pxavi // Comentario del autor.
 */
import javax.swing.*; // Importa Swing.

public class ProyectoSuperMercado { // Declaración de la clase.
    public static void main(String[] args) { // Punto de entrada de la aplicación.
        // Pone la creación de la GUI en el hilo de despacho de eventos de Swing, que es la práctica correcta.
        SwingUtilities.invokeLater(() -> {
            try {
                // Intenta establecer el "Look and Feel" (apariencia) nativo del sistema operativo.
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // ignorar si falla el L&F
            }
            JFrame frame = new JFrame("ProyectoSuperMercado - Inicio de sesión"); // Crea la ventana principal.
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define que el programa termine al cerrar la ventana.
            frame.setContentPane(new LoginRegisterPanel()); // Establece el panel de login como contenido inicial.
            frame.pack(); // Ajusta el tamaño de la ventana al de sus componentes.
            frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla.
            frame.setVisible(true); // Hace visible la ventana.
        });
    }
}

