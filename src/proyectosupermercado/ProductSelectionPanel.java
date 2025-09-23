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
import javax.swing.tree.*;
import java.awt.*;
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

    private final CartManager cartManager;

    public interface BackListener {
        void onBack();
    }

    public ProductSelectionPanel(CartManager cartManager, BackListener backListener) {
        this.cartManager = cartManager;
        setLayout(new BorderLayout(10, 10));

        // Construir árbol
        DefaultMutableTreeNode root = ProductTreeBuilder.buildTree(ProductCatalog.getAllProducts());
        productTree = new JTree(root);
        productTree.setRootVisible(false);
        productTree.setCellRenderer(new ProductTreeCellRenderer()); // usar íconos
        JScrollPane scrollProducts = new JScrollPane(productTree);

        cartArea = new JTextArea(12, 30);
        cartArea.setEditable(false);
        JScrollPane scrollCart = new JScrollPane(cartArea);

        JPanel infoPanel = new JPanel(new GridLayout(2,1));
        infoPanel.add(lblDiscount);
        infoPanel.add(lblTotal);

        JPanel buttons = new JPanel();
        buttons.add(btnAddToCart);
        buttons.add(btnCheckout);
        buttons.add(btnBack);

        // Buscador
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(txtSearch);
        JButton btnSearch = new JButton("🔍");
        searchPanel.add(btnSearch);

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Seleccione productos por categoría:"), BorderLayout.WEST);
        top.add(searchPanel, BorderLayout.EAST);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(infoPanel, BorderLayout.WEST);
        bottom.add(buttons, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(scrollProducts, BorderLayout.CENTER);
        add(scrollCart, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        // Acción agregar producto
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
        StringBuilder sb = new StringBuilder("🛒 Carrito:\n");
        for (Product p : cartManager.getCart()) {
            sb.append("- ").append(p.toString()).append("\n");
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
}

