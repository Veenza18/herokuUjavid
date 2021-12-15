/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ujaen.dae.ujavid.seguridad;

import es.ujaen.dae.ujavid.entidades.Rastreador;
import es.ujaen.dae.ujavid.entidades.Usuario;
import es.ujaen.dae.ujavid.servicios.ServicioUjaVid;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio que proporciona los datos del Rastreador
 *
 * @author Venza
 */
@Service
public class ServicioDatosRastreador implements UserDetailsService {

    @Autowired
    ServicioUjaVid servicioUjaVid;

    PasswordEncoder encoder;

    public ServicioDatosRastreador() {
        encoder = new BCryptPasswordEncoder();
    }

    PasswordEncoder getEncoder() {
        return encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        Rastreador rastreador = servicioUjaVid.verRastreador(dni)
                .orElseThrow(() -> new UsernameNotFoundException(""));

        return User.withUsername(rastreador.getDni())
                .roles("RASTREADOR").password(rastreador.getPassword())
                .build();
    }

}
