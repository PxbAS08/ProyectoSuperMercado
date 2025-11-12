/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
// Importa la clase 'DefaultMutableTreeNode', que es la clase estándar para crear nodos en un JTree de Swing.
import javax.swing.tree.DefaultMutableTreeNode;
// Importa todas las clases del paquete java.util, que incluye List, Map, ArrayList, LinkedHashMap, etc.
import java.util.*;

// Define la clase pública 'ProductTreeBuilder'. Su propósito es construir una estructura de árbol con productos.
public class ProductTreeBuilder {

    // Define un método público y estático llamado 'buildTree' que acepta una lista de productos y devuelve un nodo de árbol.
    public static DefaultMutableTreeNode buildTree(List<Product> products) {
        // Crea el nodo raíz (el nodo principal) del árbol con el texto "Productos".
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Productos");

        // Declara un mapa anidado para organizar los productos. La estructura será: Categoría -> (Subcategoría -> Lista de Productos).
        // Se usa LinkedHashMap para mantener el orden de inserción de las categorías y subcategorías.
        Map<String, Map<String, List<Product>>> grouped = new LinkedHashMap<>();

        // Inicia un bucle 'for-each' para iterar sobre cada objeto 'Product' en la lista 'products'.
        for (Product p : products) {
            // Accede al mapa 'grouped' para empezar a organizar el producto actual 'p'.
            grouped
                // Busca la categoría del producto. Si no existe como clave en el mapa, la crea y le asigna un nuevo LinkedHashMap vacío.
                .computeIfAbsent(p.getCategory(), k -> new LinkedHashMap<>())
                // Dentro del mapa de la categoría, busca la subcategoría. Si no existe, la crea y le asigna un nuevo ArrayList vacío.
                .computeIfAbsent(p.getSubcategory(), k -> new ArrayList<>())
                // A la lista de productos (de la categoría y subcategoría correctas), le añade el producto actual 'p'.
                .add(p);
        }

        // Inicia un bucle para iterar sobre cada clave (nombre de categoría) en el mapa 'grouped'.
        for (String category : grouped.keySet()) {
            // Crea un nuevo nodo de árbol para la categoría actual.
            DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
            // Obtiene el mapa de subcategorías correspondiente a la categoría actual.
            Map<String, List<Product>> subcats = grouped.get(category);

            // Inicia un bucle anidado para iterar sobre cada clave (nombre de subcategoría) en el mapa de subcategorías.
            for (String subcat : subcats.keySet()) {
                // Crea un nuevo nodo de árbol para la subcategoría actual.
                DefaultMutableTreeNode subcatNode = new DefaultMutableTreeNode(subcat);
                // Itera sobre cada objeto 'Product' en la lista de la subcategoría actual.
                for (Product prod : subcats.get(subcat)) {
                    // Crea un nodo hoja (el último nivel) para el producto y lo añade como hijo del nodo de la subcategoría.
                    subcatNode.add(new DefaultMutableTreeNode(prod));
                }
                // Añade el nodo de la subcategoría (que ya contiene a sus productos) como hijo del nodo de la categoría.
                categoryNode.add(subcatNode);
            }

            // Añade el nodo de la categoría (que ya contiene a sus subcategorías) como hijo del nodo raíz.
            root.add(categoryNode);
        }

        // Devuelve el nodo raíz, que ahora contiene toda la estructura jerárquica del árbol de productos.
        return root;
    }
}


