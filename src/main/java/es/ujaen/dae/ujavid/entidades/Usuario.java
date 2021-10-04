/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
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
    List<ContactoCercano> listadoContactos;

    public Usuario(String numTelefono, LocalDate f_alta, LocalDateTime f_positivo, boolean positivo, List<ContactoCercano> listadoContactos) {
        this.uuid = UUID.randomUUID();
        this.numTelefono = numTelefono;
        this.f_alta = f_alta;
        this.f_positivo = f_positivo;
        this.positivo = positivo;
        this.listadoContactos = listadoContactos;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public LocalDate getF_alta() {
        return f_alta;
    }

    public LocalDateTime getF_positivo() {
        return f_positivo;
    }

    public boolean isPositivo() {
        return positivo;
    }

    public List<ContactoCercano> getListadoContactos() {
        return listadoContactos;
    }
    
    
    
    
}
