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
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * Panel de selección de productos con árbol jerárquico, íconos y buscador.
 */
public class ProductSelectionPanel extends JPanel {
    private final JTree productTree;
    private final JTextField txtSearch = new JTextField(15);
    private final JTextArea cartArea;
    private final JLabel lblTotal = new JLabel("Total: $0.00");
    private final JLabel lblDiscount = new JLabel("Descuento: $0.00");
    private final JButton btnAddToCart = new JButton("Agregar al carrito");
    private final JButton btnCheckout = new JButton("Finalizar compra");
    private final JButton btnBack = new JButton("Regresar al menú");
    private final JButton btnClearCart = new JButton("Borrar"); // Nuevo botón para borrar el carrito

    private final CartManager cartManager;

    public interface BackListener {
        void onBack();
    }

    // Colores y fuentes para el diseño
    private static final Color PRIMARY_COLOR = Color.decode("#72E872");
    private static final Color SECONDARY_COLOR = Color.decode("#FFFFFF");
    private static final Color BUTTON_HOVER_COLOR = Color.decode("#A0D8A0");
    private static final Font FONT_TITLE = new Font("Cascadia Code", Font.BOLD, 18);
    private static final Font FONT_TEXT = new Font("Cascadia Code", Font.PLAIN, 14);
    private static final Font FONT_BUTTON = new Font("Monospaced", Font.BOLD, 14);
    private static final Font FONT_MONOSPACED = new Font("Monospaced", Font.PLAIN, 12);

