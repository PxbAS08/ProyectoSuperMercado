/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectosupermercado; // Define el paquete al que pertenece la interfaz.

/**
 *
 * @author pxavi // Comentario del autor.
 */
/**
 * Interfaz para reglas de descuento.
 */
public interface DiscountRule { // Declaración de la interfaz pública 'DiscountRule'.
    /**
     * Calcula el descuento aplicable a un producto.
     * @param product producto agregado
     * @param quantity cuántos productos iguales hay en el carrito
     * @return monto a descontar
     */
    // Define un método abstracto que debe ser implementado por cualquier clase que use esta interfaz.
    double apply(Product product, int quantity);
}


