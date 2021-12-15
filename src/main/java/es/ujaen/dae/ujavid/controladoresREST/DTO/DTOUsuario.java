/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.controladoresREST.DTO;

import es.ujaen.dae.ujavid.entidades.Usuario;
import es.ujaen.dae.ujavid.util.CodificadorPassword;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author admin
 */
public class DTOUsuario {

    /**
     * UUID del Usuario
     */
    private UUID uuid;

    /**
     * Número de teléfono
     */
    private String numTelefono;

    /**
     * Fecha de curación
     */
    private LocalDate fCuracion;

    /**
     * Fecha de positivo
     */
    private LocalDateTime fPositivo;

    /**
     * ¿Es Positivo?
     */
    boolean positivo;

    /**
     * Cotraseña del usuario
     */
    private String password;

    /**
     * Fecha de alta/registro
     */
    private LocalDate fAlta;

    public DTOUsuario() {
    }

    
    /**
     * Constructor para realizar un POST
     * 
     * @param numTelefono Nº de teléfono del Usuario
     * @param password Contraseña del Usuario
     */
    public DTOUsuario(String numTelefono, String password) {
        this.numTelefono = numTelefono;
        this.password = password;
        this.uuid = null;
        this.fCuracion = null;
        this.fPositivo = null;
        this.positivo = false;
        this.fAlta = null;
    }

    /**
     * Constructor para realizar un GET
     * 
     * @param uuid UUID del Usuario
     * @param numTelefono Nº de teléfono del Usuario
     * @param fCuracion Fecha en la que se curó el Usuario
     * @param fPositivo Última fecha en la que dió positivo
     * @param positivo Si el Usuario es positivo o no
     * @param password Cotraseña(codificada) del Usuario 
     * @param fAlta Fecha en la que se dió de alta
     */
    public DTOUsuario(UUID uuid, String numTelefono, LocalDate fCuracion, LocalDateTime fPositivo, boolean positivo, String password, LocalDate fAlta) {
        this.uuid = uuid;
        this.numTelefono = numTelefono;
        this.fCuracion = fCuracion;
        this.fPositivo = fPositivo;
        this.positivo = positivo;
        this.password = password;
        this.fAlta = fAlta;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public LocalDate getfCuracion() {
        return fCuracion;
    }

    public LocalDateTime getfPositivo() {
        return fPositivo;
    }

    public boolean isPositivo() {
        return positivo;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getfAlta() {
        return fAlta;
    }

    /**
     * Método para pasar de DTO a Usuario
     * @return 
     */
    public Usuario aUsuario() {
        return new Usuario(numTelefono, password);
    }

}
