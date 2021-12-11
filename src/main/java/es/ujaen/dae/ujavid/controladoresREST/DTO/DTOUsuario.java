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

    public DTOUsuario(UUID uuid, String numTelefono, LocalDate fCuracion, LocalDateTime fPositivo, boolean positivo, String password, LocalDate fAlta) {
        this.uuid = uuid;
        this.numTelefono = numTelefono;
        this.fCuracion = fCuracion;
        this.fPositivo = fPositivo;
        this.positivo = positivo;
        this.password = CodificadorPassword.codificar(password);
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
     public Usuario aUsuario() {
         
        return new Usuario(uuid,numTelefono, fCuracion,fPositivo,positivo,password,fAlta);
    }

}
