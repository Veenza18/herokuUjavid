/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

/**
 *
 * @author admin
 */
public class Rastreador {

    /**
     * Dni del rastreador*
     */
    String dni;
    
    /**
     * Nombre del rastreador*
     */
    String nombre;
    
    /**
     * Primer apellido del rastreador*
     */
    String apellido_1;
    
    /**
     * Segundo apellido del rastreador*
     */
    String apellido_2;
    
    /**
     * Número de teléfono del rastreador*
     */
    String numTelefono;
    
    /**
     * Contraseña del rastreador*
     */
    String contraseña;

    public Rastreador(String dni, String nombre, String apellido_1, String apellido_2, String numTelefono, String contraseña) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido_1 = apellido_1;
        this.apellido_2 = apellido_2;
        this.numTelefono = numTelefono;
        this.contraseña = contraseña;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido_1() {
        return apellido_1;
    }

    public String getApellido_2() {
        return apellido_2;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public String getContraseña() {
        return contraseña;
    }

}
