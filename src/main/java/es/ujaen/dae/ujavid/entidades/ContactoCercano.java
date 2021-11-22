/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PastOrPresent;

/**
 * Clase que representa un Contacto Cercano por parte de un Usuario
 *
 * @author admin
 */
@Entity
public class ContactoCercano implements Comparable<ContactoCercano> {

    /**
     * Identificador único de cada usuario
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int id;
    
    /**
     * Fecha y Hora en ka que se produjo el contacto
     */
    private LocalDateTime fechaContacto;

    /**
     * Usuario con el que se produjo el contacto
     */
    @ManyToOne
    @JoinColumn
    private Usuario contacto;

    /**
     * Distancia al otro dispositivo en metros
     */
    @Min(0)
    @Max(4)
    private double distancia;

    /**
     * Duración del contacto en segundos
     */
    @Min(0)
    private int duracion;

    /**
     * Riesgo de contagio del contacto en función de: 1 - La duración del
     * contacto 2 - La distancia del contacto 3 - Nº de diás transcurridos
     */
    private double riesgo;

    /**
     * Constructor por defecto de la clase ContactoCercano
     */
    public ContactoCercano() {
    }

    
    /**
     * Constructor de la clase ContactoCercano
     *
     * @param fechaContacto Fecha y Hora
     * @param contacto Usuario con el que se produjo el contacto
     * @param distancia Distancia a la que se produce el contacto
     * @param duracion Duración del contacto
     */
    public ContactoCercano(LocalDateTime fechaContacto, Usuario contacto, double distancia, int duracion) {
        this.fechaContacto = fechaContacto;
        this.contacto = contacto;
        this.distancia = distancia;
        this.duracion = duracion;
        this.riesgo = 0;
    }

    /**
     * Método para obtener la fecha y hora en la que se produjo el contacto
     *
     * @return Fecha y Hora del contacto
     */
    public LocalDateTime getFechaContacto() {
        return fechaContacto;
    }

    /**
     * Método para obtener el Usuario con el que se produjo el contacto
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

    /**
     * Método para obtener el riesgo del contacto
     *
     * @return Riesgo del contacto
     */
    public double getRiesgo() {
        return riesgo;
    }

    /**
     * Método para calcular el riesgo del contacto cercano dado un positivo
     *
     * @param fechaPositivo Fecha del positvo del Usuario que tiene registrado
     * este contacto
     */
    public void calcularRiesgo(LocalDate fechaPositivo) {
        // Comprobamos que el usuario es positivo
        if (fechaPositivo != null) {
            // Comprobamos los diás que han transcurrido desde el contacto y la fecha de positivo
            long diasTranscurridos = DAYS.between(fechaPositivo, LocalDate.now());
            // Si han pasado más de 14 días "suponemos que ya no hay riesgo de contagio"
            if (diasTranscurridos > Usuario.DIAS_TRANSCURRIDOS) {
                this.riesgo = 0;
            } else {
                this.riesgo = (this.duracion / this.distancia) * (Usuario.DIAS_TRANSCURRIDOS - diasTranscurridos);
            }
        }
    }

    /**
     * Comparador de objetos ContactoCercano según el riesgo
     *
     * @param o ContactoCercano con el que se quiere comparar
     * @return -1 si el objeto original es menor 1 si el objeto original es
     * mayor 0 si los 2 objetos son iguales
     */
    @Override
    public int compareTo(ContactoCercano o) {
        if (this.riesgo < o.getRiesgo()) {
            return 1;
        }

        if (this.riesgo > o.getRiesgo()) {
            return -1;
        }

        // Los riesgos son iguales
        return 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    /**
     * Comparador de la clase ContactoCercano
     *
     * @param obj Contacto cercano a comparar
     * @return True si los 2 contactos tienen el mismo UUID o False en caso
     * contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContactoCercano other = (ContactoCercano) obj;
        // Comprobamos si tienen el mismo UUID
        if (this.contacto.getUuid().equals(other.contacto.getUuid())) {
            return true;
        }
        return false;
    }

}
