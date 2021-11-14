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
import es.ujaen.dae.ujavid.repositorios.RepositorioRastreadores;
import es.ujaen.dae.ujavid.repositorios.RepositorioUsuarios;
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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RepositorioUsuarios repositorioUsuarios;

    @Autowired
    RepositorioRastreadores repositorioRastreadores;

    /**
     * Nº total de infectados
     */
    private int numTotalInf = 0;

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
    }

    /**
     * Dar de alta usuario
     *
     * @param usuario el usuario a dar de alta
     * @return El Usuario creado
     * @throws UsuarioYaRegistrado en caso de que esté el Usuario registrado
     */
    public UUID altaUsuario(@NotNull @Valid Usuario usuario) {

        if (repositorioUsuarios.buscar(usuario.getUuid()).isPresent()) {
            throw new UsuarioYaRegistrado();
        }

        repositorioUsuarios.guardar(usuario);
        return usuario.getUuid();
    }

    /**
     * Dar de alta un Rastreador en el sistema
     *
     * @param rastreador Rastreador a dar de alta
     * @return El rastreador registrado en el sistema
     */
    public Rastreador altaRastreador(@NotNull @Valid Rastreador rastreador) {
        if(repositorioRastreadores.buscar(rastreador.getDni()).isPresent()){
             throw new RastreadorYaRegistrado();
        }
        // Registrar Rastreador
        repositorioRastreadores.guardar(rastreador);
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
        
        Optional<Rastreador> rastreadorLogin = repositorioRastreadores.buscar(dni)
                .filter((rastreadores)->rastreadores.passwordValida(clave));

        if(rastreadorLogin.isEmpty()){
            throw new RastreadorNoRegistrado();
        }
        return rastreadorLogin.get().getUuid();
    }

    /**
     * Método para añadir una Lista de Contactos cercanos a un usuario
     *
     * @param contactos Contactos que se van a añadir a la lista del Usuario
     * @param uuidUsuario UUID del usuario al que se le añadirá el contacto
     */
    public void addContactoCercano(List<ContactoCercano> contactos, UUID uuidUsuario) {
         
        
        Usuario usuario = repositorioUsuarios.buscar(uuidUsuario).orElseThrow(UsuarioNoRegistrado::new);
       
        for (ContactoCercano contacto : contactos) {
            if (!usuario.getUuid().equals(contacto.getContacto().getUuid())) {
                usuario.addContactoCercano(contacto);
                //Hay que crear el repositorio para hacer esto
              // repositorioContactosCercanos.guardar(contacto);
            }
        }

    }

    /**
     * Método para ver los contactos cercanos de un Usuario
     *
     * @param uuid UUID del usuario
     * @param dniRastreador Dni del rastreador
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return Lista de contactos cercanos al usuario
     */
    public List<ContactoCercano> verContactosCercanos(UUID uuid, String dniRastreador, UUID uuidRastreador) {
        // Obtenemos el Rastreador
        Rastreador rastreador = repositorioRastreadores.buscar(dniRastreador).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que el rastreador está registrado
        if (!rastreador.getUuid().equals(uuidRastreador)) {
            throw new RastreadorNoRegistrado();
        }
        Usuario usuario = repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);
        return usuario.verContactosCercanos();
    }

    /**
     * Método para notificar el positivo a un Usuario
     *
     * @param uuid UUID del usuario a notificar el positivo
     * @param f_positivo Fecha y hora del positivo
     * @param dniRastreador DNI del rastreador que notifica el positivo
     * @param uuidRastreador UUID del rastreador obtenido en el login
     */
    public void notificarPos(UUID uuid, @PastOrPresent LocalDateTime f_positivo, String dniRastreador, UUID uuidRastreador) {
        // Obtenemos el Rastreador
        Rastreador rastreador = repositorioRastreadores.buscar(dniRastreador).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado
        if (rastreador.getUuid().equals(uuidRastreador)) {
            // Obtenemos el usuario
        Usuario usuario = repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);
            // Realizamos las operaciones
            rastreador.aumentarNotificados();
            usuario.setPositivo(true);
            usuario.setfPositivo(f_positivo);
            usuario.calcularRiesgoContactos();
            numTotalInf++;
            repositorioUsuarios.actualizar(usuario);
            repositorioRastreadores.actualizar(rastreador);
        }

    }

    /**
     * Método para notificar la curación de un positivo
     *
     * @param uuid UUID del usuario a notificar la curación
     * @param dniRastreador DNI del rastreador
     * @param uuidRastreador UUID del rastreador obtenido en el login
     */
    public void notificarCuracion(UUID uuid, String dniRastreador, UUID uuidRastreador) {
        Rastreador rastreador = repositorioRastreadores.buscar(dniRastreador).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado
        if (rastreador.getUuid().equals(uuidRastreador)) {
            Usuario usuario = repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);
            usuario.setPositivo(false);
            usuario.setfCuracion(LocalDate.now());
            repositorioUsuarios.actualizar(usuario);
        }
    }
