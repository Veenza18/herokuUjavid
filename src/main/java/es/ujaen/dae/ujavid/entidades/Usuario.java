/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author admin
 */
public class Usuario {
    /**UUID del usuario**/ 
    UUID uuid;
    /**Número de teléfono**/
    String numTelefono;
    /**Fecha de alta**/
    LocalDate f_alta;
    /**Fecha de positivo **/
    LocalDateTime f_positivo;
    /**¿Es Positivo?**/
    boolean positivo;
    
    /**Listado de Contactos Cercanos**/
    
}
