/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import es.ujaen.dae.ujavid.util.CodificadorMd5;
import es.ujaen.dae.ujavid.util.ExprReg;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Clase qu representa un Rstreador
 *
 * @author admin
 */
public class Rastreador {

    /**
     * Nº total de infectados
     */
    private int numTotalNotificados;

    /**
     * UUID del rastreador*
     */
    private final UUID uuid;

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
    private String apellido1;

    /**
     * Segundo apellido del rastreador*
     */
    private String apellido2;

    /**
     * Número de teléfono del rastreador*
     */
    @Pattern(regexp = ExprReg.TLF)
    private String numTelefono;

    /**
     * Contraseña del rastreador*
     */
    private String password;

    /**
     * Contructor parametrizado del Rastreador
     *
     * @param dni DNI del rastreador
     * @param nombre Nombre del Rastreador
     * @param apellido1 Primer Apellido del rastreador
     * @param apellido2 Segundo apellido del rastreador
     * @param numTelefono Nº de teléfono del rastreador
     * @param password Contraseña del Rastreador
     */
    public Rastreador(String dni, String nombre, String apellido1, String apellido2, String numTelefono, String password) {
        this.uuid = UUID.randomUUID();
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.numTelefono = numTelefono;
        this.password = CodificadorMd5.codificar(password);
        this.numTotalNotificados = 0;
    }

    /**
     * Método para obtener el UUID del rastreador
     *
     * @return UUID del rastreador
     */
    public UUID getUuid() {
        return uuid;
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
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Método para obtener el segundo apellido del rastreador
     *
     * @return Segundo apellido del rastreador
     */
    public String getApellido2() {
        return apellido2;
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
    public String getPassword() {
        return password;
    }

    public int getNumTotalNotificados() {
        return numTotalNotificados;
    }

    public void aumentarNotificados() {
        numTotalNotificados++;
    }

    /**
     * Compara la password con la del usuario, codificándola en Md5
     *
     * @param password Contraseña a comprobar
     * @return True si las contrasñeas son iguales o False si son distintas
     */
    public boolean passwordValida(String password) {
        return this.password.equals(CodificadorMd5.codificar(password));
    }
}