    public ProductSelectionPanel(CartManager cartManager, BackListener backListener) {
        this.cartManager = cartManager;
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
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Panel superior para el logo y el titulo principal
        JPanel mainHeaderPanel = new JPanel(new BorderLayout());
        mainHeaderPanel.setOpaque(false);
        JLabel mainTitleLabel = new JLabel("SuperMercado ONIX");
        mainTitleLabel.setFont(new Font("Cascadia Code", Font.BOLD, 24));
        mainTitleLabel.setForeground(Color.BLACK);
        mainTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Agregar la imagen del logo en la esquina superior izquierda
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/recursos/logo.png"));
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            JLabel logoLabel = new JLabel(resizedIcon);
            logoPanel.add(logoLabel);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
        
        mainHeaderPanel.add(mainTitleLabel, BorderLayout.CENTER);
        mainHeaderPanel.add(logoPanel, BorderLayout.WEST);

        // Título de la sección y buscador
        JLabel headerLabel = new JLabel("PRODUCTOS:");
        headerLabel.setFont(FONT_TITLE);
        headerLabel.setForeground(Color.BLACK);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(FONT_TEXT);
        searchLabel.setForeground(Color.BLACK);
        txtSearch.setFont(FONT_TEXT);
        JButton btnSearch = new JButton("🔍");
        btnSearch.setFont(FONT_BUTTON);
        btnSearch.setBackground(Color.WHITE);
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchPanel.add(searchLabel);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(headerLabel, BorderLayout.WEST);
        top.add(searchPanel, BorderLayout.EAST);

        // Crear un panel para agrupar el encabezado principal y el secundario
        JPanel fullTopPanel = new JPanel(new BorderLayout());
        fullTopPanel.setOpaque(false);
        fullTopPanel.add(mainHeaderPanel, BorderLayout.NORTH);
        fullTopPanel.add(top, BorderLayout.CENTER);
        
        // Crear árbol de productos
        DefaultMutableTreeNode root = ProductTreeBuilder.buildTree(ProductCatalog.getAllProducts());
        productTree = new JTree(root);
        productTree.setRootVisible(false);
        productTree.setCellRenderer(new ProductTreeCellRenderer());
        productTree.setFont(FONT_TEXT);
        productTree.setBackground(Color.WHITE);
        productTree.setOpaque(true);
        JScrollPane scrollProducts = new JScrollPane(productTree);
        scrollProducts.setOpaque(false);
        scrollProducts.getViewport().setOpaque(true);

        // Panel lateral derecho para carrito y totales
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setOpaque(false);

        //Header del carrito con el nuevo botón
        JPanel cartHeader = new JPanel(new BorderLayout());
        cartHeader.setOpaque(false);
        JLabel cartLabel = new JLabel(" ");
        cartLabel.setFont(FONT_TEXT);
        cartLabel.setForeground(Color.WHITE);
        
        btnClearCart.setPreferredSize(new Dimension(50, 25)); // Tamaño pequeño
        btnClearCart.setBackground(Color.decode("#e74c3c")); // Color de alerta
        btnClearCart.setForeground(Color.BLACK);
        btnClearCart.setBorder(BorderFactory.createEmptyBorder());
        btnClearCart.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cartHeader.add(cartLabel, BorderLayout.WEST);
        cartHeader.add(btnClearCart, BorderLayout.EAST);
        
        // Área de texto del carrito (ahora más ancho)
        cartArea = new JTextArea(12, 32); // Incremento de ancho (de 30 a 40)
        cartArea.setEditable(false);
        cartArea.setFont(FONT_MONOSPACED);
        cartArea.setForeground(Color.BLACK);
        cartArea.setBackground(Color.WHITE);
        cartArea.setOpaque(true);
        JScrollPane scrollCart = new JScrollPane(cartArea);
        scrollCart.setOpaque(false);
        scrollCart.getViewport().setOpaque(true);

        // Panel para los totales
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setOpaque(false);
        
        lblDiscount.setFont(FONT_TEXT);
        lblDiscount.setForeground(Color.BLACK);
        lblTotal.setFont(FONT_TEXT);
        lblTotal.setForeground(Color.RED);
        
        infoPanel.add(lblDiscount);
        infoPanel.add(lblTotal);
        
        // Panel que agrupa el header del carrito, el área de texto y los totales
        JPanel fullCartPanel = new JPanel(new BorderLayout(5,5));
        fullCartPanel.setOpaque(false);
        fullCartPanel.add(cartHeader, BorderLayout.NORTH);
        fullCartPanel.add(scrollCart, BorderLayout.CENTER);
        fullCartPanel.add(infoPanel, BorderLayout.SOUTH);

        rightPanel.add(fullCartPanel, BorderLayout.CENTER);


        // Panel para los botones
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        styleButton(btnAddToCart);
        styleButton(btnCheckout);
        styleButton(btnBack);
        buttons.add(btnAddToCart);
        buttons.add(btnCheckout);
        buttons.add(btnBack);

        // Añadiendo todos los componentes al panel de contenido
        contentPanel.add(fullTopPanel, BorderLayout.NORTH);
        contentPanel.add(scrollProducts, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        contentPanel.add(buttons, BorderLayout.SOUTH);

        // Agrega el panel de contenido al panel de degradado
        gradientPanel.add(contentPanel);

        // Agrega el panel de degradado al panel principal
        add(gradientPanel, BorderLayout.CENTER);

        // Acción de agregar producto
        btnAddToCart.addActionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) productTree.getLastSelectedPathComponent();
            if (node == null) return;
            Object obj = node.getUserObject();
            if (obj instanceof Product) {
                Product selected = (Product) obj;
                new Thread(() -> {
                    cartManager.addToCart(selected);
                    SwingUtilities.invokeLater(this::updateCart);
                    new Thread(new CartCalculatorThread(cartManager, (newTotal, discount) -> {
                        SwingUtilities.invokeLater(() -> {
                            lblDiscount.setText(String.format("Descuento: $%.2f", discount));
                            lblTotal.setText(String.format("Total: $%.2f", newTotal));
                        });
                    })).start();
                }).start();
            }
        });
        
        // Acción del nuevo botón de borrar carrito
        btnClearCart.addActionListener(e -> {
            cartManager.clearCart();
            updateCart();
        });

        // Acción finalizar compra
        btnCheckout.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ProductSelectionPanel.this);
            PurchaseManager pm = new PurchaseManager(cartManager);
            frame.setContentPane(new PurchasePanel(pm, () -> {
                frame.setContentPane(new ProductSelectionPanel(cartManager, backListener));
                frame.revalidate();
                frame.repaint();
            }));
            frame.revalidate();
            frame.repaint();
        });

        // Acción regresar
        btnBack.addActionListener(e -> {
            if (backListener != null) backListener.onBack();
        });

        // Acción buscar
        btnSearch.addActionListener(e -> searchProduct(txtSearch.getText().trim()));

        updateCart();
    }

    private void updateCart() {
        // Se usará una cadena con formato para alinear las columnas
        StringBuilder sb = new StringBuilder("🛒 Carrito:\n\n");
        sb.append(String.format("%-25s %s\n", "Descripción", "Precio"));
        sb.append("--------------------------------\n");
        for (Product p : cartManager.getCart()) {
            String name = p.getName() + " (" + p.getSize() + ")";
            sb.append(String.format("%-25s $%.2f\n", name, p.getPrice()));
        }
        cartArea.setText(sb.toString());
        lblDiscount.setText(String.format("Descuento: $%.2f", cartManager.getDiscount()));
        lblTotal.setText(String.format("Total: $%.2f", cartManager.getTotal()));
    }

    /**
     * Busca un producto en el árbol y lo selecciona.
     */
    private void searchProduct(String query) {
        if (query.isEmpty()) return;
        TreeModel model = productTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode found = searchNode(root, query.toLowerCase());

        if (found != null) {
            TreePath path = new TreePath(found.getPath());
            productTree.scrollPathToVisible(path);
            productTree.setSelectionPath(path);
        } else {
            JOptionPane.showMessageDialog(this, "Producto no encontrado.", "Buscar", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private DefaultMutableTreeNode searchNode(DefaultMutableTreeNode root, String query) {
        @SuppressWarnings("unchecked")
        Enumeration<TreeNode> e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            Object obj = node.getUserObject();
            if (obj instanceof Product) {
                Product p = (Product) obj;
                if (p.getName().toLowerCase().contains(query)) {
                    return node;
                }
            }
        }
        return null;
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
