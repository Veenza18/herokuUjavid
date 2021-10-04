/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;


import java.time.LocalDateTime;

/**
 *
 * @author admin
 */
public class ContactoCercano {
    LocalDateTime fecha_contacto;
    Usuario contacto;

    public ContactoCercano(LocalDateTime fecha_contacto, Usuario contacto) {
        this.fecha_contacto = fecha_contacto;
        this.contacto = contacto;
    }

    public LocalDateTime getFecha_contacto() {
        return fecha_contacto;
    }

    public Usuario getContacto() {
        return contacto;
    }
    
}
