/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class ProductTreeBuilder {

    public static DefaultMutableTreeNode buildTree(List<Product> products) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Productos");

        // Mapear: Categoria -> Subcategoria -> Productos
        Map<String, Map<String, List<Product>>> grouped = new LinkedHashMap<>();

        for (Product p : products) {
            grouped
                .computeIfAbsent(p.getCategory(), k -> new LinkedHashMap<>())
                .computeIfAbsent(p.getSubcategory(), k -> new ArrayList<>())
                .add(p);
        }

        // Construir nodos
        for (String category : grouped.keySet()) {
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
            Map<String, List<Product>> subcats = grouped.get(category);

            for (String subcat : subcats.keySet()) {
                DefaultMutableTreeNode subcatNode = new DefaultMutableTreeNode(subcat);
                for (Product prod : subcats.get(subcat)) {
                    subcatNode.add(new DefaultMutableTreeNode(prod));
                }
                categoryNode.add(subcatNode);
            }

            root.add(categoryNode);
        }

        return root;
    }
}