//  TODO: A PARTIR DE AQUI DEBERIAMOS DE USAR JPQL. MAS EFICIENTE EN ESTADISTICOS
    /**
     * Método para obtener el Nº total de infectados
     *
     * @param dniRastreador DNI del rastreador
     * @param uuid_rastreador UUID del rastreador obtenido en el login
     * @return Nº total de infectados
     */
    public int totalInfectados(String dniRastreador, UUID uuidRastreador) {
        Rastreador rastreador = repositorioRastreadores.buscar(dniRastreador).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado
        if (!rastreador.getUuid().equals(uuidRastreador)) {
            throw new RastreadorNoRegistrado();
        }
        return numTotalInf;
    }

    /**
     * Método para obtener el Nº total de positivos que hay actualmente
     *
     * @param dniRastreador DNI del rastreador
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return Nº de positivos actualmente
     */
    public int positivos_actual(String dniRastreador, UUID uuidRastreador) {
        int positivos = 0;
        Rastreador rastreador = repositorioRastreadores.buscar(dniRastreador).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado
        if (rastreador.getUuid().equals(uuidRastreador)) {
            for (Usuario u : usuarios.values()) {
                if (u.isPositivo()) {
                    positivos++;
                }
            }
        }

        return positivos;
    }

    /**
     * Método para obtener el nº de positivos los últimos 15 días
     *
     * @param dniRastreador DNI del rastreador
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return Nº de positivos
     */
    public int positivos15Dias(String dniRastreador, UUID uuidRastreador) {
        int positivos = 0;
        Rastreador rastreador = Optional.ofNullable(this.rastreadores.get(dniRastreador)).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado
        if (rastreador.getUuid().equals(uuidRastreador)) {
            Iterator<Usuario> it = usuarios.values().iterator();

            LocalDateTime fecha15dias = LocalDateTime.now().minusDays(15);
            while (it.hasNext()) {
                Usuario usuario = it.next();

                // Probar testing
                if (usuario.isPositivo() && usuario.getfPositivo().isAfter(fecha15dias)) {
                    positivos++;
                }
            }
        }
        return positivos;
    }

    /**
     * Método para obtener el nº de personas que ha contagiado un usuario
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
        LocalDate fechaPositivo = usuario.getfPositivo().minusDays(15).toLocalDate();
        // Obtenemos la fecha de curacion
        LocalDate fechaCuracion = usuario.getfCuracion();
        if (fechaCuracion == null) {
            // Como no tiene fecha de curación, contaremos los contactos pasador 15 días desde hoy
            fechaCuracion = LocalDate.now();
        }
        // Recorremos todos los contactos del usuario
        for (int i = 0; i < contactos.size(); i++) {
            // Comprobamos si el contacto es positivo
            if (contactos.get(i).getContacto().isPositivo()) {
                // Obtenemos la fecha de contacto y la pasamos a LocalDate
                LocalDate fechaPositivoContacto = contactos.get(i).getFechaContacto().toLocalDate();
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
     * @param dniRastreador DNI del rastreador
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return La estadistica de contagiados por usuarios positivos
     */
    public double contagiadosXusuario(String dniRastreador, UUID uuidRastreador) {
        Rastreador rastreador = Optional.ofNullable(this.rastreadores.get(dniRastreador)).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado
        if (rastreador.getUuid().equals(uuidRastreador)) {
            double n_positivos_total = 0;
            double contagiados_total = 0;

            // Recorremos todos los usuarios 
            Iterator<Usuario> it = usuarios.values().iterator();

            while (it.hasNext()) {
                Usuario usuario = it.next();
                // Comprobamos que usuarios son positivos
                if (usuario.getfPositivo() != null) {
                    n_positivos_total++;
                    // Calculamos los contagiados producidos por el usuario
                    contagiados_total += this.contagiadosUsuario(usuario.getUuid());
                }
            }

            // Comprobamos que hay almenos una persona que es positivo
            if (n_positivos_total > 0) {
                return contagiados_total / n_positivos_total;
            }
        }
        return 0;
    }

    /**
     * Método pata obtener los positivos notificados por un rastreador
     *
     * @param dniRastreador DNI del rastreador
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return El nº de positivos notificados por el rastreador
     */
    public int positivosRastreador(String dniRastreador, UUID uuidRastreador) {
        Rastreador rastreador = Optional.ofNullable(this.rastreadores.get(dniRastreador)).orElseThrow(RastreadorNoRegistrado::new);
        if (rastreador.getUuid().equals(uuidRastreador)) {
            return rastreador.getNumTotalNotificados();
        }
        return 0;
    }
}
