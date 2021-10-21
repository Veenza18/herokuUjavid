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
     * Días máximos que puede estar un contacto cercano en el sistema
     */
    private static final int DIAS_BORRADO = 31;

    /**
     * Nº de diás transucrridos para que un contacto Cercano pueda ser obtenido
     * por un rastreador
     */
    public static final int DIAS_TRANSCURRIDOS = 14;

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
     * Fecha de alta/registro*
     */
    @PastOrPresent
    private final LocalDate f_alta;

    /**
     * Listado de Contactos Cercanos*
     */
    private List<ContactoCercano> listadoContactos;

    /**
     * Constructor parametrizado de al clase Usuario
     *
     * @param numTelefono Nº de teléfono
     * @param password Contraseña del usuario
     */
    public Usuario(String numTelefono, String password) {
        this.uuid = UUID.randomUUID();
        this.password = CodificadorMd5.codificar(password);
        this.numTelefono = numTelefono;
        this.f_curacion = null;
        this.f_positivo = null;
        this.positivo = false;
        this.f_alta = LocalDate.now();
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
     * @return Fecha de curación del usuario o null si nunca se ha contagiado
     */
    public LocalDate getF_curacion() {
        return f_curacion;
    }

    /**
     * Método para modificar la fecha de curación del usuario
     *
     * @param f_curacion Nueva Fecha en la que se ha curado el usuario
     */
    public void setF_curacion(LocalDate f_curacion) {
        this.f_curacion = f_curacion;
    }

    /**
     * Método para obtener el UUID del usuario
     *
     * @return Uuid del usuario
     */
    public LocalDate getF_alta() {
        return f_alta;
    }

    /**
     * Método para obtener la fecha del último positivo del Usuario
     *
     * @return Fecha del último positivo del usuario o null si nunca ha dado
     * positivo
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
     * @todo Hay que mirar como ordenamos la lista para no iterar ya que puede
     * ser muy lento y costoso
     */
    public void addContactoCercano(ContactoCercano contacto) {
//        Iterator<ContactoCercano> it = listadoContactos.iterator();
//        boolean encontrado = false;
//        // Recorremos la lista de contactos cercanos
//        while (it.hasNext() && !encontrado) {
//            ContactoCercano con_aux = it.next();
//            // Comprobamos si ya está el contacto cercano en la lista
//            if (con_aux.getContacto().equals(contacto.getContacto())) {
//                listadoContactos.remove(con_aux);
//                encontrado = true;
//            }
//        }

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
            if (contacto.getFecha_contacto().toLocalDate().isAfter(fecha2Semanas)) {
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
            contacto.calcularRiesgo(this.f_positivo.toLocalDate());
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
            if (contacto.getFecha_contacto().toLocalDate().isBefore(fechaTope)) {
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
