/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.controladoresREST.DTO;

import java.util.UUID;

/**
 *
 * @author admin
 */
public class DTORastreador {
      /**
     * Nº total de infectados
     */
    private int numTotalNotificados;

    /**
     * UUID del rastreador
     */
    private UUID uuid;
    /**
     * Dni del rastreador
     */
  
    private String dni;

    /**
     * Nombre del rastreador
     */

    private String nombre;

    /**
     * Primer apellido del rastreador
     */
    
    private String apellido1;

    /**
     * Segundo apellido del rastreador
     */
    private String apellido2;

    /**
     * Número de teléfono del rastreador
     */
    
    private String numTelefono;

    /**
     * Contraseña del rastreador
     */
    private String password;

    public int getNumTotalNotificados() {
        return numTotalNotificados;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public String getPassword() {
        return password;
    }

    public DTORastreador(int numTotalNotificados, UUID uuid, String dni, String nombre, String apellido1, String apellido2, String numTelefono, String password) {
        this.numTotalNotificados = numTotalNotificados;
        this.uuid = uuid;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.numTelefono = numTelefono;
        this.password = password;
    }
    
}
