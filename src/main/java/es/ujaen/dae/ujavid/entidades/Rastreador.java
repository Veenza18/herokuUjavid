/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import es.ujaen.dae.ujavid.util.CodificadorMd5;
import es.ujaen.dae.ujavid.util.ExprReg;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Clase qu representa un Rstreador
 *
 * @author admin
 */
public class Rastreador {

    /**
     * Dni del rastreador*
     */
    @Pattern(regexp = ExprReg.DNI)
    private String dni;

    /**
     * Nombre del rastreador*
     */
    @NotBlank
    private String nombre;

    /**
     * Primer apellido del rastreador*
     */
    @NotBlank
    private String apellido_1;

    /**
     * Segundo apellido del rastreador*
     */
    private String apellido_2;

    /**
     * Número de teléfono del rastreador*
     */
    @Pattern(regexp = ExprReg.TLF)
    private String numTelefono;

    /**
     * Contraseña del rastreador*
     */
    private String contraseña;

    /**
     * Contructor parametrizado del Rastreador
     *
     * @param dni DNI del rastreador
     * @param nombre Nombre del Rastreador
     * @param apellido_1 Primer Apellido del rastreador
     * @param apellido_2 Segundo apellido del rastreador
     * @param numTelefono Nº de teléfono del rastreador
     * @param contraseña Contraseña del Rastreador
     */
    public Rastreador(String dni, String nombre, String apellido_1, String apellido_2, String numTelefono, String contraseña) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido_1 = apellido_1;
        this.apellido_2 = apellido_2;
        this.numTelefono = numTelefono;
        this.contraseña = contraseña;
    }

    /**
     * Método para obtener el DNI del rastreador
     *
     * @return DNI del rastreador
     */
    public String getDni() {
        return dni;
    }

    /**
     * Método para obtener el nombre del rastreador
     *
     * @return Nombre del rastreador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para obtener el primer apeliido del rastreador
     *
     * @return Primer apellido del rastreador
     */
    public String getApellido_1() {
        return apellido_1;
    }

    /**
     * Método para obtener el segundo apellido del rastreador
     *
     * @return Segundo apellido del rastreador
     */
    public String getApellido_2() {
        return apellido_2;
    }

    /**
     * Método para obtener el nº de teléfono del rastreador
     *
     * @return Nº de teléfono del rastreador
     */
    public String getNumTelefono() {
        return numTelefono;
    }

    /**
     * Método para obtener la contraseño del rastreador
     *
     * @return Contraseña del rastreador
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Compara la contraseña con la del usuario, codificándola en Md5
     *
     * @param password Contraseña a comprobar
     * @return True si las contrasñeas son iguales o False si son distintas
     */
    public boolean passwordValida(String password) {
        return this.contraseña.equals(CodificadorMd5.codificar(password));
    }
}
