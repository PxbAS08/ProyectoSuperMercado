/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
public class Product {
    private final String code;
    private final String name;
    private final String category;
    private final String subcategory;
    private final String size;
    private final double price;

    public Product(String code, String name, String category, String subcategory, String size, double price) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
        this.size = size;
        this.price = price;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getSubcategory() { return subcategory; }
    public String getSize() { return size; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "[" + code + "] " + name + " (" + size + ") - $" + price;
    }
}


