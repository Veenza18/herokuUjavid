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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.format.datetime.joda.LocalDateTimeParser;
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
    private static int NUM_TOTAL_INF = 0;
    
    /**
     * Mapa con la lista de rastreadores
     */
    
    private Map<String, Rastreador> rastreadores;
    /**
     * Mapa con la lista de usuarios
     */
    
    private Map<String, Usuario> usuarios;

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
        if (usuarios.containsKey(usuario.getNumTelefono())) {
            throw new UsuarioYaRegistrado();
        }
        // Registrar Usuario
        usuarios.put(usuario.getNumTelefono(), usuario);
        return usuario;
    }

    /**
     * Realiza un login de un cliente
     *
     * @param numTelefono Nº de teléfono del usuario
     * @param clave la clave de acceso
     * @return el objeto de la clase Usuario asociado
     */
    public Optional<Usuario> loginUsuario(String numTelefono, String clave) {
        return Optional.ofNullable(usuarios.get(numTelefono)).filter((usuario) -> usuario.passwordValida(clave));
    }

    /**
     * Dar de alta un Rstreador en el sistema
     *
     * @param rastreador el rastredor a dar de alta
     * @return El rastreador registrado en el sistema
     */
    public Rastreador altaRastreador(@NotNull @Valid Rastreador rastreador) {
        if (usuarios.containsKey(rastreador.getNumTelefono())) {
            throw new RastreadorYaRegistrado();
        }
        // Registrar Rastreador
        rastreadores.put(rastreador.getNumTelefono(), rastreador);
        return rastreador;
    }

    /**
     * Realiza un login de un cliente
     *
     * @param dni el DNI del rastreador
     * @param clave la clave de acceso
     * @return el objeto de la clase Rastreador asociado
     */
    public Optional<Rastreador> loginRastreador(String dni, String clave) {
        return Optional.ofNullable(rastreadores.get(dni)).filter((rastreador) -> rastreador.passwordValida(clave));
    }

    /**
     * Devolver los contactos cercanos de un usuario dado
     *
     * @param uuid Id del usuario a buscar
     * @return Lista de Contactos Cercanos al Usuario
     */
    public List<ContactoCercano> verContactosCercanos(UUID uuid) {
        Iterator<Usuario> it = usuarios.values().iterator();
        Usuario usuario_aux = null;
        boolean encontrado = false;
        while (it.hasNext() || encontrado) {
            usuario_aux = it.next();
            if (usuario_aux.getUuid().equals(uuid)) {
                encontrado = true;
            }
        }
        Usuario usuario = Optional.ofNullable(usuarios.get(usuario_aux.getNumTelefono())).orElseThrow(UsuarioNoRegistrado::new);
        return usuario.verContactosCercanos();
    }
    
    /**
     * Método para notificar el positivo a un Usuario
     * 
     * @param uuid
     * @param f_positivo Fecha y hora del positivo
     * @param dniRastreador
     */
    public void notificarPos(UUID uuid, LocalDateTime f_positivo, String dniRastreador) {
        Iterator<Usuario> it = usuarios.values().iterator();
        Usuario usuario_aux = null;
        boolean encontrado = false;
        while (it.hasNext() || encontrado) {
            usuario_aux = it.next();
            if (usuario_aux.getUuid().equals(uuid)) {
                encontrado = true;
            }
        }
        Usuario usuario = Optional.ofNullable(usuarios.get(usuario_aux.getNumTelefono())).orElseThrow(UsuarioNoRegistrado::new);
        Rastreador rastreador = this.rastreadores.get(dniRastreador);
        rastreador.aumentarNotificados();
        usuario.setPositivo(true);
        usuario.setF_positivo(f_positivo);
        NUM_TOTAL_INF++;
    }

    /**
     * Método para notificar la curación de un positivo
     * 
     * @param uuid
     */
    public void notificarCuracion(UUID uuid) {
        Iterator<Usuario> it = usuarios.values().iterator();
        Usuario usuario_aux = null;
        boolean encontrado = false;
        while (it.hasNext() || encontrado) {
            usuario_aux = it.next();
            if (usuario_aux.getUuid().equals(uuid)) {
                encontrado = true;
            }
        }
        Usuario usuario = Optional.ofNullable(usuarios.get(usuario_aux.getNumTelefono())).orElseThrow(UsuarioNoRegistrado::new);
        usuario.setPositivo(false);
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
    public int positivos15Dias(){
        int positivos = 0;
        Iterator<Usuario> it = usuarios.values().iterator();

        LocalDateTime fecha15dias = LocalDateTime.now().minusDays(15);
        while (it.hasNext()) {
            Usuario usuario = it.next();
            
            // Probar testing
            if (usuario.isPositivo() && usuario.getF_positivo().isAfter(fecha15dias) ) {
                positivos++;
            }
        }
        
        return positivos;
    }
    
    /**
     * FUNCIÓN NO RAYARSER
     * @param uuidUsuario
     * @return 
     */
//    private int contagiadosUsuario(UUID uuidUsuario){
//        int contagiados = 0;
//        // Obtenemos al usuario
//        Usuario usuario = Optional.ofNullable(usuarios.get(uuidUsuario)).orElseThrow(UsuarioNoRegistrado::new);
//        // Obtenemos sus contactos cercanos
//        List<ContactoCercano> contactos = usuario.verContactosCercanos();
//        // Obtenemos la fecha del positivo del usuario
//        LocalDateTime fechaPositivo = usuario.getF_positivo().minusDays(15);
//        // Obtenemos la fecha de curacion
//        LocalDate fechaCuracion = usuario.getF_alta();
////        LocalDate aux = new LocalDate(fechaPositivo.get)
////        fechaPositivo.
//                
//        
//        for (int i = 0; i < contactos.size(); i++) {
//            // Comprobamos si el contacto es positivo
//            if(contactos.get(i).getContacto().isPositivo()){
//                if(contactos.get(i).getFecha_contacto().isAfter(fechaPositivo) && contactos.get(i).getFecha_contacto().isBefore(fechaCuracion)){
//                    
//                }
//            }
//            
//        }
//        
//        return contagiados;
//        
//    }
    
    public int positivosRastreador(String dniRastreador){
        Rastreador rastreador = Optional.ofNullable(this.rastreadores.get(dniRastreador)).orElseThrow(RastreadorNoRegistrado::new);;
        return rastreador.getNUM_TOTAL_NOTIFICADOS();
    }
}
