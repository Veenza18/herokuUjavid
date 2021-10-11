/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import java.time.LocalDateTime;

/**
 * Clase que representa un Contacto Cercano por parte de un Usuario
 *
 * @author admin
 */
public class ContactoCercano {

    /**
     * Fecha y Hora en ka que se produjo el contacto
     */
    private LocalDateTime fecha_contacto;

    /**
     * Usuario con el que re produjo el contacto
     */
    private Usuario contacto;

    /**
     * Constructor de la clase ContactoCercano
     *
     * @param fecha_contacto Fecha y Hora
     * @param contacto Usuario con el que se produjo el contacto
     */
    public ContactoCercano(LocalDateTime fecha_contacto, Usuario contacto) {
        this.fecha_contacto = fecha_contacto;
        this.contacto = contacto;
    }

    /**
     * Método para obtener la fecha y hora en la que se produjo el contacto
     *
     * @return Fecha y Hora del contacto
     */
    public LocalDateTime getFecha_contacto() {
        return fecha_contacto;
    }

    /**
     * Método para obtener del Usuario con el que se produjo el contacto
     *
     * @return Usuario con el que se produjo el contacto
     */
    public Usuario getContacto() {
        return contacto;
    }

}
