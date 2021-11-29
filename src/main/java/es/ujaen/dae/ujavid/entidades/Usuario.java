/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.entidades;

import es.ujaen.dae.ujavid.util.CodificadorMd5;
import es.ujaen.dae.ujavid.util.ExprReg;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * Clase que representa un Usuario
 *
 * @author admin
 */
@Entity
public class Usuario implements Serializable {

    /**
     * Días máximos que puede estar un contacto cercano en el sistema
     */
    private static final int DIAS_BORRADO = 31;

    /**
     * Nº de diás transcurridos para que un contacto Cercano pueda ser obtenido
     * por un rastreador
     */
    public static final int DIAS_TRANSCURRIDOS = 14;

    /**
     * UUID del usuario*
     */
    @Id
    private UUID uuid;

    /**
     * Número de teléfono
     */
    @Pattern(regexp = ExprReg.TLF)
    private String numTelefono;

    /**
     * Fecha de curación
     */
    private LocalDate fCuracion;

    /**
     * Fecha de positivo
     */
    private LocalDateTime fPositivo;

    /**
     * ¿Es Positivo?
     */
    boolean positivo;

    /**
     * Cotraseña del usuario
     */
    private String password;

    /**
     * Fecha de alta/registro
     */
    @PastOrPresent
    private LocalDate fAlta;

    /**
     * Listado de Contactos Cercanos
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn
    private List<ContactoCercano> listadoContactos;

    /**
     * Constructor por defecto de la clase Usuario
     */
    public Usuario() {
    }

    /**
     * Constructor parametrizado de la clase Usuario
     *
     * @param numTelefono Nº de teléfono
     * @param password Contraseña del usuario
     */
    public Usuario(String numTelefono, String password) {
        this.uuid = UUID.randomUUID();
        this.password = CodificadorMd5.codificar(password);
        this.numTelefono = numTelefono;
        this.fCuracion = null;
        this.fPositivo = null;
        this.positivo = false;
        this.fAlta = LocalDate.now();
        this.listadoContactos = new ArrayList<>();
    }

    public Usuario(UUID uuid, String numTelefono, LocalDate fCuracion, LocalDateTime fPositivo, boolean positivo, String password, LocalDate fAlta) {
        this.uuid = uuid;
        this.numTelefono = numTelefono;
        this.fCuracion = fCuracion;
        this.fPositivo = fPositivo;
        this.positivo = positivo;
        this.password = password;
        this.fAlta = fAlta;
        
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
     * @return Fecha de curación del usuario o null si nunca se ha contagiado
     */
    public LocalDate getfCuracion() {
        return fCuracion;
    }

    /**
     * Método para modificar la fecha de curación del usuario
     *
     * @param fCuracion Nueva Fecha en la que se ha curado el usuario
     */
    public void setfCuracion(LocalDate fCuracion) {
        this.fCuracion = fCuracion;
    }

    /**
     * Método para obtener la fecha en la que se registro el usuario
     *
     * @return Fecha de Alta del usuario en el sistema
     */
    public LocalDate getfAlta() {
        return fAlta;
    }

    /**
     * Método para obtener la fecha del último positivo del Usuario
     *
     * @return Fecha del último positivo del usuario o null si nunca ha dado
     * positivo
     */
    public LocalDateTime getfPositivo() {
        return fPositivo;
    }

    /**
     * Método para modificar la fecha del último positivo del Usuario
     *
     * @param fPositivo Fecha del último positivo
     */
    public void setfPositivo(LocalDateTime fPositivo) {
        this.fPositivo = fPositivo;
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
    public void addContactoCercano(@NotNull @Valid ContactoCercano contacto) {
        // Comprobamos si está el contacto guardado en la lista
        if (this.listadoContactos.contains(contacto)) {
            this.listadoContactos.remove(contacto);
        }
        // Añadimos el contacto
        this.listadoContactos.add(contacto);

    }

    /**
     * Devolver contactos cercanos del usuario
     *
     * @return Lista de contactos cercanos
     */
    public List<ContactoCercano> verContactosCercanos() {
        // Ordenamos la lista de contactos
        Collections.sort(this.listadoContactos);
        // Creamos una nueva lista de contactos en las que solo aparezcan los contactos de las 2 últimas semanas
        List<ContactoCercano> listaDefinitiva = new ArrayList<>();
        // Obtenemos la fecha de hace 2 semanas
        LocalDate fecha2Semanas = LocalDate.now().minusDays(DIAS_TRANSCURRIDOS);

        // Recorremos la lista
        for (ContactoCercano contacto : this.listadoContactos) {
            if (contacto.getFechaContacto().toLocalDate().isAfter(fecha2Semanas)) {
                // Añadimos el contacto a la lista
                listaDefinitiva.add(contacto);
            }
        }
        return Collections.unmodifiableList(listaDefinitiva);
    }

    /**
     * Método para calcular los riesgos de los contactos del Usuario
     */
    public void calcularRiesgoContactos() {
        // Calculamos todos los riesgos de los contactos del Usuario
        for (ContactoCercano contacto : this.listadoContactos) {
            contacto.calcularRiesgo(this.fPositivo.toLocalDate());
        }
    }

    /**
     * Método para borrar contactos cercanos al usuario pasados 31 diás
     */
    public void limpiarContactosCercanos() {
        // Obtenemos la fecha de hace 1 mes(suponemos un mes equivale a 31 diás)
        LocalDate fechaTope = LocalDate.now().minusDays(DIAS_BORRADO);
        // Recorremos todos los contactos cercanos y eliminamos los que tengan más de 1 mes
        for (ContactoCercano contacto : this.listadoContactos) {
            // Comprobamos las fechas
            if (contacto.getFechaContacto().toLocalDate().isBefore(fechaTope)) {
                // Eliminamos el contacto
                this.listadoContactos.remove(contacto);
            }
        }
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
