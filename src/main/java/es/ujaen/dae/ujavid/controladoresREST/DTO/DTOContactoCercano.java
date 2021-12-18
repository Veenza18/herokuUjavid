/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.controladoresREST.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase que representa un DTO de Contacto Cercano
 *
 * @author Venza
 */
public class DTOContactoCercano {

    /**
     * UUID del Usuario contacto
     */
    private UUID uuid;

    /**
     * Fecha en la que se produjo el contacto
     */
    private LocalDateTime fechaContacto;

    /**
     * Distancia del contacto
     */
    private double distancia;

    /**
     * Duración del contacto
     */
    private int duracion;

    /**
     * Riesgo del contacto
     */
    private double riesgo;

    /**
     * Constructor por defecto de la clase DTOContactoCercano
     */
    public DTOContactoCercano() {
    }

    /**
     * Constructor parametrizado de la clase DTOContactoCercano
     *
     * @param fechaContacto Fecha en la que se produce el contacto
     * @param uuid UUID del usuario 1
     * @param distancia Distancia en la que se produjo el contacto
     * @param duracion Duración del contacto
     */
    public DTOContactoCercano(LocalDateTime fechaContacto, UUID uuid, double distancia, int duracion) {
        this.fechaContacto = fechaContacto;
        this.uuid = uuid;
        this.distancia = distancia;
        this.duracion = duracion;
        this.riesgo = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LocalDateTime getFechaContacto() {
        return fechaContacto;
    }

    public double getDistancia() {
        return distancia;
    }

    public int getDuracion() {
        return duracion;
    }

    public double getRiesgo() {
        return riesgo;
    }

}
