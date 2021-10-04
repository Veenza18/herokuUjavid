/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.servicios;

import es.ujaen.dae.ujavid.entidades.Rastreador;
import es.ujaen.dae.ujavid.entidades.Usuario;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author admin
 */
@Service
public class ServicioUjaVid {
    Map<String, Rastreador> rastreadores;
    Map<UUID,Usuario> usuarios;

    public ServicioUjaVid() {
        rastreadores= new TreeMap<>();
        usuarios = new TreeMap<>();
    }
    
}
