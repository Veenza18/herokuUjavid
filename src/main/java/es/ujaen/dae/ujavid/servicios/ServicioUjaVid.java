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
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 */
@Service
public class ServicioUjaVid {

    static int NUM_TOTAL_INF = 0;
    /**
     * Mapa con la lista de rastreadores
     */
    Map<String, Rastreador> rastreadores;
    /**
     * Mapa con la lista de usuarios
     */
    Map<String, Usuario> usuarios;

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
    public Usuario altaUsuario(Usuario usuario) {
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
     * @param dni el DNI del usuario
     * @param clave la clave de acceso
     * @return el objeto de la clase Usuario asociado
     */
    public Optional<Usuario> loginUsuario(String numTelefono, String clave) {
        return Optional.ofNullable(usuarios.get(numTelefono)).filter((usuario) -> usuario.passwordValida(clave));
    }

    /**
     * Dar de alta usuario y crear una cuenta asociada
     *
     * @param rastreador el rastredor a dar de alta
     * @return la cuenta asociada al rastreador
     */
    public Rastreador altaRastreador(Rastreador rastreador) {
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
     * @param dni el DNI del cliente
     * @return la lista de cuentas
     */
    public List<ContactoCercano> verContactosCercanos(String numTelefono) {
        Usuario usuario = Optional.ofNullable(usuarios.get(numTelefono)).orElseThrow(UsuarioNoRegistrado::new);
        return usuario.verContactosCercanos();
    }

    void notificarPos(String numTelefono, LocalDateTime f_positivo) {
        Usuario usuario = Optional.ofNullable(usuarios.get(numTelefono)).orElseThrow(UsuarioNoRegistrado::new);
        usuario.setPositivo(true);
        usuario.setF_positivo(f_positivo);
        NUM_TOTAL_INF++;
    }

    void notificarCuracion(String numTelefono) {
        Usuario usuario = Optional.ofNullable(usuarios.get(numTelefono)).orElseThrow(UsuarioNoRegistrado::new);
        usuario.setPositivo(false);
    }

    int totalInfectados() {
        return NUM_TOTAL_INF;
    }

    int positivos_actual() {
        int positivos = 0;

        Iterator<Usuario> it = usuarios.values().iterator();
        while (it.hasNext()) {
            if (it.next().isPositivo()) {
                positivos++;
            }
        }
        return positivos;
    }
}
