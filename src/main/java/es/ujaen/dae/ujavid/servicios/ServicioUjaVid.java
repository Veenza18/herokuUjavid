/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.servicios;

import es.ujaen.dae.ujavid.entidades.ContactoCercano;
import es.ujaen.dae.ujavid.entidades.Rastreador;
import es.ujaen.dae.ujavid.entidades.Usuario;
import es.ujaen.dae.ujavid.excepciones.UsuarioYaRegistrado;
import es.ujaen.dae.ujavid.excepciones.UsuarioNoRegistrado;
import es.ujaen.dae.ujavid.excepciones.RastreadorYaRegistrado;
import es.ujaen.dae.ujavid.excepciones.RastreadorNoRegistrado;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Clase con los servicios utilizados por los usuarios y rastreadores de la
 * aplicación
 *
 * @author admin
 */
@Service
@Validated
public class ServicioUjaVid {

    /**
     * Nº total de infectados
     */
    private int NUM_TOTAL_INF = 0;

    /**
     * Mapa con la lista de rastreadores
     */
    private Map<String, Rastreador> rastreadores;
    /**
     * Mapa con la lista de usuarios
     */

    private Map<UUID, Usuario> usuarios;

    /**
     * Constructor de la clase ServicioUjavid
     */
    public ServicioUjaVid() {
        rastreadores = new TreeMap<>();
        usuarios = new TreeMap<>();
    }

    /**
     * Dar de alta usuario y crear una cuenta asociada
     *
     * @param usuario el usuario a dar de alta
     * @return la cuenta asociada al usuario
     */
    public Usuario altaUsuario(@NotNull @Valid Usuario usuario) {
        if (usuarios.containsKey(usuario.getUuid())) {
            throw new UsuarioYaRegistrado();
        }
        // Registrar Usuario
        usuarios.put(usuario.getUuid(), usuario);
        return usuario;
    }

    /**
     * Realiza un login de un cliente
     *
     * @param numTelefono Nº de teléfono del usuario
     * @param clave la clave de acceso
     * @return el objeto de la clase Usuario asociado
     * @todo Cambiar la clave que se le pasa(Iterar)
     */
    public Optional<Usuario> loginUsuario(String numTelefono, String clave) {
        Iterator<Usuario> it = this.usuarios.values().iterator();
        Usuario usuario_aux = null;
        boolean encontrado = false;
        while (it.hasNext() && !encontrado) {
            usuario_aux = it.next();
            if (usuario_aux.getNumTelefono().equals(numTelefono)) {
                encontrado = true;
            }
        }
        return Optional.ofNullable(usuarios.get(usuario_aux.getUuid())).filter((usuario) -> usuario.passwordValida(clave));
    }

    /**
     * Dar de alta un Rstreador en el sistema
     *
     * @param rastreador el rastredor a dar de alta
     * @return El rastreador registrado en el sistema
     */
    public Rastreador altaRastreador(@NotNull @Valid Rastreador rastreador) {
        if (rastreadores.containsKey(rastreador.getDni())) {
            throw new RastreadorYaRegistrado();
        }
        // Registrar Rastreador
        rastreadores.put(rastreador.getDni(), rastreador);
        return rastreador;
    }

    /**
     * Realiza un login de un cliente
     *
     * @param dni el DNI del rastreador
     * @param clave la clave de acceso
     * @return el objeto de la clase Rastreador asociado
     */
    public UUID loginRastreador(String dni, String clave) {
        Optional<Rastreador> op =  Optional.ofNullable(rastreadores.get(dni)).filter((rastreador) -> rastreador.passwordValida(clave));
        return op.get().getUuid();
    }

    /**
     * Método para añadir un Contacto cercano a un usuario
     *
     * @param contactos Contactos que se van a añadir a la lista
     * @param uuidUsuario UUID del usuario al que se le añadirá el contacto
     */
    public void addContactoCercano(List<ContactoCercano> contactos, UUID uuidUsuario) {
        Usuario usuario = Optional.ofNullable(usuarios.get(uuidUsuario)).orElseThrow(UsuarioNoRegistrado::new);
        for (ContactoCercano contacto : contactos) {
            if(!usuario.getUuid().equals(contacto.getContacto().getUuid())){
                usuario.addContactoCercano(contacto);
            }
        }

    }

    /**
     * Método para ver los contactos cercanos de un Usuario
     *
     * @param uuid UUID del usuario
     * @return Lista de contactos cercanos al usuario
     */
    public List<ContactoCercano> verContactosCercanos(UUID uuid) {
        Usuario usuario = Optional.ofNullable(usuarios.get(uuid)).orElseThrow(UsuarioNoRegistrado::new);
        return usuario.verContactosCercanos();
    }

