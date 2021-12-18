/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.controladoresREST.DTO;

import es.ujaen.dae.ujavid.entidades.Rastreador;
import java.util.UUID;

/**
 * Clase que representa un DTO de Rastreador
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

    /**
     * Constructor por defecto de la clase DTORastreador
     */
    public DTORastreador() {
    }

    /**
     * Constructor para realizar un POST
     *
     * @param dni DNI del Rastrador
     * @param nombre Nombre del Rastreador
     * @param apellido1 Apellido 1 del Rastreador
     * @param apellido2 Apellido 2 del Rastreador
     * @param numTelefono Nº de teléfono de rastreador
     * @param password Contraseña del rastreador(sin codificar)
     */
    public DTORastreador(String dni, String nombre, String apellido1, String apellido2, String numTelefono, String password) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.numTelefono = numTelefono;
        this.password = password;
        this.uuid = null;
        this.numTotalNotificados = 0;
    }

    /**
     * Constructor para realizar un GET
     *
     * @param rastreador Rastreador a convertir
     */
    public DTORastreador(Rastreador rastreador) {
        this.numTotalNotificados = rastreador.getNumTotalNotificados();
        this.uuid = rastreador.getUuid();
        this.dni = rastreador.getDni();
        this.nombre = rastreador.getNombre();
        this.apellido1 = rastreador.getApellido1();
        this.apellido2 = rastreador.getApellido2();
        this.numTelefono = rastreador.getApellido2();
        this.password = rastreador.getPassword();
    }

    /**
     * Constructor para realizar un GET
     *
     * @param numTotalNotificados Nº total de notificados
     * @param uuid UUID del Rastreado
     * @param dni DNI del Rastreador
     * @param nombre Nombre del Rastreador
     * @param apellido1 Apellido 1 del Rastreador
     * @param apellido2 Apellido 2 del Rastreador
     * @param numTelefono Nº de teléfono del Rastreador
     * @param password Contraseña del Rastreador
     */
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

    /**
     * Método para crear un Rastreador a partir de un DTO
     *
     * @return Rastreador creado
     */
    public Rastreador aRastreador() {
        return new Rastreador(dni, nombre, apellido1, apellido2, numTelefono, password);
    }

}
