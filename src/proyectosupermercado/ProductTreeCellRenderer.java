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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.net.URL;

/**
 * Personaliza los íconos del árbol de productos por categoría principal.
 */
public class ProductTreeCellRenderer extends DefaultTreeCellRenderer {
    private final Icon lecheIcon;
    private final Icon bebidasIcon;
    private final Icon yogurtIcon;
    private final Icon mantequillaIcon;
    private final Icon snacksIcon;
    private final Icon limpiezaIcon;
    private final Icon defaultIcon;

    public ProductTreeCellRenderer() {
        lecheIcon = loadIcon("/Icon/leche.png");
        bebidasIcon = loadIcon("/Icon/bebidas.png");
        yogurtIcon = loadIcon("/Icon/yogur.png");
        mantequillaIcon = loadIcon("/Icon/mantequilla.png");
        snacksIcon = loadIcon("/Icon/papas-fritas.png");
        limpiezaIcon = loadIcon("/Icon/limpieza.png");
        defaultIcon = loadIcon("/Icon/flecha.png"); 
    }

    private Icon loadIcon(String path) {
        URL url = getClass().getResource(path);
        return url != null ? new ImageIcon(url) : null;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean sel, boolean expanded,
                                                  boolean leaf, int row,
                                                  boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();

        if (obj instanceof Product) {
            setIcon(defaultIcon); // productos → ícono genérico
        } else {
            String category = obj.toString().toLowerCase();
            if (category.contains("leche")) setIcon(lecheIcon);
            else if (category.contains("bebida")) setIcon(bebidasIcon);
            else if (category.contains("yogurt")) setIcon(yogurtIcon);
            else if (category.contains("mantequilla y margarina")) setIcon(mantequillaIcon);
            else if (category.contains("snack")) setIcon(snacksIcon);
            else if (category.contains("limpieza")) setIcon(limpiezaIcon);
            else setIcon(defaultIcon);
        }

        return this;
    }
}



