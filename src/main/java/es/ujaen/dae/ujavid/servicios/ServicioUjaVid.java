/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.servicios;

import es.ujaen.dae.ujavid.entidades.ContactoCercano;
import es.ujaen.dae.ujavid.controladoresREST.DTO.DTOContactoCercano;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
     * DAO de los usuarios del sistema
     */
    @Autowired
    RepositorioUsuarios repositorioUsuarios;

    /**
     * DAO de los rastreadores del sistema
     */
    @Autowired
    RepositorioRastreadores repositorioRastreadores;

    /**
     * Nº total de infectados
     */
    private int numTotalInf = 0;

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
        if (repositorioUsuarios.buscar(usuario.getUuid()).isPresent() || repositorioUsuarios.buscar(usuario.getNumTelefono()).isPresent()) {
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
        if (repositorioRastreadores.buscar(rastreador.getDni()).isPresent()) {
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
        Rastreador rastreadorLogin = repositorioRastreadores.buscar(dni)
                .filter((rastreadores) -> rastreadores.passwordValida(clave))
                .orElseThrow(RastreadorNoRegistrado::new);

        return rastreadorLogin.getUuid();
    }

    /**
     * Método para añadir una Lista de Contactos cercanos a un usuario
     *
     * @param contactos Contactos que se van a añadir a la lista del Usuario
     * @param uuidUsuario UUID del usuario al que se le añadirá el contacto
     */
    @Transactional
    public void addContactoCercano(List<DTOContactoCercano> contactos, UUID uuidUsuario) {

        Usuario usuario = repositorioUsuarios.buscar(uuidUsuario).orElseThrow(UsuarioNoRegistrado::new);

        for (DTOContactoCercano contacto : contactos) {
            if (!usuario.getUuid().equals(contacto.getUuid())) {
                Usuario usuarioContacto = repositorioUsuarios.buscar(contacto.getUuid()).orElseThrow(UsuarioNoRegistrado::new);
                ContactoCercano c1 = new ContactoCercano(contacto.getFechaContacto(), usuarioContacto, contacto.getDistancia(), contacto.getDuracion());
                usuario.addContactoCercano(c1);
                //Hay que crear el repositorio para hacer esto
                repositorioUsuarios.addContactoCercano(c1);

            }
        }
    }

    /**
     * Método para ver los contactos cercanos de un Usuario
     *
     * @param uuid UUID del usuario
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return Lista de contactos cercanos al usuario
     */
    @Transactional
    public List<ContactoCercano> verContactosCercanos(UUID uuid, UUID uuidRastreador) {
        // Comprobamos que es un  rastreador válido
        repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);

        // Comprobamos que es un usuario registrado
        repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);
        return repositorioUsuarios.verContactos(uuid);

        // return usuario.verContactosCercanos();
    }

    /**
     * Método para notificar el positivo a un Usuario
     *
     * @param uuid UUID del usuario a notificar el positivo
     * @param f_positivo Fecha y hora del positivo
     * @param uuidRastreador UUID del rastreador obtenido en el login
     */
    @Transactional
    public void notificarPos(UUID uuid, @PastOrPresent LocalDateTime f_positivo, UUID uuidRastreador) {
        // Obtenemos el Rastreador
        Rastreador rastreador = repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado

        // Obtenemos el usuario
        Usuario usuario = repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);
        // Realizamos las operaciones
        rastreador.aumentarNotificados();
        usuario.setPositivo(true);
        usuario.setfPositivo(f_positivo);
        usuario.calcularRiesgoContactos();
        numTotalInf++;

    }

    /**
     * Método para notificar la curación de un positivo
     *
     * @param uuid UUID del usuario a notificar la curación
     * @param uuidRastreador UUID del rastreador obtenido en el login
     */
    public void notificarCuracion(UUID uuid, UUID uuidRastreador) {
        Rastreador rastreador = repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado

        Usuario usuario = repositorioUsuarios.buscar(uuid).orElseThrow(UsuarioNoRegistrado::new);
        usuario.setPositivo(false);
        usuario.setfCuracion(LocalDate.now());
        repositorioUsuarios.actualizar(usuario);

    }

    /**
     * Método para obtener el Nº total de infectados
     *
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return Nº total de infectados
     */
    public int totalInfectados() {
        //Rastreador rastreador = repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);

        return numTotalInf;
    }

    /**
     * Método para obtener el Nº total de positivos que hay actualmente
     *
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return Nº de positivos actualmente
     */
    public int positivosActual() {
        int positivos = 0;
        //Rastreador rastreador = repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado

        positivos = repositorioUsuarios.positivosActual();

        return positivos;
    }

    /**
     * Método para obtener el nº de positivos los últimos 15 días
     *
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return Nº de positivos
     */
    public int positivos15Dias() {
        int positivos = 0;
        // Rastreador rastreador = this.repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);
        // Comprobamos que es un rastreador registrado

        positivos = this.repositorioUsuarios.positivos15Dias();

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
        Usuario usuario = repositorioUsuarios.buscar(uuidUsuario).orElseThrow(UsuarioNoRegistrado::new);
        // Obtenemos sus contactos cercanos
        List<ContactoCercano> contactos = repositorioUsuarios.verContactos(uuidUsuario);
        // Obtenemos la fecha del positivo del usuario y la pasamos a LocalDate
        if (usuario.getfPositivo() != null) {
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
        }

        return contagiados;

    }

    /**
     * Método para obtener uns estadística de contagiados / usuarios positivos
     *
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return La estadistica de contagiados por usuarios positivos
     */
    @Transactional
    public double contagiadosXusuario() {
        // Rastreador rastreador = this.repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);

        //  Comprobamos que es un rastreador registrado
        double n_positivos_total = 0;
        double contagiados_total = 0;

        // Obtenemos los usuarios que alguna vez han dado positivo
        List<Usuario> lista = this.repositorioUsuarios.positivosHistorial();
        n_positivos_total = lista.size();

        // Recorremos todos los usuarios  que alguna vez han dado positivo
        for (Usuario u : lista) {
            contagiados_total += this.contagiadosUsuario(u.getUuid());
        }

        // Comprobamos que hay almenos una persona que es positivo
        if (n_positivos_total > 0) {
            return contagiados_total / n_positivos_total;
        }

        return 0;
    }

    /**
     * Método pata obtener los positivos notificados por un rastreador
     *
     * @param uuidRastreador UUID del rastreador obtenido en el login
     * @return El nº de positivos notificados por el rastreador
     */
    public int positivosRastreador(UUID uuidRastreador) {
        Rastreador rastreador = this.repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);

        return rastreador.getNumTotalNotificados();

    }

    /**
     * Método para obtener un usuario completo
     *
     * @param uuidRastreador UUID del rastreador
     * @param uuidUsuario UUID del usuario
     * @return Usuario
     */
    public Optional<Usuario> devuelveUsuario(UUID uuidRastreador, UUID uuidUsuario) {
        Rastreador rastreador = this.repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);

        return repositorioUsuarios.buscar(uuidUsuario);

    }

    /**
     * Método para obtener un usuario completo
     *
     * @param uuidRastreador UUID del rastreador
     * @param uuidUsuario UUID del usuario
     * @return Usuario
     */
    public Optional<Usuario> devuelveUsuario(UUID uuidRastreador, String numTelefono) {
        Rastreador rastreador = this.repositorioRastreadores.buscar(uuidRastreador).orElseThrow(RastreadorNoRegistrado::new);

        return repositorioUsuarios.buscar(numTelefono);

    }

    /**
     * Método para obtener un Rastreador dado un dni
     *
     * @param uuidRastreador UUID del rastreador
     * @return Rastreador
     */
    public Optional<Rastreador> devuelveRastreador(UUID uuidRastreador) {
        Rastreador rastreador = this.repositorioRastreadores.buscar(uuidRastreador).get();

        return Optional.ofNullable(rastreador);
    }

    @Transactional
    public Optional<Rastreador> verRastreador(@NotBlank String dni) {
        Optional<Rastreador> rastreadorLogin = repositorioRastreadores.buscar(dni);

        // Asegurarnos de que se devuelve el cliente con los datos precargados
        return rastreadorLogin;
    }

}