    /**
     * Método para notificar el positivo a un Usuario
     *
     * @param uuid UUID del usuario a notificar el positivo
     * @param f_positivo Fecha y hora del positivo
     * @param dniRastreador DNI del rastreador que notifica el positivo
     */
    public void notificarPos(UUID uuid, @PastOrPresent LocalDateTime f_positivo, String dniRastreador,String contraseña ) {
        
        // Obtenemos el usuario
        Usuario usuario = Optional.ofNullable(usuarios.get(uuid)).orElseThrow(UsuarioNoRegistrado::new);
        // Obtnemos el Rstreador
        Rastreador rastreador = Optional.ofNullable(this.rastreadores.get(dniRastreador)).orElseThrow(RastreadorNoRegistrado::new);
       if(rastreador.getUuid().equals(this.loginRastreador(dniRastreador, contraseña))){
           
            // Realizamos las operaciones
            rastreador.aumentarNotificados();
            usuario.setPositivo(true);
            usuario.setF_positivo(f_positivo);
            usuario.calcularRiesgoContactos();
            NUM_TOTAL_INF++;
       }
        
    }

    /**
     * Método para notificar la curación de un positivo
     *
     * @param uuid UUID del usuario a notificar la curación
     */
    public void notificarCuracion(UUID uuid) {
        Usuario usuario = Optional.ofNullable(usuarios.get(uuid)).orElseThrow(UsuarioNoRegistrado::new);
        usuario.setPositivo(false);
        usuario.setF_curacion(LocalDate.now());
    }

    /**
     * Método para obtener el Nº total de infectados
     *
     * @return Nº total de infectados
     */
    public int totalInfectados() {
        return NUM_TOTAL_INF;
    }

    /**
     * Método para obtener el Nº total de positivos que hay actualmente
     *
     * @return Nº de positivos actualmente
     */
    public int positivos_actual() {
        int positivos = 0;

        Iterator<Usuario> it = usuarios.values().iterator();
        while (it.hasNext()) {
            if (it.next().isPositivo()) {
                positivos++;
            }
        }
        return positivos;
    }

    /**
     * Método para obtener el nº de positivos los últimos 15 días
     *
     * @return Nº de positivos
     */
    public int positivos15Dias() {
        int positivos = 0;
        Iterator<Usuario> it = usuarios.values().iterator();

        LocalDateTime fecha15dias = LocalDateTime.now().minusDays(15);
        while (it.hasNext()) {
            Usuario usuario = it.next();

            // Probar testing
            if (usuario.isPositivo() && usuario.getF_positivo().isAfter(fecha15dias)) {
                positivos++;
            }
        }

        return positivos;
    }

    /**
     * Método para obtener el nº personas que ha contagiado un usuario
     *
     * @param uuidUsuario UUID del usuario a comprobar
     * @return El número de contagiados que ha (generado) el usuario
     */
    private int contagiadosUsuario(UUID uuidUsuario) {
        int contagiados = 0;
        // Obtenemos al usuario
        Usuario usuario = Optional.ofNullable(usuarios.get(uuidUsuario)).orElseThrow(UsuarioNoRegistrado::new);
        // Obtenemos sus contactos cercanos
        List<ContactoCercano> contactos = usuario.verContactosCercanos();
        // Obtenemos la fecha del positivo del usuario y la pasamos a LocalDate
        LocalDate fechaPositivo = usuario.getF_positivo().minusDays(15).toLocalDate();
        // Obtenemos la fecha de curacion
        LocalDate fechaCuracion = usuario.getF_curacion();
        if(fechaCuracion==null){fechaCuracion=LocalDate.now();}
        // Recorremos todos los contactos del usuario
        for (int i = 0; i < contactos.size(); i++) {
            // Comprobamos si el contacto es positivo
            if (contactos.get(i).getContacto().isPositivo()) {
                // Obtenemos la fecha de contacto y la pasamos  LocalDate
                LocalDate fechaPositivoContacto = contactos.get(i).getFecha_contacto().toLocalDate();
                if (fechaPositivoContacto.isAfter(fechaPositivo) && fechaPositivoContacto.isBefore(fechaCuracion)) {
                    contagiados++;
                }
            }

        }

        return contagiados;

    }

    /**
     * Método para obtener uns estadística de contagiados / usuarios positivos
     *
     * @return La estadistica de contagiados por usuarios positivos
     */
    public double contagiadosXusuario() {
        double n_positivos_total = 0;
        double contagiados_total = 0;

        // Recorremos todos los usuarios 
        Iterator<Usuario> it = usuarios.values().iterator();

        while (it.hasNext()) {
            Usuario usuario = it.next();
            // Comprobamos que usuarios son positivos
            if (usuario.getF_positivo()!=null) {              
                n_positivos_total++;
                // Calculamos los contagiados producidos por el usuario
                contagiados_total += this.contagiadosUsuario(usuario.getUuid());
            }
        }

        System.out.println("TIMEEEEEEEEEEEEEe"+n_positivos_total+"S"+contagiados_total);
        // Comprobamos que hay almenos una persona que es positivo
        if (n_positivos_total > 0) {
            return contagiados_total / n_positivos_total;
        }
        return 0;
    }

    /**
     * Método pata obtener los positivos notificados por un rastreador
     *
     * @param dniRastreador DNI del rastreador
     * @return El nº de positivos notificados por el rastreador
     */
    public int positivosRastreador(String dniRastreador) {
        Rastreador rastreador = Optional.ofNullable(this.rastreadores.get(dniRastreador)).orElseThrow(RastreadorNoRegistrado::new);
        return rastreador.getNUM_TOTAL_NOTIFICADOS();
    }
}
