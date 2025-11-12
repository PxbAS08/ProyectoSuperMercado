/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete al que pertenece la clase.

/**
 *
 * @author pxavi // Comentario del autor generado por el IDE.
 */
public class Product { // Declaración de la clase pública 'Product'.
    private final String code; // Atributo final para almacenar el código del producto, no se puede modificar una vez asignado.
    private final String name; // Atributo final para el nombre del producto.
    private final String category; // Atributo final para la categoría del producto.
    private final String subcategory; // Atributo final para la subcategoría.
    private final String size; // Atributo final para el tamaño o presentación del producto.
    private final double price; // Atributo final para el precio del producto.

    // Constructor de la clase 'Product'. Se utiliza para crear nuevas instancias de productos.
    public Product(String code, String name, String category, String subcategory, String size, double price) {
        this.code = code; // Asigna el código recibido como parámetro al atributo 'code' de la instancia.
        this.name = name; // Asigna el nombre.
        this.category = category; // Asigna la categoría.
        this.subcategory = subcategory; // Asigna la subcategoría.
        this.size = size; // Asigna el tamaño.
        this.price = price; // Asigna el precio.
    }

    // Métodos 'getter' para acceder a los atributos privados de la clase desde fuera.
    public String getCode() { return code; } // Devuelve el código del producto.
    public String getName() { return name; } // Devuelve el nombre del producto.
    public String getCategory() { return category; } // Devuelve la categoría.
    public String getSubcategory() { return subcategory; } // Devuelve la subcategoría.
    public String getSize() { return size; } // Devuelve el tamaño.
    public double getPrice() { return price; } // Devuelve el precio.

    @Override // Indica que este método sobrescribe un método de la clase padre (Object).
    public String toString() { // Devuelve una representación en texto del objeto 'Product'.
        // Concatena los atributos del producto en un formato legible.
        return "[" + code + "] " + name + " (" + size + ") - $" + price;
    }
}



