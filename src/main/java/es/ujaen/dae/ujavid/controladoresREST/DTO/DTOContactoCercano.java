/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.controladoresREST.DTO;

import es.ujaen.dae.ujavid.entidades.Usuario;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author Venza
 */
public class DTOContactoCercano{
    private UUID uuid1;
    private UUID uuid2;
    private LocalDateTime fechaContacto;
    private double distancia;
    private int duracion;
    private double riesgo;

    public DTOContactoCercano(LocalDateTime fechaContacto, UUID uuidUsuario, double distancia, int duracion) {
        this.fechaContacto = fechaContacto;
        this.uuid2 = uuidUsuario;
        this.distancia = distancia;
        this.duracion = duracion;
        this.riesgo = 0;
    }
    public UUID getUuid1() {
        return uuid1;
    }

    public void setUuid1(UUID uuid1) {
        this.uuid1 = uuid1;
    }

    public UUID getUuid2() {
        return uuid2;
    }

    public void setUuid2(UUID uuid2) {
        this.uuid2 = uuid2;
    }

    public LocalDateTime getFechaContacto() {
        return fechaContacto;
    }

    public void setFechaContacto(LocalDateTime fechaContacto) {
        this.fechaContacto = fechaContacto;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public double getRiesgo() {
        return riesgo;
    }

    public void setRiesgo(double riesgo) {
        this.riesgo = riesgo;
    }
    
    
}
