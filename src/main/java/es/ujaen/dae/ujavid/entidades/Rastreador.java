/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import es.ujaen.dae.ujavid.util.CodificadorPassword;
import es.ujaen.dae.ujavid.util.ExprReg;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Clase que representa un Rastreador
 *
 * @author admin
 */
@Entity
public class Rastreador implements Serializable {

    /**
     * Nº total de infectados
     */
    private int numTotalNotificados;

    /**
     * UUID del rastreador
     */
    @Id
    private UUID uuid;

    /**
     * Dni del rastreador
     */
    @Column(unique=true)
    @NotNull
    @Size(min = 9, max = 9)
    @Pattern(regexp = ExprReg.DNI)
    private String dni;

    /**
     * Nombre del rastreador
     */
    @NotBlank
    private String nombre;

    /**
     * Primer apellido del rastreador
     */
    @NotBlank
    private String apellido1;

    /**
     * Segundo apellido del rastreador
     */
    private String apellido2;

    /**
     * Número de teléfono del rastreador
     */
    @Pattern(regexp = ExprReg.TLF)
    private String numTelefono;

    /**
     * Contraseña del rastreador
     */
    private String password;

    public Rastreador() {

    }

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
        this.password = (password != null ? CodificadorPassword.codificar(password) : null);
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
        return CodificadorPassword.igual(password, this.password);
        //return this.password.equals(CodificadorPassword.codificar(password));
    }
}
