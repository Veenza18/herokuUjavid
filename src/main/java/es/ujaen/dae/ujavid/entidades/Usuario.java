/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author admin
 */
public class Usuario {

    /**
     * UUID del usuario*
     */
    UUID uuid;

    /**
     * Número de teléfono*
     */
    String numTelefono;

    /**
     * Fecha de alta*
     */
    LocalDate f_alta;

    /**
     * Fecha de positivo *
     */
    LocalDateTime f_positivo;

    /**
     * ¿Es Positivo?*
     */
    boolean positivo;

    /**
     * Cotraseña del usuario
     */
    private String password;

    /**
     * Listado de Contactos Cercanos*
     */
    List<ContactoCercano> listadoContactos;

    /**
     * Constructor parametrizado de al clase Usuario
     *
     * @param numTelefono Nº de teléfono
     * @param f_alta Fecha en la que se registra el usuario
     */
    public Usuario(String numTelefono, String password, LocalDate f_alta) {
        this.uuid = UUID.randomUUID();
        this.password = password;
        this.numTelefono = numTelefono;
        this.f_alta = f_alta;
        this.f_positivo = null;
        this.positivo = false;
        this.listadoContactos = new ArrayList<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getPassword() {
        return password;
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

    public void setF_positivo(LocalDateTime f_positivo) {
        this.f_positivo = f_positivo;
    }

    public boolean isPositivo() {
        return positivo;
    }

    public void setPositivo(boolean positivo) {
        this.positivo = positivo;
    }

    public List<ContactoCercano> getListadoContactos() {
        return listadoContactos;
    }

    
}
