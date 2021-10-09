/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.servicios;

import es.ujaen.dae.ujavid.entidades.Rastreador;
import es.ujaen.dae.ujavid.entidades.Usuario;
import es.ujaen.dae.ujavid.excepciones.UsuarioYaRegistrado;
import es.ujaen.dae.ujavid.excepciones.UsuarioNoRegistrado;
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
    /** Mapa con la lista de rastreadores */
    Map<String, Rastreador> rastreadores;
    /** Mapa con la lista de usuarios */
    Map<String,Usuario> usuarios;

    public ServicioUjaVid() {
        rastreadores= new TreeMap<>();
        usuarios = new TreeMap<>();
    }
    /**
     * Dar de alta usuario y crear una cuenta asociada
     * @param usuario el usuario a dar de alta
     * @return la cuenta asociada al cliente
     */
    public Usuario altaCliente(Usuario usuario) {
        if (usuarios.containsKey(usuario.getNumTelefono())) {
            throw new UsuarioYaRegistrado();
        }
        // Registrar cliente
        usuarios.put(usuario.getNumTelefono(), usuario);
        return usuario;        
    }
    
    
     /**
     * Realiza un login de un cliente
     * @param dni el DNI del cliente
     * @param clave la clave de acceso
     * @return el objeto de la clase Cliente asociado
     */
    public Optional<Usuario> loginCliente(String numTelefono,  String clave) {
        return Optional.ofNullable(usuarios.get(numTelefono)).filter((usuario)->usuario.passwordValida(clave));
    }
    
    /**
     * Devolver los contactos cercanos de un usuario dado
     * @param dni el DNI del cliente
     * @return la lista de cuentas
     */
    public List<Usuario> verContactosCercanos(String numTelefono) {
        Usuario usuario = Optional.ofNullable(usuarios.get(numTelefono)).orElseThrow(UsuarioNoRegistrado::new);
        return usuario.verContactosCercanos();
    }
    
}
