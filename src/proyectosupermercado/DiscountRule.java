/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado;

/**
 *
 * @author pxavi
 */
/**
 * Interfaz para reglas de descuento.
 */
public interface DiscountRule {
    /**
     * Calcula el descuento aplicable a un producto.
     * @param product producto agregado
     * @param quantity cuántos productos iguales hay en el carrito
     * @return monto a descontar
     */
    double apply(Product product, int quantity);
}

