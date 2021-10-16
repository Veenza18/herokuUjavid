/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import es.ujaen.dae.ujavid.util.CodificadorMd5;
import es.ujaen.dae.ujavid.util.ExprReg;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

/**
 * Clase que representa un Usuario
 *
 * @author admin
 */
public class Usuario {

    /**
     * UUID del usuario*
     */
    private final UUID uuid;

    /**
     * Número de teléfono*
     */
    @Pattern(regexp = ExprReg.TLF)
    private String numTelefono;

    /**
     * Fecha de curación*
     */
    @PastOrPresent
    private LocalDate f_curacion;

    /**
     * Fecha de positivo *
     */
    @PastOrPresent
    private LocalDateTime f_positivo;

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
    private List<ContactoCercano> listadoContactos;

    /**
     * Constructor parametrizado de al clase Usuario
     *
     * @param numTelefono Nº de teléfono
     * @param password Contraseña del usuario
     * @param f_alta Fecha en la que se registra el usuario
     */
    public Usuario(String numTelefono, String password, LocalDate f_alta) {
        this.uuid = UUID.randomUUID();
        this.password = CodificadorMd5.codificar(password);
        this.numTelefono = numTelefono;
        this.f_curacion = f_alta;
        this.f_positivo = null;
        this.positivo = false;
        this.listadoContactos = new ArrayList<>();
    }

    

    /**
     * Método para obtener el UUID del usuario
     *
     * @return Uuid del usuario
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Método para obtener la contraseñ del Usuario
     *
     * @return Contraseña del Usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Método para obtener el Nº de teléfono del Usuario
     *
     * @return Nº de teléfono del Usuario
     */
    public String getNumTelefono() {
        return numTelefono;
    }

    /**
     * Método para obtener la fecha en la que se registro el Usuario
     *
     * @return Fecha de registro del Usuario en el Sistema
     */
    public LocalDate getF_curacion() {
        return f_curacion;
    }

    /**
     * Método para obtener la fecha del último positivo del Usuario
     *
     * @return Fecha del último positivo del usuario
     */
    public LocalDateTime getF_positivo() {
        return f_positivo;
    }

    /**
     * Método para modificar la fecha del último positivo del Usuario
     *
     * @param f_positivo Fecha del último positivo
     */
    public void setF_positivo(LocalDateTime f_positivo) {
        this.f_positivo = f_positivo;
    }

    /**
     * Método para comprobar si el Usuario es positivo
     *
     * @return True si es positivo, False si no es positivo
     */
    public boolean isPositivo() {
        return positivo;
    }

    /**
     * Método para modificar el estado del Usuario
     *
     * @param positivo Estado del usuario
     */
    public void setPositivo(boolean positivo) {
        this.positivo = positivo;
    }

    /**
     * Método para obtener el listado de contactos cercanos del Usuario
     *
     * @return Contactos cercanos del Usuario
     */
    public List<ContactoCercano> getListadoContactos() {
        return listadoContactos;
    }

    /**
     * Método para añadir un Contacto al usuario
     * 
     * @param contacto Contacto cercano al usuario
     */
    public void addContactoCercano(ContactoCercano contacto){
        this.listadoContactos.add(contacto);
    }
    
    /**
     * Devolver contactos cercanos del usuario
     *
     * @return Lista de contactos cercanos
     */
    public List<ContactoCercano> verContactosCercanos() {
        return Collections.unmodifiableList(this.listadoContactos);
    }

    /**
     * Compara la contraseña con la del usuario, codificándola en Md5
     *
     * @param password Contraseña a comprobar
     * @return True si las contrasñeas son iguales o False si son distintas
     */
    public boolean passwordValida(String password) {
        return this.password.equals(CodificadorMd5.codificar(password));
    }
}
