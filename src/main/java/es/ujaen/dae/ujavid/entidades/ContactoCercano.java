/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import java.time.LocalDateTime;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

/**
 * Clase que representa un Contacto Cercano por parte de un Usuario
 *
 * @author admin
 */
public class ContactoCercano {

    /**
     * Fecha y Hora en ka que se produjo el contacto
     */
    @PastOrPresent
    private final LocalDateTime fecha_contacto;

    /**
     * Usuario con el que re produjo el contacto
     */
    private final Usuario contacto;

    /**
     * Distancia al otro dispositivo en metros
     */
    @Min(0)
    @Max(4)
    private final double distancia;

    /**
     * Duración del contacto en segundos
     */
    @Min(0)
    private final int duracion;

    /**
     * Constructor de la clase ContactoCercano
     *
     * @param fecha_contacto Fecha y Hora
     * @param contacto Usuario con el que se produjo el contacto
     * @param distancia Distancia a la que se produce el contacto
     * @param duracion Duración del contacto
     */
    public ContactoCercano(LocalDateTime fecha_contacto, Usuario contacto, double distancia, int duracion) {
        this.fecha_contacto = fecha_contacto;
        this.contacto = contacto;
        this.distancia = distancia;
        this.duracion = duracion;
    }

    /**
     * Método para obtener la fecha y hora en la que se produjo el contacto
     *
     * @return Fecha y Hora del contacto
     */
    public LocalDateTime getFecha_contacto() {
        return fecha_contacto;
    }

    /**
     * Método para obtener del Usuario con el que se produjo el contacto
     *
     * @return Usuario con el que se produjo el contacto
     */
    public Usuario getContacto() {
        return contacto;
    }

    /**
     * Método para obtener la distancia del contacto
     *
     * @return Distancia del contacto
     */
    public double getDistancia() {
        return distancia;
    }

    /**
     * Método para obtener la duración del contacto
     *
     * @return Duración del contacto
     */
    public int getDuracion() {
        return duracion;
    }

}
